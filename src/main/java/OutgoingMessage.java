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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

/**
 * OutgoingMessage resource placeholder for json/xml representation.
 *
 * @author Augustin Wang
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OutgoingMessage implements Serializable {

    private static final long serialVersionUID = 1987894975060698298L;

    @XmlElement(name = "to")
    private List<String> to;

    @XmlElement(name = "toChannel")
    private Long toChannel;

    @XmlElement(name = "eventType")
    private String eventType;

    @XmlElement(name = "content")
    private MessageContent content;

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public Long getToChannel() {
        return toChannel;
    }

    public void setToChannel(Long toChannel) {
        this.toChannel = toChannel;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }
}
