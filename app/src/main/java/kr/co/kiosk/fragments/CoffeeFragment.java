package kr.co.kiosk.fragments;

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

public class CoffeeFragment extends Fragment {

    FragmentCoffeeBinding binding;
    RecyclerMenuAdapter menuAdapter;

    ArrayList<Menu> menuItems= new ArrayList<>();

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

        for (int i=0; i<20; i++){
            menuItems.add(new Menu("에스프레소","3,000", R.drawable.coffee_01));
            menuItems.add(new Menu("커피","4,000", R.drawable.coffee_02));
        }

        // 카페 메뉴아이템마다 클릭할 때 동작
        ClickedMenu();
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
}
