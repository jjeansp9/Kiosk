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

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int[] category={0,1,2,3,4};

    String password= "1233";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.category1.setOnClickListener(v-> clickedCoffe());
        binding.category2.setOnClickListener(v-> clickedParfait());
        binding.category3.setOnClickListener(v-> clickedMilkTea());
        binding.category4.setOnClickListener(v-> clickedDessert());
        binding.category5.setOnClickListener(v-> clickedDrink());
        binding.settings.setOnClickListener(v-> clickedSettings());

        checkPermission(); // 외부저장소 권한요청
        //verifyStoragePermissions(this);
    }

    void clickedCoffe(){
        Toast.makeText(this, "커피 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[0]);
        startActivity(intent);
    }

    void clickedParfait(){
        Toast.makeText(this, "파르페 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[1]);
        startActivity(intent);
    }

    void clickedMilkTea(){
        Toast.makeText(this, "밀크티 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[2]);
        startActivity(intent);
    }

    void clickedDessert(){
        Toast.makeText(this, "디저트 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[3]);
        startActivity(intent);
    }

    void clickedDrink(){
        Toast.makeText(this, "음료 메뉴로 이동", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[4]);
        startActivity(intent);
    }

    void clickedSettings(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.dialog_login, null);

        final EditText pw= (EditText) loginLayout.findViewById(R.id.pw);

        // 비밀번호 :1233 [ 잘못 입력한 경우 관리자설정으로 갈 수 없음 ]
        new AlertDialog.Builder(this).setTitle("Login").setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (pw.getText().toString().equals(password)){

                    Toast.makeText(MainActivity.this, "관리자 설정으로 이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SettingMenuActivity.class);
                    startActivity(intent);

                }else{ Toast.makeText(MainActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show(); }
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

//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }

}