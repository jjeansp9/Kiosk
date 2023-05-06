package kr.co.kiosk.fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.activities.HomeActivity;
import kr.co.kiosk.adapters.RecyclerMenuAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeBinding;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.Price;
import kr.co.kiosk.model.PriceCategory;

public class CoffeeFragment extends Fragment {

    FragmentCoffeeBinding binding;

    RecyclerMenuAdapter menuAdapter;

    public static ArrayList<Menu> menuItems= new ArrayList<>();
    ArrayList<Menu> menuInfo= new ArrayList<>();

    MenuDBHelper dbHelper;
    public static int oneTouch=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentCoffeeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuAdapter= new RecyclerMenuAdapter(getActivity(), menuItems);
        binding.recyclerMenuCoffee.setAdapter(menuAdapter);

        dbHelper= new MenuDBHelper(getActivity());

        binding.right.setOnClickListener(v->binding.recyclerMenuCoffee.smoothScrollToPosition(menuItems.size())); // 클릭시 [ 오른쪽 ]으로 스크롤
        binding.left.setOnClickListener(v->binding.recyclerMenuCoffee.smoothScrollToPosition(0)); // 클릭시 [ 왼쪽 ]으로 스크롤
        scrollListener();

        clickedMenu(); // 카페 메뉴아이템마다 클릭할 때 동작
    }


    private void scrollListener(){
        binding.recyclerMenuCoffee.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("positions", binding.recyclerMenuCoffee.getWidth()+"");
                Log.d("positions", "extent : "+binding.recyclerMenuCoffee.computeHorizontalScrollExtent()+""); // 화면에 보여지는 길이 [ 리사이클러뷰 컨테이너 사이즈 ]
                Log.d("positions", "offset : "+binding.recyclerMenuCoffee.computeHorizontalScrollOffset()+""); // 어느정도 움직였는지 알려주는 값
                Log.d("positions", "range : "+binding.recyclerMenuCoffee.computeHorizontalScrollRange()+""); // 스크롤 막대가 나타내는 범위

                if (menuItems.size()<5){
                    binding.right.setVisibility(View.GONE);
                    binding.left.setVisibility(View.GONE);
                }else{
                    if (
                            binding.recyclerMenuCoffee.computeHorizontalScrollExtent()+binding.recyclerMenuCoffee.computeHorizontalScrollOffset()
                                    < binding.recyclerMenuCoffee.computeHorizontalScrollRange()-binding.recyclerMenuCoffee.computeHorizontalScrollOffset()
                    ){
                        binding.right.setVisibility(View.VISIBLE);
                        binding.left.setVisibility(View.GONE);
                    }else{

                        binding.right.setVisibility(View.GONE);
                        binding.left.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    @Override
    public void onPause() { // Fragment를 떠나기 시작했지만 Fragment는 여전히 visible 일 때 호출 [ STARTED ] 상태로 호출됨.
        super.onPause();

        menuItems.clear();
        HomeActivity.priceListItems.clear();

        for (int i=0; i<HomeActivity.selectList.size(); i++) HomeActivity.selectList.get(i).clear();

        HomeActivity.selectList.clear();

        HomeActivity.selectList.add(0, HomeActivity.selectCoffee);
        HomeActivity.selectList.add(1, HomeActivity.selectParfait);
        HomeActivity.selectList.add(2, HomeActivity.selectMilkTea);
        HomeActivity.selectList.add(3, HomeActivity.selectDessert);
        HomeActivity.selectList.add(4, HomeActivity.selectDrink);

        CoffeeFragment.oneTouch= 0;
        ParfaitFragment.oneTouch= 0;
        MilkTeaFragment.oneTouch= 0;
        DessertFragment.oneTouch= 0;
        DrinkFragment.oneTouch= 0;
    }

    // 프래그먼트가 사용자와 상호작용할 수 있을 때 onResume()콜백이 호출
    @Override
    public void onResume() {
        super.onResume();
        clickedListMenu();
    }

    void clickedMenu(){
        menuAdapter.setItemClickListener(new RecyclerMenuAdapter.OnItemClickListener() {

            // 메뉴 이미지를 클릭했을 때 반응
            @Override
            public void onImageClick(View view, int position) {
                if (oneTouch ==0){ // 두번클릭할때 같은이름으로 item을 add하지 않기 위한 조건문
                    for (int i=0; i<menuItems.size(); i++){
                        HomeActivity.selectList.get(0).add(false);
                    }
                    oneTouch= 1;
                }

                if (!HomeActivity.selectList.get(0).get(position)){

                    HomeActivity.priceListItems.add(new Price(
                            menuItems.get(position).menuName,
                            HomeActivity.num[position]+"", menuItems.get(position).menuPrice,
                            R.drawable.plus, R.drawable.minus
                        ));

                    HomeActivity.num[HomeActivity.priceListItems.size()-1]= 1;

                    HomeActivity.priceListItems.set(HomeActivity.priceListItems.size()-1, new Price(
                            menuItems.get(position).menuName,
                            HomeActivity.num[HomeActivity.priceListItems.size()-1]+"",
                            menuItems.get(position).menuPrice, R.drawable.plus, R.drawable.minus
                        ));
                    HomeActivity.selectList.get(0).set(position, true);
                    HomeActivity.binding.resultPrice.setText(resultPrice());
                    HomeActivity.binding.cancel.setVisibility(View.VISIBLE);

                }else{

                    for (int i=0; i<HomeActivity.priceListItems.size(); i++){

                        if (HomeActivity.priceListItems.get(i).menuName.equals(menuItems.get(position).menuName)){
                            HomeActivity.num[i]+=1;
                            HomeActivity.priceListItems.set(i, new Price(HomeActivity.priceListItems.get(i).menuName, HomeActivity.num[i]+"", addPrice(i , HomeActivity.priceListItems.get(i).menuPrice), R.drawable.plus, R.drawable.minus));
                        }
                    }
                    HomeActivity.binding.resultPrice.setText(resultPrice());
                }
                HomeActivity.priceListAdapter.notifyDataSetChanged();

            }


            // 메뉴 info 아이콘을 클릭했을 때 반응
            @Override
            public void onInfoClick(View view, int position) {

                Dialog dialog= new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_menu_info);

                // 다이얼로그 사이즈조절
                WindowManager.LayoutParams params= dialog.getWindow().getAttributes();
                params.width= WindowManager.LayoutParams.MATCH_PARENT;
                params.height= WindowManager.LayoutParams.WRAP_CONTENT;

                ImageView image= dialog.findViewById(R.id.menu_img);
                TextView name= dialog.findViewById(R.id.menu_name);
                TextView info= dialog.findViewById(R.id.menu_info);

                // 다이얼로그 텍스트,이미지 설정
                name.setText(menuItems.get(position).menuName);
                info.setText(menuInfo.get(position).menuPrice);
                Glide.with(getActivity()).load(menuItems.get(position).menuImage).into(image);

                dialog.show();
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
                if (cursor.getString(2).equals(HomeActivity.priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }
        } // while

        result= Integer.parseInt(s)+value+"";
        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    private String resultPrice(){
        String result= "";
        int num = 0;
        for (int i=0; i<HomeActivity.priceListItems.size(); i++){
            String s= HomeActivity.priceListItems.get(i).menuPrice.replaceAll(",","");
            num+= Integer.parseInt(s);
        }
        result= num+"";
        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }


    // 등록한 메뉴 모두 보여주기
    private void clickedListMenu(){

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        while (cursor.moveToNext()){
            buffer.append("id : " + cursor.getString(0)+"\n");
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");
            buffer.append("image : " + cursor.getString(4)+"\n\n");
            buffer.append("info : " + cursor.getString(5)+"\n\n");

            if(cursor.getString(1).equals("커피")){
                menuItems.add(new Menu(cursor.getString(2), cursor.getString(3), cursor.getString(4), R.drawable.ic_baseline_info_24));
                menuInfo.add(new Menu(cursor.getString(2), cursor.getString(5), cursor.getString(4), R.drawable.ic_baseline_info_24));
            }
        }
        Log.d("menuitems", menuItems.size()+","+dbHelper.getDatabaseName()+"," +dbHelper.getDataAll().getCount());

    } // clickedListMenu()

}
