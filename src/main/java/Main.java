/*
 * Copyright 2015 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import com.google.common.util.concurrent.ListenableFuture;
import com.heroku.sdk.jdbc.DatabaseUrl;
import org.apache.commons.codec.binary.Base64;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static spark.Spark.*;

public class Main {

    final static String API_URL_EVENTS = "https://api.line.me/v1/events";
    final static String API_URL_PROFILES = "https://api.line.me/v1/profiles";

    public static void main(String[] args) {

        String channelSecret = System.getenv("CHANNEL_SECRET");
        String channelAccessToken = System.getenv("CHANNEL_ACCESS_TOKEN");
        if (channelSecret == null || channelSecret.isEmpty() || channelAccessToken == null ||
                channelAccessToken.isEmpty()) {
            System.err.println("Error! Environment variable CHANNEL_SECRET and CHANNEL_ACCESS_TOKEN not defined.");
            return;
        }

        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        post("/events", (request, response) -> {
            String requestBody = request.body();
            String channelSignature = request.headers("X-LINE-CHANNELSIGNATURE");
            if (channelSignature == null || channelSignature.isEmpty()) {
                response.status(400);
                return "Please provide valid channel signature and try again.";
            }

            if (!validateBCRequest(requestBody, channelSecret, channelSignature)) {
                response.status(401);
                return "Invalid channel signature.";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            EventList events;
            try {
                events = objectMapper.readValue(requestBody, EventList.class);
            } catch (IOException e) {
                response.status(400);
                return "Invalid request body.";
            }

            ApiHttpClient apiHttpClient = new ApiHttpClient(channelAccessToken);

            List<String> toUsers;
            for (Event event : events.getResult()) {
                switch (event.getEventType()) {
                    case Constants.EventType.MESSAGE:
                        toUsers = new ArrayList<>();
                        toUsers.add(event.getContent().getFrom());
                        sendTextMessage(toUsers, "You said: " + event.getContent().getText(), apiHttpClient);
                        break;
                    case Constants.EventType.OPERATION:
                        if (event.getContent().getOpType() == Constants.OperationType.ADDED_AS_FRIEND) {
                            String newFriend = event.getContent().getParams().get(0);
                            Profile profile = getProfile(newFriend, apiHttpClient);
                            String displayName = profile == null ? "Unknown" : profile.getDisplayName();
                            toUsers = new ArrayList<>();
                            toUsers.add(newFriend);
                            sendTextMessage(toUsers, displayName + ", welcome to be my friend!", apiHttpClient);
                            Connection connection = null;
                            connection = DatabaseUrl.extract().getConnection();
                            toUsers = getFriends(newFriend, connection);
                            if (toUsers.size() > 0) {
                                sendTextMessage(toUsers, displayName +
                                        " just join us, let's welcome him/her!", apiHttpClient);
                            }
                            addFriend(newFriend, displayName, connection);
                            if (connection != null) {
                                connection.close();
                            }
                        }
                        break;
                    default:
                        // Unknown type?
                }
            }
            return "Events received successfully.";
        });

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("message", "Hello World!");
            return new ModelAndView(attributes, "index.ftl");
        }, new FreeMarkerEngine());
    }

    private static boolean validateBCRequest(String httpRequestBody, String channelSecret, String channelSignature) {
        if (httpRequestBody == null || channelSecret == null || channelSignature == null) {
            return false;
        }

        String signature;
        SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] source = httpRequestBody.getBytes("UTF-8");
            signature = Base64.encodeBase64String(mac.doFinal(source));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return channelSignature.equals(signature);
    }

    private static void sendTextMessage(List<String> toUsers, String message, ApiHttpClient apiHttpClient) {
        OutgoingMessage outgoingMessage = new OutgoingMessage();
        outgoingMessage.setTo(toUsers);
        outgoingMessage.setToChannel(Constants.ToChannel.MESSAGE);
        outgoingMessage.setEventType(Constants.EventType.OUTGOING_MESSAGE);
        MessageContent messageContent = new MessageContent();
        messageContent.setContentType(Constants.ContentType.TEXT);
        messageContent.setToType(Constants.ToType.USER);
        messageContent.setText(message);
        outgoingMessage.setContent(messageContent);

        ListenableFuture<ApiResponse> apiListenableFuture = apiHttpClient.sendMessage(API_URL_EVENTS, outgoingMessage);
        if (apiListenableFuture != null) {
            try {
                ApiResponse apiResponse = apiListenableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private static Profile getProfile(String mid, ApiHttpClient apiHttpClient) {
        ProfileList profileList = null;
        String url = String.format("%s?mids=%s", API_URL_PROFILES, mid);
        ListenableFuture<ProfileList> apiListenableFuture = apiHttpClient.getProfileList(url);
        if (apiListenableFuture != null) {
            try {
                profileList = apiListenableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (profileList.getContacts().size() > 0) {
            return profileList.getContacts().get(0);
        }
        return null;
    }

    private static void addFriend(String mid, String displayName, Connection connection) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS friends (mid VARCHAR(100) CONSTRAINT friends_pk PRIMARY KEY, " +
                    "display VARCHAR(300), occasion TIMESTAMP)");
            stmt.executeUpdate("INSERT INTO friends VALUES ('" + mid + "', '" + displayName + "', now())");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getFriends(String current_mid, Connection connection) {
        List<String> output = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT mid FROM friends WHERE mid <> '" + current_mid + "'");
            while (rs.next()) {
                output.add(rs.getString("mid"));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

}
