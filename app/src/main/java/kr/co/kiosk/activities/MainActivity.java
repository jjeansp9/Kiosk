package kr.co.kiosk.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerMenuAdapter;
import kr.co.kiosk.databinding.ActivityMainBinding;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int[] category={0,1,2,3,4};

    String password= "1233";
    MenuDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper= new MenuDBHelper(this);
        //Log.d("dahelper", dbHelper.getDataAll().getCount()+"");

        binding.category1.setOnClickListener(v-> clickedCategory("커피 메뉴로 이동", 0));
        binding.category2.setOnClickListener(v-> clickedCategory("파르페 메뉴로 이동", 1));
        binding.category3.setOnClickListener(v-> clickedCategory("밀크티 메뉴로 이동", 2));
        binding.category4.setOnClickListener(v-> clickedCategory("디저트 메뉴로 이동", 3));
        binding.category5.setOnClickListener(v-> clickedCategory("음료 메뉴로 이동", 4));

        clickedListMenu();
        binding.settings.setOnClickListener(v-> clickedSettings());

        checkPermission(); // 외부저장소 권한요청
    }

    // 클릭한 카테고리 값을 얻어와서 값에 해당하는 카테고리로 이동
    private void clickedCategory(String msg, int num){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[num]);
        startActivity(intent);
    }

    void clickedSettings(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.dialog_login, null);

        final EditText pw= loginLayout.findViewById(R.id.pw);

        // 비밀번호 :1233 [ 잘못 입력한 경우 관리자설정으로 갈 수 없음 ]
        new AlertDialog.Builder(this).setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (pw.getText().toString().equals(password)){

                    Toast.makeText(MainActivity.this, "관리자 설정으로 이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SettingMenuActivity.class);
                    startActivity(intent);

                }else{ Toast.makeText(MainActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show(); }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();


    }

    // 외부저장소 권한요청
    void checkPermission(){
        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions, 100);
        }
    }

    // 등록한 메뉴 모두 보여주기
    void clickedListMenu(){

    }

    public void showDialog(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


}