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

@XmlAccessorType(XmlAccessType.FIELD)
public class ContentMetadata implements Serializable {

    private static final long serialVersionUID = 3943325586376949936L;

    @XmlElement(name = "STKPKGID")
    private String stkPkgId;

    @XmlElement(name = "STKID")
    private String stkId;

    @XmlElement(name = "STKVER")
    private String stkVer;

    @XmlElement(name = "STKTXT")
    private String stkTxt;

    @XmlElement(name = "mid")
    private String mid;

    @XmlElement(name = "displayName")
    private String displayName;

    @XmlElement(name = "AUDLEN")
    private String audLen;

    public String getStkPkgId() {
        return stkPkgId;
    }

    public void setStkPkgId(String stkPkgId) {
        this.stkPkgId = stkPkgId;
    }

    public String getStkId() {
        return stkId;
    }

    public void setStkId(String stkId) {
        this.stkId = stkId;
    }

    public String getStkVer() {
        return stkVer;
    }

    public void setStkVer(String stkVer) {
        this.stkVer = stkVer;
    }

    public String getStkTxt() {
        return stkTxt;
    }

    public void setStkTxt(String stkTxt) {
        this.stkTxt = stkTxt;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAudLen() {
        return audLen;
    }

    public void setAudLen(String audLen) {
        this.audLen = audLen;
    }
}
