package com.ksblletba.orangemusic.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlayListItem {

    /**
     * id : 2150055953
     * name : 缁嗘暟閭ｄ簺鍊煎緱鍗曟洸寰幆鐨勬皯璋�
     * coverImgUrl : https://p1.music.126.net/Hcf_U-bipM4nl8AQbWsqvQ==/19031446765754182.jpg
     * creator : {"nickname":"楣跨櫧宸�","userId":493707309,"userType":0,"authStatus":0,"expertTags":["娴佽","鍗庤","娆х編"],"experts":{"2":"璧勮杈句汉锛氭儏鎰�"}}
     * subscribed : false
     * trackCount : 300
     * userId : 493707309
     * playCount : 7043090
     * bookCount : 143543
     * highQuality : false
     */

    private long id;
    private String name;
    private String coverImgUrl;
    private CreatorBean creator;
    private boolean subscribed;
    private int trackCount;
    private int userId;
    private int playCount;
    private int bookCount;
    private boolean highQuality;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public CreatorBean getCreator() {
        return creator;
    }

    public void setCreator(CreatorBean creator) {
        this.creator = creator;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public boolean isHighQuality() {
        return highQuality;
    }

    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }

    public static class CreatorBean {
        /**
         * nickname : 楣跨櫧宸�
         * userId : 493707309
         * userType : 0
         * authStatus : 0
         * expertTags : ["娴佽","鍗庤","娆х編"]
         * experts : {"2":"璧勮杈句汉锛氭儏鎰�"}
         */

        private String nickname;
        private int userId;
        private int userType;
        private int authStatus;
        private ExpertsBean experts;
        private List<String> expertTags;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public int getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(int authStatus) {
            this.authStatus = authStatus;
        }

        public ExpertsBean getExperts() {
            return experts;
        }

        public void setExperts(ExpertsBean experts) {
            this.experts = experts;
        }

        public List<String> getExpertTags() {
            return expertTags;
        }

        public void setExpertTags(List<String> expertTags) {
            this.expertTags = expertTags;
        }

        public static class ExpertsBean {
            /**
             * 2 : 璧勮杈句汉锛氭儏鎰�
             */

            @SerializedName("2")
            private String _$2;

            public String get_$2() {
                return _$2;
            }

            public void set_$2(String _$2) {
                this._$2 = _$2;
            }
        }
    }
}
