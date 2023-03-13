package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String[] tabMenu= {"커피", "파르페", "밀크티", "디저트", "음료"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 홈화면 버튼 클릭시 메인화면으로 이동
        binding.home.setOnClickListener(v-> clickedHome());


    }

    void clickedHome(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}