package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerPriceListAdapter;
import kr.co.kiosk.databinding.ActivityHomeBinding;
import kr.co.kiosk.databinding.ActivityMyOrderBinding;
import kr.co.kiosk.model.Price;

public class MyOrderActivity extends AppCompatActivity {

    ActivityMyOrderBinding binding;

//    private ArrayList<Price> priceListItems= new ArrayList<>();
    private ArrayList<Price> priceList= new ArrayList<>();
    private RecyclerPriceListAdapter priceListAdapter;

    private int[] num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 선택한 메뉴데이터들 가져오기
        Intent intent= getIntent();
        priceList= intent.getParcelableArrayListExtra("select_menu");

        priceListAdapter= new RecyclerPriceListAdapter(this, priceList);
        binding.recycler.setAdapter(priceListAdapter);

        binding.home.setOnClickListener(v-> clickedHome()); // 홈버튼 눌렀을 때
        binding.buyNow.setOnClickListener(v-> clickedBuyNow()); // 결제버튼 눌렀을 때
        binding.buyCancel.setOnClickListener(v-> clickedBuyCancel()); // 취소버튼 눌렀을 때

        num= new int[priceList.size()];
        clickedPlusOrMinus();
    }

    void clickedPlusOrMinus(){
        priceListAdapter.setItemClickListener(new RecyclerPriceListAdapter.OnItemClickListener() {

            // 선택한 메뉴 항목에 [+] 버튼을 눌렀을 때
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onAddClick(View view, int position) {

                num[position]+=1;

                priceList.set(position, new Price(priceList.get(position).menuName, num[position]+"", PriceAdd(priceList.get(position).menuPrice), R.drawable.plus, R.drawable.minus));


                priceListAdapter.notifyDataSetChanged();
            }

            // 선택한 메뉴 항목에 [-] 버튼을 눌렀을 때
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSubTractClick(View view, int position) {

                if (num[position]>1){
                    num[position]-=1;
                    priceList.set(position, new Price(priceList.get(position).menuName, num[position]+"", PriceAdd(priceList.get(position).menuPrice), R.drawable.plus, R.drawable.minus));
                    priceListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    String PriceAdd(String price){

        String s= price.replaceAll(",","");
        int number= Integer.parseInt(s);

        String result= number+"";

        return result;
    }

    private void clickedHome(){
        finish();
    }

    private void clickedBuyNow(){

        saveData();

        Toast.makeText(this, "주문 완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MyOrderActivity.this, PaymentActivity.class);
        startActivity(intent);
    }

    private void clickedBuyCancel(){
        Toast.makeText(this, "주문 취소", Toast.LENGTH_SHORT).show();
        finish();
    }

    //    - 파일 경로 : /sdcard/Android/data/%Package%/cache/history/history.log
    //    - 파일에 기록할 format : [yyyyMMdd HH:mm:ss][주문내역][결제금액]
    private void saveData() {
        // 외부메모리(SDcard, USB)가 있는가?
        String state= Environment.getExternalStorageState(); // 저장소 연결상태값 문자열 리턴함

        // 외장메모리상태(state)가 연결(mounted)되어 있지 않는가?
        if( !state.equals(Environment.MEDIA_MOUNTED) ){
            Toast.makeText(this, "SDcard is not mounted", Toast.LENGTH_SHORT).show();
            return;
        }else{

            // 2023.03.17 13:16:34 2개 6,000원 아메리카노

            long now= System.currentTimeMillis();
            Date today = new Date(now); // 현재시간에서 하루 더하기 : new Date(now+(1000*60*60*24*1))

            SimpleDateFormat sdf= new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

            String getDate= sdf.format(today);

            String[] name= new String[priceList.size()];
            String[] number= new String[priceList.size()];
            String[] price= new String[priceList.size()];

            // 파일에 저장할 데이터 가져오기
            for (int i=0; i<priceList.size(); i++){
                name[i]= priceList.get(i).menuName;
                number[i]= priceList.get(i).menuNumber;
                price[i]= priceList.get(i).menuPrice;
            }

            File path; // 외부메모리 영역의 경로를 관리하는 객체 참조변수

            File[] paths= getExternalCacheDirs();
            //paths[0]= getExternalFilesDir("history");;  // "history" 폴더명
            path= paths[0];

            // 위 경로에 "Data.txt"라는 이름의 파일을 만들기 위해 File객체 생성
            File file= new File(path, "history.log");

            try {
                // "history.log"까지 연결되는 문자스트림 생성
                FileWriter fw= new FileWriter(file, true);
                PrintWriter writer= new PrintWriter(fw); // 문자스트림 -> 보조문자스트림

                for (int i=0; i<priceList.size(); i++){
                    writer.println(getDate+"    "+number[i]+"개 "+price[i]+"원 "+name[i]);
                }


                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }




}





























