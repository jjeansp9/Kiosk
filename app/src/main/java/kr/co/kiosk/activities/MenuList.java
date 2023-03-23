package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import kr.co.kiosk.model.SetMenuList;

public class MenuList extends AppCompatActivity {


    ActivityMenuListBinding binding;
    private RecyclerSetMenuListAdapter adapter;

    ArrayList<SetMenuList> items= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMenuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tabLayout();



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

                if (tab.getText().toString().equals("커피")){

                }else if (tab.getText().toString().equals("파르페")){

                }else if (tab.getText().toString().equals("밀크티")){

                }else if (tab.getText().toString().equals("디저트")){

                }else if (tab.getText().toString().equals("음료")){

                }

                Log.d("tabs", tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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