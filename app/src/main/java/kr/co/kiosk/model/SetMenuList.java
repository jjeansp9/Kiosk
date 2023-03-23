package kr.co.kiosk.model;

public class SetMenuList {

    public String setMenuName;
    public String setMenuPrice;
    public String setMenuImage;
    public String setMenuInfo;
    public int setMenuDelete;

    public SetMenuList(String setMenuName, String setMenuPrice, String setMenuImage, String setMenuInfo, int setMenuDelete) {
        this.setMenuName = setMenuName;
        this.setMenuPrice = setMenuPrice;
        this.setMenuImage = setMenuImage;
        this.setMenuInfo = setMenuInfo;
        this.setMenuDelete = setMenuDelete;
    }

    public SetMenuList() {
    }
}
