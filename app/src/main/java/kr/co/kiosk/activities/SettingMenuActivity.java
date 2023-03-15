package kr.co.kiosk.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivitySettingMenuBinding;

public class SettingMenuActivity extends AppCompatActivity {

    ActivitySettingMenuBinding binding;
    int select= 0;
    Uri uri;

    DecimalFormat myFormatter= new DecimalFormat("###,###");
    String result= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.coffee.setOnClickListener(v-> clickedCoffee()); // 등록할 메뉴의 카테고리 선택
        binding.imgMenuImage.setOnClickListener(v-> clickedImageSelect()); // 메뉴이미지 선택해서 디바이스 사진첩에 접근 후 사진 선택
        binding.etMenuPrice.addTextChangedListener(commaAddForNumber()); // 메뉴가격에 숫자입력할 때 천단위마다 [,] 표시

        // 데이터베이스에서 CRUD 작업하는 메소드
        binding.btnInsert.setOnClickListener(v-> clickedInsert());
        binding.btnUpdate.setOnClickListener(v-> clickedUpdate());
        binding.btnDelete.setOnClickListener(v-> clickedDelete());
        binding.btnListMenu.setOnClickListener(v-> clickedListMenu());


    }

    void clickedCoffee(){
        if (select % 2==0){binding.coffee.setBackgroundColor(Color.parseColor("#000000"));
        }else{ binding.coffee.setBackgroundColor(Color.parseColor("#8A8A8A")); }
        select++;
    }

    void clickedImageSelect(){
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityResult.launch(intent);
    }


    // 사진업로드 관련 객체
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        uri = result.getData().getData();

                        Glide.with(SettingMenuActivity.this).load(uri).into(binding.imgMenuImage);
                        Log.d("ImgURI", uri+"");
                    }
                }
            });

    // 메뉴가격 숫자 입력란에 천단위마다 [,]를 표시하기 위한 메소드
    TextWatcher commaAddForNumber(){
        TextWatcher watcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result= myFormatter.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    binding.etMenuPrice.setText(result);
                    binding.etMenuPrice.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        return watcher;
    } // commaAddForNumber()

    // 메뉴데이터 테이블에 추가 [ 메뉴이름, 메뉴가격, 메뉴이미지 ]
    void clickedInsert(){

    }

    // 메뉴데이터 수정 [ 메뉴이름, 메뉴가격, 메뉴이미지 ]
    void clickedUpdate(){

    }

    // 해당 메뉴이름을 가진 데이터 항목 모두 삭제
    void clickedDelete(){

    }

    // 등록한 메뉴 모두 보여주기
    void clickedListMenu(){

    }
}



























