package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import kr.co.kiosk.fragments.CoffeeFragment;
import kr.co.kiosk.fragments.DessertFragment;
import kr.co.kiosk.fragments.DrinkFragment;
import kr.co.kiosk.fragments.MilkTeaFragment;
import kr.co.kiosk.fragments.ParfaitFragment;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.Price;
import kr.co.kiosk.model.PriceCategory;

public class MyOrderActivity extends AppCompatActivity {

    ActivityMyOrderBinding binding;

    private ArrayList<Price> priceList= new ArrayList<>();
    private RecyclerPriceListAdapter priceListAdapter;
    private String priceResult="";

    MenuDBHelper dbHelper;

    Cursor cursor;

    StringBuffer buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMyOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이미 선택한 메뉴를 중복선택 하지않게 설정하는 변수
        HomeActivity.selectList.add(0, HomeActivity.selectCoffee);
        HomeActivity.selectList.add(1, HomeActivity.selectParfait);
        HomeActivity.selectList.add(2, HomeActivity.selectMilkTea);
        HomeActivity.selectList.add(3, HomeActivity.selectDessert);
        HomeActivity.selectList.add(4, HomeActivity.selectDrink);

        dbHelper= new MenuDBHelper(this);

        // 선택한 메뉴데이터들 가져오기
        Intent intent= getIntent();
        priceList= intent.getParcelableArrayListExtra("select_menu");
        priceResult= intent.getStringExtra("price_result");
        binding.priceResult.setText(priceResult);

        priceListAdapter= new RecyclerPriceListAdapter(this, priceList);
        binding.recycler.setAdapter(priceListAdapter);

        //binding.home.setOnClickListener(v-> clickedHome()); // 홈버튼 눌렀을 때
        binding.buyNow.setOnClickListener(v-> clickedBuyNow()); // 결제버튼 눌렀을 때
        binding.buyCancel.setOnClickListener(v-> clickedBuyCancel()); // 취소버튼 눌렀을 때

