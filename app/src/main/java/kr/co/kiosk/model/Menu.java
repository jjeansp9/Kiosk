package kr.co.kiosk.model;

public class Menu {
    public String menuName;
    public String menuPrice;
    public String menuImage;
    public int menuInfo;

    public Menu(String menuName, String menuPrice, String menuImage, int menuInfo) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuImage = menuImage;
        this.menuInfo = menuInfo;
    }

    public Menu() {
    }

    public static class MenuDummy{
        public String menuName;
        public String menuPrice;
        public int menuImage;
        public int menuInfo;

        public MenuDummy(String menuName, String menuPrice, int menuImage, int menuInfo) {
            this.menuName = menuName;
            this.menuPrice = menuPrice;
            this.menuImage = menuImage;
            this.menuInfo = menuInfo;
        }

        public MenuDummy() {
        }
    }
}


