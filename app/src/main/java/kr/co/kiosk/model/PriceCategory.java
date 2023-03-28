package kr.co.kiosk.model;

import java.util.ArrayList;

public class PriceCategory {
    public ArrayList<Integer> coffee= new ArrayList<>();
    public ArrayList<Integer> parfait= new ArrayList<>();
    public ArrayList<Integer> milkTea= new ArrayList<>();
    public ArrayList<Integer> dessert= new ArrayList<>();
    public ArrayList<Integer> drink= new ArrayList<>();

    public PriceCategory(ArrayList<Integer> coffee, ArrayList<Integer> parfait, ArrayList<Integer> milkTea, ArrayList<Integer> dessert, ArrayList<Integer> drink) {
        this.coffee = coffee;
        this.parfait = parfait;
        this.milkTea = milkTea;
        this.dessert = dessert;
        this.drink = drink;
    }

    public PriceCategory() {
    }
}
