package kr.co.kiosk.model;

public class Price {
    public String menuName;
    public String menuNumber;
    public String menuPrice;
    public int add;
    public int subtract;

    public Price(String menuName, String menuNumber, String menuPrice, int add, int subtract) {
        this.menuName = menuName;
        this.menuNumber = menuNumber;
        this.menuPrice = menuPrice;
        this.add = add;
        this.subtract = subtract;
    }

    public Price() {
    }
}
