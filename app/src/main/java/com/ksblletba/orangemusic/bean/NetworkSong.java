package com.ksblletba.orangemusic.bean;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class NetworkSong extends DataSupport{


    /**
     * id : 520460927
     * name : 如风过境
     * artists : [{"id":12893319,"name":"哎哟蔚蔚","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0,"trans":null}]
     * album : {"id":36854253,"name":"如风过境","artist":{"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0,"trans":null},"publishTime":1511884800007,"size":2,"copyrightId":405024,"status":0,"picId":109951163072107970}
     * duration : 263779
     * copyrightId : 405024
     * status : 0
     * alias : []
     * rtype : 0
     * ftype : 0
     * mvid : 0
     * fee : 8
     * rUrl : null
     */

    private int Songid;
    private String name;
    private AlbumBean album;
    private int duration;
    private int copyrightId;
    private int status;
    private int rtype;
    private int ftype;
    private int mvid;
    private int fee;
    private Object rUrl;
    private List<ArtistsBean> artists;
    private List<?> alias;


    public int getSongid() {
        return Songid;
    }

    public void setSongid(int songid) {
        Songid = songid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumBean getAlbum() {
        return album;
    }

    public void setAlbum(AlbumBean album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCopyrightId() {
        return copyrightId;
    }

    public void setCopyrightId(int copyrightId) {
        this.copyrightId = copyrightId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public int getMvid() {
        return mvid;
    }

    public void setMvid(int mvid) {
        this.mvid = mvid;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public Object getRUrl() {
        return rUrl;
    }

    public void setRUrl(Object rUrl) {
        this.rUrl = rUrl;
    }

    public List<ArtistsBean> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistsBean> artists) {
        this.artists = artists;
    }

    public List<?> getAlias() {
        return alias;
    }

    public void setAlias(List<?> alias) {
        this.alias = alias;
    }

    public static class AlbumBean {
        /**
         * id : 36854253
         * name : 如风过境
         * artist : {"id":0,"name":"","picUrl":null,"alias":[],"albumSize":0,"picId":0,"img1v1Url":"http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg","img1v1":0,"trans":null}
         * publishTime : 1511884800007
         * size : 2
         * copyrightId : 405024
         * status : 0
         * picId : 109951163072107970
         */

        private int id;
        private String name;
        private ArtistBean artist;
        private long publishTime;
        private int size;
        private int copyrightId;
        private int status;
        private long picId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArtistBean getArtist() {
            return artist;
        }

        public void setArtist(ArtistBean artist) {
            this.artist = artist;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCopyrightId() {
            return copyrightId;
        }

        public void setCopyrightId(int copyrightId) {
            this.copyrightId = copyrightId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getPicId() {
            return picId;
        }

        public void setPicId(long picId) {
            this.picId = picId;
        }

        public static class ArtistBean {
            /**
             * id : 0
             * name :
             * picUrl : null
             * alias : []
             * albumSize : 0
             * picId : 0
             * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
             * img1v1 : 0
             * trans : null
             */

            private int id;
            private String name;
            private Object picUrl;
            private int albumSize;
            private int picId;
            private String img1v1Url;
            private int img1v1;
            private Object trans;
            private List<?> alias;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(Object picUrl) {
                this.picUrl = picUrl;
            }

            public int getAlbumSize() {
                return albumSize;
            }

            public void setAlbumSize(int albumSize) {
                this.albumSize = albumSize;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public int getImg1v1() {
                return img1v1;
            }

            public void setImg1v1(int img1v1) {
                this.img1v1 = img1v1;
            }

            public Object getTrans() {
                return trans;
            }

            public void setTrans(Object trans) {
                this.trans = trans;
            }

            public List<?> getAlias() {
                return alias;
            }

            public void setAlias(List<?> alias) {
                this.alias = alias;
            }
        }
    }

    public static class ArtistsBean {
        /**
         * id : 12893319
         * name : 哎哟蔚蔚
         * picUrl : null
         * alias : []
         * albumSize : 0
         * picId : 0
         * img1v1Url : http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
         * img1v1 : 0
         * trans : null
         */

        private int id;
        private String name;
        private Object picUrl;
        private int albumSize;
        private int picId;
        private String img1v1Url;
        private int img1v1;
        private Object trans;
        private List<?> alias;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(Object picUrl) {
            this.picUrl = picUrl;
        }

        public int getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(int albumSize) {
            this.albumSize = albumSize;
        }

        public int getPicId() {
            return picId;
        }

        public void setPicId(int picId) {
            this.picId = picId;
        }

        public String getImg1v1Url() {
            return img1v1Url;
        }

        public void setImg1v1Url(String img1v1Url) {
            this.img1v1Url = img1v1Url;
        }

        public int getImg1v1() {
            return img1v1;
        }

        public void setImg1v1(int img1v1) {
            this.img1v1 = img1v1;
        }

        public Object getTrans() {
            return trans;
        }

        public void setTrans(Object trans) {
            this.trans = trans;
        }

        public List<?> getAlias() {
            return alias;
        }

        public void setAlias(List<?> alias) {
            this.alias = alias;
        }
    }
}
