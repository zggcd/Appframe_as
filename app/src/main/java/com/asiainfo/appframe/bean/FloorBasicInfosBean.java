package com.asiainfo.appframe.bean;

public class FloorBasicInfosBean {
    /**
     * createtime : {"date":7,"day":5,"hours":11,"minutes":26,"month":8,"seconds":49,"time":1536290809000,"timezoneOffset":-480,"year":118}
     * floorExtends : {"id":40}
     * homeid : 42
     * id : 40
     * picurl : http://jiaofeng.tunnel.testpub.net/gateway/img/app1/%E8%AE%A2%E8%B4%AD%E6%88%98%E6%8A%A5.png
     * showhome : 1
     * sort : 1
     * subhead : 详细的订购数据
     * title : 订购战报
     */

    private CreatetimeBean createtime;
    private FloorExtendsBean floorExtends;
    private int homeid;
    private int id;
    private String picurl;
    private int showhome;
    private int sort;
    private String subhead;
    private String title;

    public CreatetimeBean getCreatetime() {
        return createtime;
    }

    public void setCreatetime(CreatetimeBean createtime) {
        this.createtime = createtime;
    }

    public FloorExtendsBean getFloorExtends() {
        return floorExtends;
    }

    public void setFloorExtends(FloorExtendsBean floorExtends) {
        this.floorExtends = floorExtends;
    }

    public int getHomeid() {
        return homeid;
    }

    public void setHomeid(int homeid) {
        this.homeid = homeid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public int getShowhome() {
        return showhome;
    }

    public void setShowhome(int showhome) {
        this.showhome = showhome;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class CreatetimeBean {
        /**
         * date : 7
         * day : 5
         * hours : 11
         * minutes : 26
         * month : 8
         * seconds : 49
         * time : 1536290809000
         * timezoneOffset : -480
         * year : 118
         */

        private int date;
        private int day;
        private int hours;
        private int minutes;
        private int month;
        private int seconds;
        private long time;
        private int timezoneOffset;
        private int year;

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHours() {
            return hours;
        }

        public void setHours(int hours) {
            this.hours = hours;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getTimezoneOffset() {
            return timezoneOffset;
        }

        public void setTimezoneOffset(int timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }

    public static class FloorExtendsBean {
        /**
         * id : 40
         */

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
