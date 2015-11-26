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

/**
 * MessageContent resource placeholder for json/xml representation.
 *
 * @author Augustin Wang
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageContent implements Serializable {

    private static final long serialVersionUID = -4415357761846250398L;

    @XmlElement(name = "contentType")
    private Integer contentType;

    @XmlElement(name = "toType")
    private Integer toType;

    @XmlElement(name = "text")
    private String text;

    @XmlElement(name = "originalContentUrl")
    private String originalContentUrl;

    @XmlElement(name = "previewImageUrl")
    private String previewImageUrl;

    @XmlElement(name = "contentMetadata")
    private ContentMetadata contentMetadata;

    @XmlElement(name = "location")
    private Location location;

    @XmlElement(name = "notificationDisabled")
    private Boolean notificationDisabled;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOriginalContentUrl() {
        return originalContentUrl;
    }

    public void setOriginalContentUrl(String originalContentUrl) {
        this.originalContentUrl = originalContentUrl;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public ContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    public void setContentMetadata(ContentMetadata contentMetadata) {
        this.contentMetadata = contentMetadata;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getNotificationDisabled() {
        return notificationDisabled;
    }

    public void setNotificationDisabled(Boolean notificationDisabled) {
        this.notificationDisabled = notificationDisabled;
    }
}
