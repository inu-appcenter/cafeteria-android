package com.inu.cafeteria.Model;

public class CoverflowModel {

    public CoverflowModel() {
    }
    public CoverflowModel(int img) {
        this.img = img;
    }

    private int img;
    private int isMenu;

    public int getIsMenu() {
        return isMenu;
    }
    public void setIsMenu(int isMenu) {
        this.isMenu = isMenu;
    }

    public int getImg() {
        return img;
    }
    public void setImg(int img) {
        this.img = img;
    }

}
