package kr.co.kiosk.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerMenuAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeBinding;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;

public class CoffeeFragment extends Fragment {

    FragmentCoffeeBinding binding;
    RecyclerMenuAdapter menuAdapter;

    ArrayList<Menu> menuItems= new ArrayList<>();

    MenuDBHelper dbHelper;

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
        binding.recyclerMenu.setAdapter(menuAdapter);

        dbHelper = new MenuDBHelper(getActivity());

        for (int i=0; i<20; i++){
            menuItems.add(new Menu("에스프레소","3,000", R.drawable.coffee_01));
            menuItems.add(new Menu("커피","4,000", R.drawable.coffee_02));
        }


        ClickedMenu(); // 카페 메뉴아이템마다 클릭할 때 동작
        clickedListMenu();
    }

    void ClickedMenu(){
        menuAdapter.setItemClickListener(new RecyclerMenuAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(View view, int position) {

                Toast.makeText(getActivity(), menuItems.get(position).menuName, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder dialog= new AlertDialog.Builder(getActivity());
                dialog.setView(R.layout.recycler_menu_item);
                dialog.setTitle(menuItems.get(position).menuName);
                dialog.setMessage("진한 향기가 나는 에스프레소입니다.");
                dialog.show();
            }
        });
    }



    // 등록한 메뉴 모두 보여주기
    void clickedListMenu(){
        Cursor cursor= dbHelper.getDataAll();

        if (cursor.getCount() == 0) showDialog("error", "데이터가 없습니다.");
        else{

            StringBuffer buffer= new StringBuffer();
            while (cursor.moveToNext()){
                buffer.append("id : " + cursor.getString(0)+"\n");
                buffer.append("name : " + cursor.getString(1)+"\n");
                buffer.append("price : " + cursor.getString(2)+"\n");
                buffer.append("image : " + cursor.getString(3)+"\n\n");
            }
            showDialog("메뉴", buffer.toString());
        }
    }

    public void showDialog(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
