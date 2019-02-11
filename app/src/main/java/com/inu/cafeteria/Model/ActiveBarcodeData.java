package com.inu.cafeteria.Model;

public class ActiveBarcodeData {

    private String activated;
    private String barcode;

    public ActiveBarcodeData() {
    }
    public ActiveBarcodeData(String activated, String barcode) {
        this.activated = activated;
        this.barcode = barcode;
    }

    public String getActivated() {
        return activated;
    }
    public String getBarcode() {
        return barcode;
    }


    public void setActivated(String activated) {
        this.activated = activated;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


}
