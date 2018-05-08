package com.ksblletba.orangemusic.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class NetworkSong{


    /**
     * name : Awake
     * id : 26319684
     * position : 23
     * alias : []
     * status : -1
     * fee : 0
     * copyrightId : 663018
     * disc : 1
     * no : 23
     * artists : [{"name":"宀╁磶鐞�","id":14056,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
     * album : {"name":"銈搞儳銈搞儳銇濡欍仾鍐掗櫤 O.S.T Battle Tendency [Leicht Verwendbar]","id":2450674,"type":"涓撹緫","size":26,"picId":2885118513728982,"blurPicUrl":"http://p1.music.126.net/VtEJb1kj4C9Fa5hTmu4A9Q==/2885118513728982.jpg","companyId":0,"pic":2885118513728982,"picUrl":"http://p1.music.126.net/VtEJb1kj4C9Fa5hTmu4A9Q==/2885118513728982.jpg","publishTime":1366905600007,"description":"","tags":"","company":"Warner Home Video","briefDesc":"","artist":{"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0},"songs":[],"alias":["TV鍔ㄧ敾銆奐OJO鐨勫濡欏啋闄� 鎴樻枟娼祦銆嬪師澹伴泦2"],"status":1,"copyrightId":0,"commentThreadId":"R_AL_3_2450674","artists":[{"name":"宀╁磶鐞�","id":14056,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}],"transNames":["jojo鐨勫濡欏啋闄┾�旀垬鏂楁疆娴佺瘒"]}
     * starred : false
     * popularity : 100.0
     * score : 100
     * starredNum : 0
     * duration : 201626
     * playedNum : 0
     * dayPlays : 0
     * hearTime : 0
     * ringtone :
     * crbt : null
     * audition : null
     * copyFrom :
     * commentThreadId : R_SO_4_26319684
     * rtUrl : null
     * ftype : 0
     * rtUrls : []
     * copyright : 2
     * hMusic : {"name":"Awake","id":27470404,"size":8103793,"extension":"mp3","sr":44100,"dfsId":3124812046187541,"bitrate":320000,"playTime":201626,"volumeDelta":-1.72}
     * mMusic : {"name":"Awake","id":27470405,"size":4070488,"extension":"mp3","sr":44100,"dfsId":3124812046187542,"bitrate":160000,"playTime":201626,"volumeDelta":-1.37}
     * lMusic : {"name":"Awake","id":27470406,"size":2456747,"extension":"mp3","sr":44100,"dfsId":3146802278742950,"bitrate":96000,"playTime":201626,"volumeDelta":-1.5}
     * bMusic : {"name":"Awake","id":27470406,"size":2456747,"extension":"mp3","sr":44100,"dfsId":3146802278742950,"bitrate":96000,"playTime":201626,"volumeDelta":-1.5}
     * mp3Url : http://m2.music.126.net/xqSRsMB9-xVF6Dw259EJaA==/3146802278742950.mp3
     * mvid : 0
     * rtype : 0
     * rurl : null
     */

    private String name;
    private int songId;
    private int position;
    private int status;
    private int fee;
    private int copyrightId;
    private String disc;
    private int no;
    private AlbumBean album;
    private boolean starred;
    private double popularity;
    private int score;
    private int starredNum;
    private int duration;
    private int playedNum;
    private int dayPlays;
    private int hearTime;
    private String ringtone;
    private Object crbt;
    private Object audition;
    private String copyFrom;
    private String commentThreadId;
    private Object rtUrl;
    private int ftype;
    private int copyright;
    private HMusicBean hMusic;
    private MMusicBean mMusic;
    private LMusicBean lMusic;
    private BMusicBean bMusic;
    private String mp3Url;
    private int mvid;
    private int rtype;
    private Object rurl;
    private List<?> alias;
    private List<ArtistsBeanX> artists;
    private List<?> rtUrls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getCopyrightId() {
        return copyrightId;
    }

    public void setCopyrightId(int copyrightId) {
        this.copyrightId = copyrightId;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public AlbumBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumBean album) {
        this.album = album;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStarredNum() {
        return starredNum;
    }

    public void setStarredNum(int starredNum) {
        this.starredNum = starredNum;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayedNum() {
        return playedNum;
    }

    public void setPlayedNum(int playedNum) {
        this.playedNum = playedNum;
    }

    public int getDayPlays() {
        return dayPlays;
    }

    public void setDayPlays(int dayPlays) {
        this.dayPlays = dayPlays;
    }

    public int getHearTime() {
        return hearTime;
    }

    public void setHearTime(int hearTime) {
        this.hearTime = hearTime;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public Object getCrbt() {
        return crbt;
    }

    public void setCrbt(Object crbt) {
        this.crbt = crbt;
    }

    public Object getAudition() {
        return audition;
    }

    public void setAudition(Object audition) {
        this.audition = audition;
    }

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public String getCommentThreadId() {
        return commentThreadId;
    }

    public void setCommentThreadId(String commentThreadId) {
        this.commentThreadId = commentThreadId;
    }

    public Object getRtUrl() {
        return rtUrl;
    }

    public void setRtUrl(Object rtUrl) {
        this.rtUrl = rtUrl;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public HMusicBean getHMusic() {
        return hMusic;
    }

    public void setHMusic(HMusicBean hMusic) {
        this.hMusic = hMusic;
    }

    public MMusicBean getMMusic() {
        return mMusic;
    }

    public void setMMusic(MMusicBean mMusic) {
        this.mMusic = mMusic;
    }

    public LMusicBean getLMusic() {
        return lMusic;
    }

    public void setLMusic(LMusicBean lMusic) {
        this.lMusic = lMusic;
    }

    public BMusicBean getBMusic() {
        return bMusic;
    }

    public void setBMusic(BMusicBean bMusic) {
        this.bMusic = bMusic;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public Object getRurl() {
        return rurl;
    }

    public void setRurl(Object rurl) {
        this.rurl = rurl;
    }

    public List<?> getAlias() {
        return alias;
    }

    public void setAlias(List<?> alias) {
        this.alias = alias;
    }

    public List<ArtistsBeanX> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsBeanX> artists) {
        this.artists = artists;
    }

    public List<?> getRtUrls() {
        return rtUrls;
    }

    public void setRtUrls(List<?> rtUrls) {
        this.rtUrls = rtUrls;
    }

    public static class AlbumBean {
        /**
         * name : 銈搞儳銈搞儳銇濡欍仾鍐掗櫤 O.S.T Battle Tendency [Leicht Verwendbar]
         * id : 2450674
         * type : 涓撹緫
         * size : 26
         * picId : 2885118513728982
         * blurPicUrl : http://p1.music.126.net/VtEJb1kj4C9Fa5hTmu4A9Q==/2885118513728982.jpg
         * companyId : 0
         * pic : 2885118513728982
         * picUrl : http://p1.music.126.net/VtEJb1kj4C9Fa5hTmu4A9Q==/2885118513728982.jpg
         * publishTime : 1366905600007
         * description :
         * tags :
         * company : Warner Home Video
         * briefDesc :
         * artist : {"name":"","id":0,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}
         * songs : []
         * alias : ["TV鍔ㄧ敾銆奐OJO鐨勫濡欏啋闄� 鎴樻枟娼祦銆嬪師澹伴泦2"]
         * status : 1
         * copyrightId : 0
         * commentThreadId : R_AL_3_2450674
         * artists : [{"name":"宀╁磶鐞�","id":14056,"picId":0,"img1v1Id":0,"briefDesc":"","picUrl":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","albumSize":0,"alias":[],"trans":"","musicSize":0}]
         * transNames : ["jojo鐨勫濡欏啋闄┾�旀垬鏂楁疆娴佺瘒"]
         */

        private String name;
        private int id;
        private String type;
        private int size;
        private long picId;
        private String blurPicUrl;
        private int companyId;
        private long pic;
        private String picUrl;
        private long publishTime;
        private String description;
        private String tags;
        private String company;
        private String briefDesc;
        private ArtistBean artist;
        private int status;
        private int copyrightId;
        private String commentThreadId;
        private List<?> songs;
        private List<String> alias;
        private List<ArtistsBean> artists;
        private List<String> transNames;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getPicId() {
            return picId;
        }

        public void setPicId(long picId) {
            this.picId = picId;
        }

        public String getBlurPicUrl() {
            return blurPicUrl;
        }

        public void setBlurPicUrl(String blurPicUrl) {
            this.blurPicUrl = blurPicUrl;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public long getPic() {
            return pic;
        }

        public void setPic(long pic) {
            this.pic = pic;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public ArtistBean getArtist() {
            return artist;
        }

        public void setArtist(ArtistBean artist) {
            this.artist = artist;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public String getCommentThreadId() {
            return commentThreadId;
        }

        public void setCommentThreadId(String commentThreadId) {
            this.commentThreadId = commentThreadId;
        }

        public List<?> getSongs() {
            return songs;
        }

        public void setSongs(List<?> songs) {
            this.songs = songs;
        }

        public List<String> getAlias() {
            return alias;
        }

        public void setAlias(List<String> alias) {
            this.alias = alias;
        }

        public List<ArtistsBean> getArtists() {
            return artists;
        }

        public void setArtists(List<ArtistsBean> artists) {
            this.artists = artists;
        }

        public List<String> getTransNames() {
            return transNames;
        }

        public void setTransNames(List<String> transNames) {
            this.transNames = transNames;
        }

        public static class ArtistBean {
            /**
             * name :
             * id : 0
             * picId : 0
             * img1v1Id : 0
             * briefDesc :
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * albumSize : 0
             * alias : []
             * trans :
             * musicSize : 0
             */

            private String name;
            private int id;
            private int picId;
            private int img1v1Id;
            private String briefDesc;
            private String picUrl;
            private String img1v1Url;
            private int albumSize;
            private String trans;
            private int musicSize;
            private List<?> alias;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }

        public static class ArtistsBean {
            /**
             * name : 宀╁磶鐞�
             * id : 14056
             * picId : 0
             * img1v1Id : 0
             * briefDesc :
             * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * albumSize : 0
             * alias : []
             * trans :
             * musicSize : 0
             */

            private String name;
            private int id;
            private int picId;
            private int img1v1Id;
            private String briefDesc;
            private String picUrl;
            private String img1v1Url;
            private int albumSize;
            private String trans;
            private int musicSize;
            private List<?> alias;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public int getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(int img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getBriefDesc() {
                return briefDesc;
            }

            public void setBriefDesc(String briefDesc) {
                this.briefDesc = briefDesc;
            }

            public String getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public String getTrans() {
                return trans;
            }

            public void setTrans(String trans) {
                this.trans = trans;
            }

            public int getMusicSize() {
                return musicSize;
            }

            public void setMusicSize(int musicSize) {
                this.musicSize = musicSize;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }
    }

    public static class HMusicBean {
        /**
         * name : Awake
         * id : 27470404
         * size : 8103793
         * extension : mp3
         * sr : 44100
         * dfsId : 3124812046187541
         * bitrate : 320000
         * playTime : 201626
         * volumeDelta : -1.72
         */

        private String name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class MMusicBean {
        /**
         * name : Awake
         * id : 27470405
         * size : 4070488
         * extension : mp3
         * sr : 44100
         * dfsId : 3124812046187542
         * bitrate : 160000
         * playTime : 201626
         * volumeDelta : -1.37
         */

        private String name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class LMusicBean {
        /**
         * name : Awake
         * id : 27470406
         * size : 2456747
         * extension : mp3
         * sr : 44100
         * dfsId : 3146802278742950
         * bitrate : 96000
         * playTime : 201626
         * volumeDelta : -1.5
         */

        private String name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class BMusicBean {
        /**
         * name : Awake
         * id : 27470406
         * size : 2456747
         * extension : mp3
         * sr : 44100
         * dfsId : 3146802278742950
         * bitrate : 96000
         * playTime : 201626
         * volumeDelta : -1.5
         */

        private String name;
        private int id;
        private int size;
        private String extension;
        private int sr;
        private long dfsId;
        private int bitrate;
        private int playTime;
        private double volumeDelta;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getExtension() {
            return extension;
        }

        public void setExtension(String extension) {
            this.extension = extension;
        }

        public int getSr() {
            return sr;
        }

        public void setSr(int sr) {
            this.sr = sr;
        }

        public long getDfsId() {
            return dfsId;
        }

        public void setDfsId(long dfsId) {
            this.dfsId = dfsId;
        }

        public int getBitrate() {
            return bitrate;
        }

        public void setBitrate(int bitrate) {
            this.bitrate = bitrate;
        }

        public int getPlayTime() {
            return playTime;
        }

        public void setPlayTime(int playTime) {
            this.playTime = playTime;
        }

        public double getVolumeDelta() {
            return volumeDelta;
        }

        public void setVolumeDelta(double volumeDelta) {
            this.volumeDelta = volumeDelta;
        }
    }

    public static class ArtistsBeanX {
        /**
         * name : 宀╁磶鐞�
         * id : 14056
         * picId : 0
         * img1v1Id : 0
         * briefDesc :
         * picUrl : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * albumSize : 0
         * alias : []
         * trans :
         * musicSize : 0
         */

        private String name;
        private int id;
        private int picId;
        private int img1v1Id;
        private String briefDesc;
        private String picUrl;
        private String img1v1Url;
        private int albumSize;
        private String trans;
        private int musicSize;
        private List<?> alias;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public int getImg1v1Id() {
            return img1v1Id;
        }

        public void setImg1v1Id(int img1v1Id) {
            this.img1v1Id = img1v1Id;
        }

        public String getBriefDesc() {
            return briefDesc;
        }

        public void setBriefDesc(String briefDesc) {
            this.briefDesc = briefDesc;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getImg1v1Url() {
            return img1v1Url;
        }

        public void setImg1v1Url(String img1v1Url) {
            this.img1v1Url = img1v1Url;
        }

        public int getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(int albumSize) {
            this.albumSize = albumSize;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public int getMusicSize() {
            return musicSize;
        }

        public void setMusicSize(int musicSize) {
            this.musicSize = musicSize;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }
    }
}
