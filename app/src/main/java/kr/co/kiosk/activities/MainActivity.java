package kr.co.kiosk.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import kr.co.kiosk.R;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int[] category={0,1,2,3,4};

    String password= "1234";

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 클릭한 버튼에 따라 값을 다르게 줘서 그 값에 맞는 카테고리로 이동
        binding.category1.setOnClickListener(v-> clickedCategory("커피 메뉴로 이동", 0));
        binding.category2.setOnClickListener(v-> clickedCategory("파르페 메뉴로 이동", 1));
        binding.category3.setOnClickListener(v-> clickedCategory("밀크티 메뉴로 이동", 2));
        binding.category4.setOnClickListener(v-> clickedCategory("디저트 메뉴로 이동", 3));
        binding.category5.setOnClickListener(v-> clickedCategory("음료 메뉴로 이동", 4));

        binding.settings.setOnClickListener(v-> clickedSettings()); // 관리자설정 화면으로 이동

        // 디바이스에 저장된 비밀번호 가져오기
        pref= getSharedPreferences("login", MODE_PRIVATE);
        password= pref.getString("password", password);

        checkPermission(); // 외부저장소 권한요청
    }

    // 클릭한 카테고리 값을 얻어와서 값에 해당하는 카테고리로 이동
    private void clickedCategory(String msg, int num){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("category", category[num]);
        startActivity(intent);
    }

    // 관리자설정 메뉴로 이동
    void clickedSettings(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout loginLayout = (LinearLayout) vi.inflate(R.layout.dialog_login, null);

        EditText pw= loginLayout.findViewById(R.id.pw);
        TextView setPw= loginLayout.findViewById(R.id.set_pw);

        // 비밀번호 :1234 [ 잘못 입력한 경우 관리자설정으로 갈 수 없음 ]
        AlertDialog.Builder builder= new AlertDialog.Builder(this).setView(loginLayout).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (pw.getText().toString().equals(password)){

                    Toast.makeText(MainActivity.this, "관리자 설정으로 이동", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, SetMenuListActivity.class);
                    startActivity(intent);

                }else{ Toast.makeText(MainActivity.this, "비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();}
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        // 비밀번호 변경 버튼 누르면 비밀번호 변경 다이얼로그 오픈
        setPw.setOnClickListener(v -> {
            Toast.makeText(this, "비밀번호 변경하기", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            LinearLayout setPwLayout = (LinearLayout) vi.inflate(R.layout.dialog_set_pw, null);

            EditText etCurrentPw= setPwLayout.findViewById(R.id.current_pw);
            EditText etNewPw= setPwLayout.findViewById(R.id.new_pw);
            EditText etNewPwConfirm= setPwLayout.findViewById(R.id.new_pw_confirm);

            AlertDialog.Builder builders= new AlertDialog.Builder(this)
                    .setView(setPwLayout)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String currentPw= etCurrentPw.getText().toString();
                    String newPw= etNewPw.getText().toString();
                    String newPwConfirm= etNewPwConfirm.getText().toString();

                    if (newPw.equals(password) || newPwConfirm.equals(password)){
                        Toast.makeText(MainActivity.this, "이미 설정된 비밀번호입니다.", Toast.LENGTH_SHORT).show();

                    }else if (!newPw.equals(newPwConfirm)){
                        Toast.makeText(MainActivity.this, "새비밀번호와 새비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                    }else if (!currentPw.equals(password)){
                        Toast.makeText(MainActivity.this, "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else if (newPw.equals(newPwConfirm) && currentPw.equals(password) && !newPw.equals(password) && !newPwConfirm.equals(password)){

                        password= newPw;
                        pref = getSharedPreferences("login", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        // 디바이스에 변경된 비밀번호 저장
                        editor.putString("password", password);
                        editor.commit();
                        Toast.makeText(MainActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(MainActivity.this, "비밀번호 변경취소", Toast.LENGTH_SHORT).show();
                }
            });

            AlertDialog dialogs = builders.create();
            dialogs.show();

            WindowManager.LayoutParams params=dialogs.getWindow().getAttributes();
            params.width= 700;
            params.height= 370;
            dialogs.getWindow().setAttributes(params);
        });
        //setPw.setVisibility(View.INVISIBLE);
    }

    // 외부저장소 권한요청
    void checkPermission(){
        String[] permissions= new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED){
            requestPermissions(permissions, 100);
        }
    }
}