        clickedPlusOrMinus();
    }

    void clickedPlusOrMinus(){
        priceListAdapter.setItemClickListener(new RecyclerPriceListAdapter.OnItemClickListener() {

            // 선택한 메뉴 항목에 [+] 버튼을 눌렀을 때
            @Override
            public void onAddClick(View view, int position) {

                HomeActivity.num[position]+=1;

                Log.d("priceList", priceList.size()+"");

                priceList.set(position, new Price(priceList.get(position).menuName, HomeActivity.num[position]+"", addPrice(position , priceList.get(position).menuPrice), R.drawable.plus, R.drawable.minus));
                binding.priceResult.setText(resultPrice());

                priceListAdapter.notifyDataSetChanged();
            }

            // 선택한 메뉴 항목에 [-] 버튼을 눌렀을 때
            @Override
            public void onSubTractClick(View view, int position) {

                if (HomeActivity.num[position]>1){

                    HomeActivity.num[position]-=1;
                    priceList.set(position, new Price(
                            priceList.get(position).menuName,
                            HomeActivity.num[position]+"",
                            subTractPrice(position ,
                            priceList.get(position).menuPrice),
                            R.drawable.plus, R.drawable.minus
                    ));
                    binding.priceResult.setText(resultPrice());
                    priceListAdapter.notifyDataSetChanged();

                }else if (priceList.size()==1){

                    cursor= dbHelper.getDataAll();

                    buffer= new StringBuffer();

                    int[] i={0,0,0,0,0};

                    while (cursor.moveToNext()){
                        buffer.append("category : " + cursor.getString(1)+"\n");
                        buffer.append("cursorName : " + cursor.getString(2)+"\n");

                        if (cursor.getString(1).equals("커피")){
                            if (priceList.get(position).menuName.equals(cursor.getString(2))){

                                priceList.remove(position);

                                binding.priceResult.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            i[0]++;

                        }
                        if (cursor.getString(1).equals("파르페")){
                            if (cursor.getString(2).equals(priceList.get(position).menuName)){

                                priceList.remove(position);

                                binding.priceResult.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            i[1]++;
                        }

                        if (cursor.getString(1).equals("밀크티")){
                            if (cursor.getString(2).equals(priceList.get(position).menuName)){

                                priceList.remove(position);

                                binding.priceResult.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            i[2]++;
                        }
                        if (cursor.getString(1).equals("디저트")){
                            if (cursor.getString(2).equals(priceList.get(position).menuName)){

                                priceList.remove(position);

                                binding.priceResult.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            i[3]++;
                        }
                        if (cursor.getString(1).equals("음료")){
                            if (cursor.getString(2).equals(priceList.get(position).menuName)){

                                priceList.remove(position);

                                binding.priceResult.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            i[4]++;
                        }
                    } // while

                }else if (priceList.get(position).menuNumber.equals("1")) {

                    try {
                        cursor = dbHelper.getDataAll();
                        buffer = new StringBuffer();

                        int[] i = {0, 0, 0, 0, 0};

                        while (cursor.moveToNext()) {

                            buffer.append("category : " + cursor.getString(1) + "\n");
                            buffer.append("cursorName : " + cursor.getString(2) + "\n");

                            if (cursor.getString(1).equals("커피")) {
                                if (priceList.get(position).menuName.equals(cursor.getString(2))) {

                                    if (position+1 == priceList.size()){

                                        priceList.remove(position);
                                        HomeActivity.num[position] = 1;

                                    }else if (position+1 < priceList.size()){

                                        priceList.remove(position);
                                        for (int j = 0; j < priceList.size(); j++) {
                                            HomeActivity.num[position + j] = HomeActivity.num[position + j + 1];
                                        }
                                    }

                                    binding.priceResult.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[0]++;

                            }
                            if (cursor.getString(1).equals("파르페")) {
                                if (priceList.get(position).menuName.equals(cursor.getString(2))) {

                                    if (position+1 == priceList.size()){

                                        priceList.remove(position);
                                        HomeActivity.num[position] = 1;

                                    }else if (position+1 < priceList.size()){

                                        priceList.remove(position);
                                        for (int j = 0; j < priceList.size(); j++) {
                                            HomeActivity.num[position + j] = HomeActivity.num[position + j + 1];
                                        }
                                    }

                                    binding.priceResult.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[1]++;

                            }
                            if (cursor.getString(1).equals("밀크티")) {
                                if (priceList.get(position).menuName.equals(cursor.getString(2))) {

                                    if (position+1 == priceList.size()){

                                        priceList.remove(position);
                                        HomeActivity.num[position] = 1;

                                    }else if (position+1 < priceList.size()){

                                        priceList.remove(position);
                                        for (int j = 0; j < priceList.size(); j++) {
                                            HomeActivity.num[position + j] = HomeActivity.num[position + j + 1];
                                        }
                                    }

                                    binding.priceResult.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[2]++;
                            }
                            if (cursor.getString(1).equals("디저트")) {
                                if (priceList.get(position).menuName.equals(cursor.getString(2))) {

                                    if (position+1 == priceList.size()){

                                        priceList.remove(position);
                                        HomeActivity.num[position] = 1;

                                    }else if (position+1 < priceList.size()){

                                        priceList.remove(position);
                                        for (int j = 0; j < priceList.size(); j++) {
                                            HomeActivity.num[position + j] = HomeActivity.num[position + j + 1];
                                        }
                                    }

                                    binding.priceResult.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[3]++;
                            }
                            if (cursor.getString(1).equals("음료")) {
                                if (priceList.get(position).menuName.equals(cursor.getString(2))) {

                                    if (position+1 == priceList.size()){

                                        priceList.remove(position);
                                        HomeActivity.num[position] = 1;

                                    }else if (position+1 < priceList.size()){

                                        priceList.remove(position);
                                        for (int j = 0; j < priceList.size(); j++) {
                                            HomeActivity.num[position + j] = HomeActivity.num[position + j + 1];
                                        }
                                    }

                                    binding.priceResult.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[4]++;
                            }
                        } // while
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // 메뉴 수량증가 할 때마다 금액 더하기
    private String addPrice(int position, String price){

        String result= "";
        String s= price.replaceAll(",","");
        int value=0;

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        while (cursor.moveToNext()){
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");

            if (cursor.getString(1).equals("커피")){
                Log.d("DBNAME", cursor.getString(2));

                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }

            }else if(cursor.getString(1).equals("파르페")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("밀크티")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("디저트")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("음료")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }
        } // while

        result= Integer.parseInt(s)+value+"";

        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    // 메뉴 수량감소 할 때마다 금액 빼기
    public String subTractPrice(int position, String price){

        String result= "";
        String s= price.replaceAll(",","");
        int value=0;

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        while (cursor.moveToNext()){
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");

            if (cursor.getString(1).equals("커피")){
                Log.d("DBNAME", cursor.getString(2));

                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("파르페")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("밀크티")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("디저트")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("음료")){
                if (cursor.getString(2).equals(priceList.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }
        } // while

        result= Integer.parseInt(s)-value+"";

        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    public String resultPrice(){

        String result= "";

        int num = 0;
        for (int i=0; i<priceList.size(); i++){
            String s= priceList.get(i).menuPrice.replaceAll(",","");
            num+= Integer.parseInt(s);
        }
        result= num+"";
        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    private void clickedBuyNow(){

        if (priceList.size()==0){
            Toast.makeText(this, "메뉴가 비어있습니다.", Toast.LENGTH_SHORT).show();
        }else{
            saveData();

            HomeActivity.priceListItems.clear();

            for (int i= 0; i< HomeActivity.selectList.size(); i++) HomeActivity.selectList.get(i).clear();

            HomeActivity.selectList.clear();

            HomeActivity.selectList.add(0, HomeActivity.selectCoffee);
            HomeActivity.selectList.add(1, HomeActivity.selectParfait);
            HomeActivity.selectList.add(2, HomeActivity.selectMilkTea);
            HomeActivity.selectList.add(3, HomeActivity.selectDessert);
            HomeActivity.selectList.add(4, HomeActivity.selectDrink);

            HomeActivity.binding.resultPrice.setText(resultPrice());

            CoffeeFragment.oneTouch= 0;
            ParfaitFragment.oneTouch= 0;
            MilkTeaFragment.oneTouch= 0;
            DessertFragment.oneTouch= 0;
            DrinkFragment.oneTouch= 0;

            HomeActivity.priceListAdapter.notifyDataSetChanged();

            Toast.makeText(this, "주문 완료", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyOrderActivity.this, PaymentActivity.class);
            intent.putExtra("select_menu", priceList);
            intent.putExtra("price_result", resultPrice());
            startActivity(intent);
        }

    }

    private void clickedBuyCancel(){

        Toast.makeText(this, "주문 취소", Toast.LENGTH_SHORT).show();

        for (int i= 0; i< HomeActivity.selectList.size(); i++) HomeActivity.selectList.get(i).clear();

        HomeActivity.selectList.clear();

        HomeActivity.selectList.add(0, HomeActivity.selectCoffee);
        HomeActivity.selectList.add(1, HomeActivity.selectParfait);
        HomeActivity.selectList.add(2, HomeActivity.selectMilkTea);
        HomeActivity.selectList.add(3, HomeActivity.selectDessert);
        HomeActivity.selectList.add(4, HomeActivity.selectDrink);

        HomeActivity.binding.resultPrice.setText(resultPrice());

        CoffeeFragment.oneTouch= 0;
        ParfaitFragment.oneTouch= 0;
        MilkTeaFragment.oneTouch= 0;
        DessertFragment.oneTouch= 0;
        DrinkFragment.oneTouch= 0;

        HomeActivity.priceListAdapter.notifyDataSetChanged();

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





























