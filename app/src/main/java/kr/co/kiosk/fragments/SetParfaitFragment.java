package kr.co.kiosk.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerSetMenuListAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeSetBinding;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.SetMenuList;

public class SetParfaitFragment extends Fragment {

    private FragmentCoffeeSetBinding binding;
    private RecyclerSetMenuListAdapter adapter;

    private ArrayList<SetMenuList> items= new ArrayList<>();

    MenuDBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= FragmentCoffeeSetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper= new MenuDBHelper(getActivity());

        clickedListMenu();

        adapter= new RecyclerSetMenuListAdapter(getActivity(), items);
        binding.recyclerCoffee.setAdapter(adapter);
        clickedItems();

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

            if(cursor.getString(1).equals("파르페")){
                items.add(new SetMenuList(cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), R.drawable.ic_baseline_cancel_24));
            }
        }
    } // clickedListMenu()


    private void clickedItems(){
        adapter.setItemClickListener(new RecyclerSetMenuListAdapter.OnItemClickListener() {

            // 아이템 클릭시 수정
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getActivity(), items.get(position).setMenuName+" 메뉴 수정", Toast.LENGTH_SHORT).show();
            }

            // X 아이콘 클릭시 아이템삭제
            @Override
            public void onDelete(View view, int position) {
                Toast.makeText(getActivity(), items.get(position).setMenuName+" 메뉴 삭제", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
