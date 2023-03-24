package kr.co.kiosk.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityEndBinding;
import kr.co.kiosk.model.Price;

public class EndActivity extends AppCompatActivity {

    ActivityEndBinding binding;

    ArrayList<Price> priceList= new ArrayList<>();
    String priceResult="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityEndBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent= getIntent();
        priceList= intent.getParcelableArrayListExtra("select_menu");
        priceResult= intent.getStringExtra("price_result");

        //binding.home.setOnClickListener(v->clickedHome()); // 홈버튼
        binding.btnConfirm.setOnClickListener(v->clickedConfirm()); // 주문화인 버튼
        binding.btnGoBack.setOnClickListener(v->clickedGoBack()); // 돌아가기 버튼
    }

    void clickedHome(){
        // 지금까지 쌓아놓은 모든 Activity 종료하고 MainActivity만 실행
        Intent intent= new Intent(EndActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void clickedConfirm(){

        ArrayList<String> list= new ArrayList<>();
        StringBuffer buffer= new StringBuffer();

        for (int i=0; i<priceList.size(); i++){
            buffer.append(priceList.get(i).menuName + " " + priceList.get(i).menuNumber + "개 " + priceList.get(i).menuPrice+"원\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("주문목록");
        builder.setMessage(buffer + "\n\n" +"총합 : "+ priceResult+"원");
        builder.show();
    }

    void clickedGoBack(){
        // 지금까지 쌓아놓은 모든 Activity 종료하고 MainActivity만 실행
        Intent intent= new Intent(EndActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}