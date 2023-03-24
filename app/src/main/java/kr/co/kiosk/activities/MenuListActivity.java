package kr.co.kiosk.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityMenuListBinding;
import kr.co.kiosk.fragments.SetCoffeeFragment;
import kr.co.kiosk.fragments.SetDessertFragment;
import kr.co.kiosk.fragments.SetDrinkFragment;
import kr.co.kiosk.fragments.SetMilkTeaFragment;
import kr.co.kiosk.fragments.SetParfaitFragment;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.SetMenuList;

public class MenuListActivity extends AppCompatActivity {


    ActivityMenuListBinding binding;

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;

    ImageView etImage;
    AlertDialog.Builder builder;
    MenuDBHelper dbHelper;

    Boolean[] result= {false,false,false,false,false}; // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    int categoryNum= 0; // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수
    String categoryName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMenuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper= new MenuDBHelper(this);

        categoryNum= getIntent().getIntExtra("categorys", categoryNum);

        createBNV();
        tabLayout();

        binding.insertMenu.setOnClickListener(v -> insertMenu());
        binding.goBack.setOnClickListener(v -> goBack());
    }

    // 사진업로드 관련
    private void clickedImageSelect(){

        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityResult.launch(intent);

        Glide.with(this).load(uri).into(etImage);
    }

    void insertMenu(){

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout updateLayout= (RelativeLayout) vi.inflate(R.layout.dialog_menu_update, null);

        EditText etName= updateLayout.findViewById(R.id.et_update_name);
        TextView title= updateLayout.findViewById(R.id.tv_title);
        etImage= updateLayout.findViewById(R.id.update_image);
        EditText etPrice= updateLayout.findViewById(R.id.et_update_price);
        EditText etInfo= updateLayout.findViewById(R.id.et_update_info);

        if (categoryNum==0) {
            categoryName= "커피";
            title.setText("커피 메뉴등록");
        }
        else if (categoryNum==1) {
            categoryName= "파르페";
            title.setText("파르페 메뉴등록");
        }
        else if (categoryNum==2) {
            categoryName= "밀크티";
            title.setText("밀크티 메뉴등록");
        }
        else if (categoryNum==3) {
            categoryName= "디저트";
            title.setText("디저트 메뉴등록");
        }
        else if (categoryNum==4) {
            categoryName= "음료";
            title.setText("음료 메뉴등록");
        }

        etName.setHint("등록할 메뉴의 이름을 입력하세요");
        etPrice.setHint("등록할 메뉴의 가격을 입력하세요");
        etInfo.setHint("등록할 메뉴에 대한 소개글을 작성하세요");

        etImage.setOnClickListener(v -> clickedImageSelect()); // 사진 파일 접근

        builder = new AlertDialog
                .Builder(this)
                .setCancelable(false)
                .setView(updateLayout);

        // 메뉴 등록
        builder.setPositiveButton("등록하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // 메뉴 등록취소
        builder.setNegativeButton("등록취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MenuListActivity.this, categoryName+"카테고리의 메뉴 등록을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog= builder.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean wantToCloseDialog = false;

                String image= "";

                if (uri==null) Toast.makeText(MenuListActivity.this, "사진을 등록해주세요.", Toast.LENGTH_SHORT).show();
                else image= String.valueOf(uri);

                String name= etName.getText().toString();
                String price= etPrice.getText().toString();
                String info= etInfo.getText().toString();

                if(name.replace(" ", "").equals("")){
                    Toast.makeText(MenuListActivity.this, "메뉴이름을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(price.replace(" ", "").equals("")){
                    Toast.makeText(MenuListActivity.this, "메뉴가격을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(info.replace(" ", "").equals("")){
                    Toast.makeText(MenuListActivity.this, "메뉴정보를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else{
                    price= price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");


                    if (categoryName.equals("커피")){

                        dbHelper.updateData("커피", name, price, image, info);

                        SetCoffeeFragment.items.add(new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));
                        SetCoffeeFragment.adapter.notifyDataSetChanged();
                        SetCoffeeFragment.binding.recyclerCoffee.setAdapter(SetCoffeeFragment.adapter);

                        Toast.makeText(MenuListActivity.this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", image);

                        wantToCloseDialog= true;
                    }else if (categoryName.equals("파르페")){

                        dbHelper.updateData("파르페", name, price, image, info);

                        SetParfaitFragment.items.add(new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));
                        SetParfaitFragment.adapter.notifyDataSetChanged();
                        SetParfaitFragment.binding.recyclerCoffee.setAdapter(SetParfaitFragment.adapter);

                        Toast.makeText(MenuListActivity.this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", image);

                        wantToCloseDialog= true;
                    }else if (categoryName.equals("밀크티")){

                        dbHelper.insertData("밀크티", name, price, image, info);

                        SetMilkTeaFragment.items.add(new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));
                        SetMilkTeaFragment.adapter.notifyDataSetChanged();
                        SetMilkTeaFragment.binding.recyclerCoffee.setAdapter(SetMilkTeaFragment.adapter);

                        Toast.makeText(MenuListActivity.this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", image);

                        wantToCloseDialog= true;
                    }else if (categoryName.equals("디저트")){

                        dbHelper.insertData("디저트", name, price, image, info);

                        SetDessertFragment.items.add(new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));
                        SetDessertFragment.adapter.notifyDataSetChanged();
                        SetDessertFragment.binding.recyclerCoffee.setAdapter(SetDessertFragment.adapter);

                        Toast.makeText(MenuListActivity.this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", image);

                        wantToCloseDialog= true;
                    }else if (categoryName.equals("음료")){

                        dbHelper.insertData("음료", name, price, image, info);

                        SetDrinkFragment.items.add(new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));
                        SetDrinkFragment.adapter.notifyDataSetChanged();
                        SetDrinkFragment.binding.recyclerCoffee.setAdapter(SetDrinkFragment.adapter);

                        Toast.makeText(MenuListActivity.this, name+" 메뉴를 등록하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", image);

                        wantToCloseDialog= true;
                    }
                }

                if(wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }

    Uri uri;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        uri = result.getData().getData();

                        Glide.with(MenuListActivity.this).load(uri).into(etImage);
                        Log.d("ImgURI", uri+"");
                    }
                }
            });



    void goBack(){
        Intent intent= new Intent(MenuListActivity.this, MainActivity.class);
        intent.putExtra("category_set", categoryNum);
        startActivity(intent);

        Log.d("cates", categoryNum+"");

        finish();
    }

    private void tabLayout(){

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("커피"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("파르페"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("밀크티"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("디저트"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("음료수"));

        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(categoryNum));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().toString().equals("커피")) {
                    categoryNum= 0;
                    clickedFragment(categoryNum);
                }

                else if (tab.getText().toString().equals("파르페")) {
                    categoryNum= 1;
                    clickedFragment(categoryNum);
                }

                else if (tab.getText().toString().equals("밀크티")) {
                    categoryNum= 2;
                    clickedFragment(categoryNum);
                }

                else if (tab.getText().toString().equals("디저트")) {
                    categoryNum= 3;
                    clickedFragment(categoryNum);
                }

                else if (tab.getText().toString().equals("음료수")) {
                    categoryNum= 4;
                    clickedFragment(categoryNum);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    } // tabLayout()

    void createBNV(){

        // 5개의 카테고리 fragment 생성
        fragments.add(0, new SetCoffeeFragment());
        fragments.add(1, new SetParfaitFragment());
        fragments.add(2, new SetMilkTeaFragment());
        fragments.add(3, new SetDessertFragment());
        fragments.add(4, new SetDrinkFragment());

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.tab_fragment_container, fragments.get(categoryNum)).commit();
        result[categoryNum] = true;

        Log.d("fragment", "categoryNum: " + categoryNum + ", fragments.size(): " + fragments.size());
    }

    void clickedFragment(int num){

        FragmentTransaction tran= fragmentManager.beginTransaction();

        if (!result[num]){
            tran.add(R.id.tab_fragment_container, fragments.get(num));
            result[num] = true;
        }
        for (int i=0; i<fragments.size(); i++){
            if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }
        }
        tran.show(fragments.get(num)).commit();

    }




}