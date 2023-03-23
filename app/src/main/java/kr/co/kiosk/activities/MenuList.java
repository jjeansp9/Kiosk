package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerSetMenuListAdapter;
import kr.co.kiosk.databinding.ActivityMenuListBinding;
import kr.co.kiosk.fragments.CoffeeFragment;
import kr.co.kiosk.fragments.DessertFragment;
import kr.co.kiosk.fragments.DrinkFragment;
import kr.co.kiosk.fragments.MilkTeaFragment;
import kr.co.kiosk.fragments.ParfaitFragment;
import kr.co.kiosk.fragments.SetCoffeeFragment;
import kr.co.kiosk.model.SetMenuList;

public class MenuList extends AppCompatActivity {


    ActivityMenuListBinding binding;

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;

    Boolean[] result= {false,false,false,false,false}; // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    int categoryNum= 0; // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMenuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createBNV();
        tabLayout();
        clickedFragment(0);


    }



    private void tabLayout(){

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("커피"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("파르페"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("밀크티"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("디저트"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("음료"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                Log.d("coffe", tab.getText().toString().equals("커피")+"");
                if (tab.getText().toString().equals("커피")) {
                    clickedFragment(0);
                    Log.d("coffe", tab.getText().toString().equals("커피")+"");
                }
                else if (tab.getText().toString().equals("파르페")) clickedFragment(1);
                else if (tab.getText().toString().equals("밀크티")) clickedFragment(2);
                else if (tab.getText().toString().equals("디저트")) clickedFragment(3);
                else if (tab.getText().toString().equals("음료")) clickedFragment(4);

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
//        fragments.add(1, new ParfaitFragment());
//        fragments.add(2, new MilkTeaFragment());
//        fragments.add(3, new DessertFragment());
//        fragments.add(4, new DrinkFragment());

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



//    private void clickedItems(){
//        adapter.setItemClickListener(new RecyclerSetMenuListAdapter.OnItemClickListener() {
//
//            // 아이템 클릭시 수정
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MenuList.this, "아이템 수정", Toast.LENGTH_SHORT).show();
//            }
//
//            // X 아이콘 클릭시 아이템삭제
//            @Override
//            public void onDelete(View view, int position) {
//                Toast.makeText(MenuList.this, "아이템 삭제", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}