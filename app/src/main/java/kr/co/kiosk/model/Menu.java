package kr.co.kiosk.model;

public class Menu {
    public String menuName;
    public String menuPrice;
    public int menuImage;

    public Menu(String menuName, String menuPrice, int menuImage) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuImage = menuImage;
    }

    public Menu() {
    }
}
