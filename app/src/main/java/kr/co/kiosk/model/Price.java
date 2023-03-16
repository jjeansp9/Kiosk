package kr.co.kiosk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Price implements Parcelable {
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

    protected Price(Parcel in) {
        menuName = in.readString();
        menuNumber = in.readString();
        menuPrice = in.readString();
        add = in.readInt();
        subtract = in.readInt();
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(menuName);
        parcel.writeString(menuNumber);
        parcel.writeString(menuPrice);
        parcel.writeInt(add);
        parcel.writeInt(subtract);
    }
}
