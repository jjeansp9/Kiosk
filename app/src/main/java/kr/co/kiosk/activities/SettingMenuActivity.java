package kr.co.kiosk.activities;

import static kr.co.kiosk.model.MenuDBHelper.TABLE_NAME;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivitySettingMenuBinding;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;

public class SettingMenuActivity extends AppCompatActivity {

    ActivitySettingMenuBinding binding;
    Uri uri;

    // 숫자 천단위에 [,]를 찍기위한 변수
    DecimalFormat myFormatter= new DecimalFormat("###,###");
    String result= "";

    MenuDBHelper dbHelper;
    String category= "커피";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new MenuDBHelper(this);

        binding.coffee.setBackgroundColor(Color.parseColor("#000000"));

        // 등록할 메뉴의 카테고리 선택
        binding.coffee.setOnClickListener(v->clickedCategory("커피"));
        binding.parfait.setOnClickListener(v->clickedCategory("파르페"));
        binding.milkTea.setOnClickListener(v->clickedCategory("밀크티"));
        binding.dessert.setOnClickListener(v->clickedCategory("디저트"));
        binding.drink.setOnClickListener(v->clickedCategory("음료"));

        binding.goBack.setOnClickListener(v-> finish()); // 뒤로가기 버튼

        binding.imgMenuImage.setOnClickListener(v-> clickedImageSelect()); // 메뉴이미지 선택해서 디바이스 사진첩에 접근 후 사진 선택
        binding.etMenuPrice.addTextChangedListener(commaAddForNumber()); // 메뉴가격에 숫자입력할 때 천단위마다 [,] 표시

        // 데이터베이스에서 CRUD 작업하는 메소드
        binding.btnInsert.setOnClickListener(v-> clickedInsert()); // 메뉴 추가
//        binding.btnUpdate.setOnClickListener(v-> clickedUpdate());
//        binding.btnDelete.setOnClickListener(v-> clickedDelete());
        binding.btnListMenu.setOnClickListener(v-> menuList()); // 메뉴 목록
    }

    // 클릭한 카테고리 탭에 따라 배경색상 변경 메소드
    public void clickedCategory(String category){

        if (category.equals("커피")){binding.coffee.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.coffee.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("파르페")){binding.parfait.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.parfait.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("밀크티")){binding.milkTea.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.milkTea.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("디저트")){binding.dessert.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.dessert.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("음료")){binding.drink.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.drink.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        Toast.makeText(this, category+"메뉴 등록하기", Toast.LENGTH_SHORT).show();
    }

    void clickedImageSelect(){
        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
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

        String name= binding.etMenuName.getText().toString(); // EditText에 입력한 문자열을 메뉴이름에 대입
        String price= binding.etMenuPrice.getText().toString(); // EditText에 입력한 문자열을 메뉴가격에 대입
        String image= String.valueOf(uri); // 사진첩에서 가져온 사진의 uri경로를 문자열로 메뉴이미지에 대입
        String info= binding.etMenuInfo.getText().toString(); // EditText에 입력한 문자열을 메뉴가격에 대입

        // 메뉴이름란에 글자가 없는경우
        if (name.replace(" ", "").equals("")) Toast.makeText(this, " 메뉴 이름을 입력해주세요", Toast.LENGTH_SHORT).show();

        // 메뉴가격란에 글자가 없는경우
        else if (price.replace(" ", "").equals("")) Toast.makeText(this, " 메뉴 가격을 입력해주세요", Toast.LENGTH_SHORT).show();

        // 사진을 등록하지 않은경우
        else if (uri==null) Toast.makeText(this, "사진을 등록해주세요", Toast.LENGTH_SHORT).show();

        // 모두 입력한 경우 데이터베이스 테이블에 추가
        else {
            dbHelper.insertData(category, name, price, image, info);

            setMenuInfo();
            Toast.makeText(this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    // 메뉴데이터 수정 [ 메뉴이름, 메뉴가격, 메뉴이미지 ]
    void clickedUpdate(){
        String name= binding.etMenuName.getText().toString(); //
        String price= binding.etMenuPrice.getText().toString(); // EditText에 입력한 문자열을 메뉴가격에 대입
        String image= String.valueOf(uri); // 사진첩에서 가져온 사진의 uri경로를 문자열로 메뉴이미지에 대입
        String info= binding.etMenuInfo.getText().toString(); // EditText에 입력한 문자열을 메뉴가격에 대입

        dbHelper.updateData(category, name, price, image, info);

        setMenuInfo();
        Toast.makeText(this, name+"의 메뉴를 수정하였습니다..", Toast.LENGTH_SHORT).show();
    }

    // 해당 메뉴이름을 가진 데이터 항목 모두 삭제
    void clickedDelete(){
        String name= binding.etMenuName.getText().toString();
        dbHelper.deleteData(name);

        Toast.makeText(this, name+"의 메뉴를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
    }

    // 등록한 메뉴 모두 보여주기
    void clickedListMenu(){
        Cursor cursor= dbHelper.getDataAll();

        if (cursor.getCount() == 0) showDialog("error", "데이터가 없습니다.");
        else{

            StringBuffer buffer= new StringBuffer();
            while (cursor.moveToNext()){
                buffer.append("id : " + cursor.getString(0)+"\n");
                buffer.append("category : " + cursor.getString(1)+"\n");
                buffer.append("name : " + cursor.getString(2)+"\n");
                buffer.append("price : " + cursor.getString(3)+"\n\n");
                buffer.append("image : " + cursor.getString(4)+"\n\n");
                buffer.append("info : " + cursor.getString(5)+"\n\n");

                Log.d("uri", cursor.getString(4));
            }
            showDialog(dbHelper.getDatabaseName(), buffer.toString());
        }
    }

    public void showDialog(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    // 메뉴목록
    private void menuList(){
        Intent intent= new Intent(SettingMenuActivity.this, MenuList.class);
        startActivity(intent);
        finish();
    }

    void setMenuInfo(){
        binding.etMenuName.setText("");
        binding.etMenuPrice.setText("");
        binding.etMenuInfo.setText("");
        binding.imgMenuImage.setImageResource(R.drawable.ic_upload_image);
    }
}



























