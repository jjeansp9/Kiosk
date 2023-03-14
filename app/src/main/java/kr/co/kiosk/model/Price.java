package kr.co.kiosk.model;

public class Price {
    public String menuName;
    public String menuNumber;
    public String menuPrice;

    public Price(String menuName, String menuNumber, String menuPrice) {
        this.menuName = menuName;
        this.menuNumber = menuNumber;
        this.menuPrice = menuPrice;
    }

    public Price() {
    }
}
