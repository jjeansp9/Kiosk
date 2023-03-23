package kr.co.kiosk.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
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
import kr.co.kiosk.adapters.RecyclerSetMenuListAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeSetBinding;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.SetMenuList;

public class SetDessertFragment extends Fragment {

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

            if(cursor.getString(1).equals("디저트")){
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
                showDialog(items.get(position).setMenuName, position);
            }
        });
    }

    public void showDialog(String menu, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(menu);
        builder.setMessage("정말로 메뉴를 삭제하시겠습니까?");

        // 메뉴데이터 삭제
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dbHelper.deleteData(menu);
                items.remove(position);
                adapter.notifyDataSetChanged();
                binding.recyclerCoffee.setAdapter(adapter);
                binding.recyclerCoffee.smoothScrollToPosition(position);

                Toast.makeText(getActivity(), menu+"를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 삭제취소
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "삭제 취소", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }
}
