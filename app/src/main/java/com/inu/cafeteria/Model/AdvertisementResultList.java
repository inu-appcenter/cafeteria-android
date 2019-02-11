package com.inu.cafeteria.Model;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class AdvertisementResultList implements Serializable {

    private String no;
    private String title;
    private String img;
    private String previewing;
    private String url;
    private ArrayList<AdvertisementResult.Contents> contents;

    public AdvertisementResultList() {
    }

    public AdvertisementResultList(AdvertisementResult item) {
        this.no = item.getNo();
        this.title = item.getTitle();
        this.img = item.getImg();
        this.previewing = item.getPreviewing();
        this.url = item.getUrl();
        this.contents = item.getContents();
    }


    public AdvertisementResultList(String img) {
        this.img = img;
    }

    public AdvertisementResultList(String no, String title, String img, String url, ArrayList<AdvertisementResult.Contents> contents) {
        this.no = no;
        this.title = title;
        this.img = img;
        this.url = url;
        this.contents = contents;
    }


    public static class Contents implements Serializable {

        private String title;
        private String msg;

        public String getTitle() {
            return title;
        }
        public String getMsg() {
            return msg;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }


        public Contents(String title, String msg) {
            this.title = title;
            this.msg = msg;
        }

    }


    public String getNo() {
        return no;
    }
    public String getTitle() {
        return title;
    }
    public String getImg() {
        return img;
    }
    public String getPreviewing() {
        return previewing;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<AdvertisementResult.Contents> getContents() {
        return contents;
    }

    public void setNo(String no) {
        this.no = no;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setPreviewing(String previewing) {
        this.previewing = previewing;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setContents(ArrayList<AdvertisementResult.Contents> contents) {
        this.contents = contents;
    }


}
