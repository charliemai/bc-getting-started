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
public class Constants {

    /**
     * ContentType defines the content types of an event from LINE platform.
     */
    public class ContentType {
        public static final int TEXT = 1;
        public static final int IMAGE = 2;
        public static final int VIDEO = 3;
        public static final int AUDIO = 4;
        public static final int LOCATION = 7;
        public static final int STICKER = 8;
        public static final int CONTACT = 10;
    }

    /**
     * EventType defines the event types of LINE platform.
     */
    public class EventType {
        public static final String MESSAGE = "138311609000106303";
        public static final String OPERATION = "138311609100106403";
        public static final String OUTGOING_MESSAGE = "138311608800106203";
        public static final String OUTGOING_MULTI_MESSAGE = "140177271400161403";
        public static final String LINK_MESSAGE = "137299299800026303";
    }

    /**
     * OperationType defines the operation types of an event from LINE platform.
     */
    public class OperationType {
        public static final int ADDED_AS_FRIEND = 4;
        public static final int INVITE_TO_GROUP = 5;
        public static final int ADD_TO_ROOM = 7;
        public static final int BLOCKED_FROM_FRIEND = 8;
    }

    /**
     * ToType defines the to types of an event from LINE platform.
     */
    public class ToType {
        public static final int USER = 1;
    }

    /**
     * ToChannel define the channel Id for different type of outgoing message.
     */
    public class ToChannel {
        public static final long MESSAGE = 1383378250L;
        public static final long MULTI_MESSAGE = 1383378250L;
        public static final long LINK_MESSAGE = 1341301715L;
    }
}
