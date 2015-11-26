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

@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Serializable {

    private static final long serialVersionUID = 7071655373271526507L;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "createdTime")
    private Long createdTime;

    @XmlElement(name = "eventType")
    private String eventType;

    @XmlElement(name = "from")
    private String from;

    @XmlElement(name = "fromChannel")
    private String fromChannel;

    @XmlElement(name = "to")
    private List<String> to;

    @XmlElement(name = "toChannel")
    private String toChannel;

    @XmlElement(name = "content")
    private EventContent content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Long createdTime)
    {
        this.createdTime = createdTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromChannel() {
        return fromChannel;
    }

    public void setFromChannel(String fromChannel) {
        this.fromChannel = fromChannel;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getToChannel() {
        return toChannel;
    }

    public void setToChannel(String toChannel) {
        this.toChannel = toChannel;
    }

    public EventContent getContent() {
        return content;
    }

    public void setContent(EventContent content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("id:%s,createdTime:%d,from:%s,fromChannel:%s,to:%s,toChannel:%s,eventType:%s",
                id, createdTime, from, fromChannel, to, toChannel, eventType);
    }
}
