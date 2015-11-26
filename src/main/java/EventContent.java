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
public class EventContent implements Serializable {

    private static final long serialVersionUID = -4215800316500130607L;

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "contentType")
    private Integer contentType;

    @XmlElement(name = "toType")
    private Integer toType;

    @XmlElement(name = "createdTime")
    private Long createdTime;

    @XmlElement(name = "deliveredTime")
    private Long deliveredTime;

    @XmlElement(name = "from")
    private String from;

    @XmlElement(name = "to")
    private List<String> to;

    @XmlElement(name = "contentMetadata")
    private ContentMetadata contentMetadata;

    @XmlElement(name = "text")
    private String text;

    @XmlElement(name = "location")
    private Location location;

    @XmlElement(name = "seq")
    private Object seq;

    @XmlElement(name = "revision")
    private Integer revision;

    @XmlElement(name = "opType")
    private Integer opType;

    @XmlElement(name = "params")
    private List<String> params;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "reqSeq")
    private Integer reqSeq;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public Integer getToType() {
        return toType;
    }

    public void setToType(Integer toType) {
        this.toType = toType;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(Long deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public ContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    public void setContentMetadata(ContentMetadata contentMetadata) {
        this.contentMetadata = contentMetadata;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Object getSeq() {
        return seq;
    }

    public void setSeq(Object seq) {
        this.seq = seq;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getOpType() {
        return opType;
    }

    public void setOpType(Integer opType) {
        this.opType = opType;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getReqSeq() {
        return reqSeq;
    }

    public void setReqSeq(Integer reqSeq) {
        this.reqSeq = reqSeq;
    }
}
