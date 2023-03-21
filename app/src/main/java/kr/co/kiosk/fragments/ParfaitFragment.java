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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import kr.co.kiosk.R;
import kr.co.kiosk.activities.HomeActivity;
import kr.co.kiosk.adapters.RecyclerMenuAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeBinding;
import kr.co.kiosk.databinding.FragmentParfaitBinding;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.Price;
import kr.co.kiosk.model.PriceCategory;

public class ParfaitFragment extends Fragment {

    FragmentParfaitBinding binding;
    RecyclerMenuAdapter menuAdapter;

    public static ArrayList<Menu> menuItems= new ArrayList<>();
    ArrayList<Menu> menuInfo= new ArrayList<>();
    PriceCategory priceCategory= new PriceCategory();

    MenuDBHelper dbHelper;

    public static int oneTouch=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentParfaitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuAdapter= new RecyclerMenuAdapter(getActivity(), menuItems);
        binding.recyclerMenuParfait.setAdapter(menuAdapter);

        dbHelper= new MenuDBHelper(getActivity(), 1);

        // 오른쪽 화살표 클릭시 오른쪽으로 스크롤
        binding.right.setOnClickListener(v->binding.recyclerMenuParfait.smoothScrollToPosition(menuItems.size()));

        clickedMenu(); // 카페 메뉴아이템마다 클릭할 때 동작
        clickedListMenu();
    }

    void clickedMenu(){
        menuAdapter.setItemClickListener(new RecyclerMenuAdapter.OnItemClickListener() {

            int num=1;

            // 메뉴 이미지를 클릭했을 때 반응
            @Override
            public void onImageClick(View view, int position) {
                Log.d("Home", oneTouch+"");

                if (oneTouch ==0){
                    for (int i=0; i<menuItems.size(); i++){
                        HomeActivity.selectList.get(1).add(false);
                        Log.d("menuItems", menuItems.size()+", "+HomeActivity.selectList.get(1).size());
                    }
                    oneTouch= 1;
                }

                if (!HomeActivity.selectList.get(1).get(position)){
                    HomeActivity.priceListItems.add(new Price(menuItems.get(position).menuName, num+"", menuItems.get(position).menuPrice, R.drawable.plus, R.drawable.minus));
                    HomeActivity.selectList.get(1).set(position, true);

                    HomeActivity.binding.resultPrice.setText(resultPrice());
                }
                HomeActivity.num= new int[HomeActivity.priceListItems.size()];

                for (int j=0; j<HomeActivity.num.length; j++){
                    HomeActivity.num[j]= 1;
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
            buffer.append("name : " + cursor.getString(1)+"\n");
            buffer.append("price : " + cursor.getString(2)+"\n");
            buffer.append("image : " + cursor.getString(3)+"\n\n");
            buffer.append("info : " + cursor.getString(4)+"\n\n");

            menuItems.add(new Menu(cursor.getString(1), cursor.getString(2), cursor.getString(3), R.drawable.ic_baseline_info_24));
            menuInfo.add(new Menu(cursor.getString(1), cursor.getString(4), cursor.getString(3), R.drawable.ic_baseline_info_24));
        }
        for (int i=0; i<=menuItems.size(); i++){
            priceCategory.parfait.add(i, 0);
        }
    } // clickedListMenu()



}
