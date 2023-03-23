package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.co.kiosk.R;

public class MenuList extends AppCompatActivity {

    String[] tabMenu= {"커피","파르페","밀크티","디저트","음료"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);



    }


}