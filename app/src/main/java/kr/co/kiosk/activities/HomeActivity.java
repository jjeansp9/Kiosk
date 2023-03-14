package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.ActivityHomeBinding;
import kr.co.kiosk.fragments.CoffeeFragment;
import kr.co.kiosk.fragments.DessertFragment;
import kr.co.kiosk.fragments.DrinkFragment;
import kr.co.kiosk.fragments.MilkTeaFragment;
import kr.co.kiosk.fragments.ParfaitFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    Boolean[] result= {false,false,false,false,false};

    // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수
    int categoryNum;

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryNum= getIntent().getIntExtra("category", categoryNum);

        // 홈화면 버튼 클릭시 메인화면으로 이동
        binding.home.setOnClickListener(v-> clickedHome());
        createBNV();

    }

    void clickedHome(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void createBNV(){

        // 5개의 카테고리 fragment 생성
        fragments.add(0, new CoffeeFragment());
        fragments.add(1, new ParfaitFragment());
        fragments.add(2, new MilkTeaFragment());
        fragments.add(3, new DessertFragment());
        fragments.add(4, new DrinkFragment());

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragments.get(categoryNum)).commit();
        result[categoryNum] = true;

        binding.tabCoffee.setOnClickListener(v -> clickedCoffee());
        binding.tabParfait.setOnClickListener(v -> clickedParfait());
        binding.tabMilkTea.setOnClickListener(v -> clickedMilkTea());
        binding.tabDessert.setOnClickListener(v -> clickedDessert());
        binding.tabDrink.setOnClickListener(v -> clickedDrink());
        binding.buy.setOnClickListener(v-> clickedBuy());
    }

    void clickedCoffee(){

        FragmentTransaction tran= fragmentManager.beginTransaction();

        Toast.makeText(this, "click Coffee", Toast.LENGTH_SHORT).show();
        if (!result[0]){
            tran.add(R.id.fragment_container, fragments.get(0));
            result[0] = true;
        }
        for(int i=0; i<fragments.size(); i++){if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }}
        tran.show(fragments.get(0)).commit();
    }

    void clickedParfait(){
        FragmentTransaction tran= fragmentManager.beginTransaction();

        Toast.makeText(this, "click Parfait", Toast.LENGTH_SHORT).show();
        if (!result[1]){
            tran.add(R.id.fragment_container, fragments.get(1));
            result[1] = true;
        }
        for(int i=0; i<fragments.size(); i++){if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }}
        tran.show(fragments.get(1)).commit();
    }

    void clickedMilkTea(){
        FragmentTransaction tran= fragmentManager.beginTransaction();

        Toast.makeText(this, "click Coffee", Toast.LENGTH_SHORT).show();
        if (!result[2]){
            tran.add(R.id.fragment_container, fragments.get(2));
            result[2] = true;
        }
        for(int i=0; i<fragments.size(); i++){if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }}
        tran.show(fragments.get(2)).commit();
    }

    void clickedDessert(){
        FragmentTransaction tran= fragmentManager.beginTransaction();

        Toast.makeText(this, "click Dessert", Toast.LENGTH_SHORT).show();
        if (!result[3]){
            tran.add(R.id.fragment_container, fragments.get(3));
            result[3] = true;
        }
        for(int i=0; i<fragments.size(); i++){if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }}
        tran.show(fragments.get(3)).commit();
    }

    void clickedDrink(){
        FragmentTransaction tran= fragmentManager.beginTransaction();

        Toast.makeText(this, "click drink", Toast.LENGTH_SHORT).show();
        if (!result[4]){
            tran.add(R.id.fragment_container, fragments.get(4));
            result[4] = true;
        }
        for(int i=0; i<fragments.size(); i++){if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }}
        tran.show(fragments.get(4)).commit();
    }

    void clickedBuy(){
        Intent intent= new Intent(HomeActivity.this, MyOrderActivity.class);
        startActivity(intent);
    }
}






























