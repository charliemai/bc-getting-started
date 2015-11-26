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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.AbstractFuture;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ApiHttpClient {
    private final int timeoutInMillis = 600;
    private final int maxConnections = 100;

    private AsyncRestTemplate asyncRestTemplate;
    private HttpHeaders httpHeaders;

    private ObjectMapper objectMapper;

    public ApiHttpClient(final String channelAccessToken) {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeoutInMillis)
                .setConnectTimeout(timeoutInMillis)
                .build();

        CloseableHttpAsyncClient asyncClient = HttpAsyncClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .addInterceptorLast((HttpRequest httpRequest, HttpContext httpContext) -> {
                    httpRequest.addHeader("X-Line-ChannelToken", channelAccessToken);
                    httpRequest.addHeader("Content-Type", "application/json; charser=UTF-8");
                    httpRequest.removeHeaders("Accept");
                    httpRequest.addHeader("Accept", "application/json; charset=UTF-8");
                })
                .setMaxConnTotal(maxConnections)
                .setMaxConnPerRoute(maxConnections)
                .disableCookieManagement()
                .build();

        asyncRestTemplate = new AsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory(asyncClient));
        asyncRestTemplate.setErrorHandler(new ApiResponseErrorHandler());

        httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Line-ChannelToken", channelAccessToken);
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        List<MediaType> list = new ArrayList<>();
        list.add(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.setAccept(list);

        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(TypeFactory.defaultInstance()));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private class ApiResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
            if (clientHttpResponse.getStatusCode() != HttpStatus.OK) {
                // Log clientHttpResponse.getStatusCode() and clientHttpResponse.getBody()
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            if (clientHttpResponse.getStatusCode() != HttpStatus.OK) {
                throw new IOException(String.format("Fail to access API: [%d]%s",
                        clientHttpResponse.getStatusCode().value(), clientHttpResponse.getStatusText()));
            }
        }
    }

    public <T> com.google.common.util.concurrent.ListenableFuture<T> getApiData(final String url, Class<T> clazz) {
        ListenableFuture<ResponseEntity<String>> listenableFuture = asyncRestTemplate.getForEntity(url, String.class);
        ApiResponseHandler<T> responseHandler = new ApiResponseHandler<>(url, clazz);
        listenableFuture.addCallback(responseHandler);
        return responseHandler;
    }

    public <T> com.google.common.util.concurrent.ListenableFuture<T> postApiData(final String url, String content,
                                                                                 Class<T> clazz) {
        HttpEntity<String> httpEntity = new HttpEntity<>(content, httpHeaders);
        ListenableFuture<ResponseEntity<String>> listenableFuture = asyncRestTemplate.postForEntity(url, httpEntity,
                String.class);
        ApiResponseHandler<T> responseHandler = new ApiResponseHandler<>(url, clazz);
        listenableFuture.addCallback(responseHandler);
        return responseHandler;
    }

    private class ApiResponseHandler<T> extends AbstractFuture<T>
            implements ListenableFutureCallback<ResponseEntity<String>> {
        private String url;
        private Class<T> clazz;

        public ApiResponseHandler(String url, Class<T> clazz) {
            this.url = url;
            this.clazz = clazz;
        }

        @Override
        public void onFailure(Throwable throwable) {
            setException(throwable);
        }

        @Override
        public void onSuccess(ResponseEntity<String> stringResponseEntity) {
            if (stringResponseEntity.getStatusCode() != HttpStatus.OK &&
                    stringResponseEntity.getStatusCode() != HttpStatus.CREATED) {
                System.err.println(String.format("Received %s for url %s", stringResponseEntity.getStatusCode(), url));
            }
            try {
                T t = objectMapper.readValue(stringResponseEntity.getBody(), clazz);
                set(t);
            } catch (IOException e) {
                onFailure(e);
            }
        }
    }

    public com.google.common.util.concurrent.ListenableFuture<ProfileList> getProfileList(final String url) {
        return getApiData(url, ProfileList.class);
    }

    public com.google.common.util.concurrent.ListenableFuture<ApiResponse> sendMessage(final String url, Object msgObj) {
        String message;
        try {
            message = objectMapper.writeValueAsString(msgObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fail to convert object to JSON", e);
        }
        return postApiData(url, message, ApiResponse.class);
    }
}
