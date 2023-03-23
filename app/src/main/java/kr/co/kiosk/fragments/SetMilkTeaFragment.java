package kr.co.kiosk.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerSetMenuListAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeSetBinding;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.SetMenuList;

public class SetMilkTeaFragment extends Fragment {

    private FragmentCoffeeSetBinding binding;
    private RecyclerSetMenuListAdapter adapter;

    private ArrayList<SetMenuList> items= new ArrayList<>();

    AlertDialog.Builder builder;

    MenuDBHelper dbHelper;
    ImageView etImage;

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

            if(cursor.getString(1).equals("밀크티")){
                items.add(new SetMenuList(cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), R.drawable.ic_baseline_cancel_24));
            }
        }
    } // clickedListMenu()


    private void clickedItems(){
        adapter.setItemClickListener(new RecyclerSetMenuListAdapter.OnItemClickListener() {

            // 아이템 클릭시 수정
            @Override
            public void onItemClick(View view, int position) {
                showDialogForUpdate(
                        items.get(position).setMenuImage,
                        items.get(position).setMenuName,
                        items.get(position).setMenuPrice,
                        items.get(position).setMenuInfo,
                        position
                );
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


    // 등록한 메뉴 정보 수정
    public void showDialogForUpdate(String getImage, String getName, String getPrice, String getInfo, int position){

        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout updateLayout= (RelativeLayout) vi.inflate(R.layout.dialog_menu_update, null);

        EditText etName= updateLayout.findViewById(R.id.et_update_name);
        etImage= updateLayout.findViewById(R.id.update_image);
        EditText etPrice= updateLayout.findViewById(R.id.et_update_price);
        EditText etInfo= updateLayout.findViewById(R.id.et_update_info);

        etImage.setOnClickListener(v -> clickedImageSelect()); // 사진 파일 접근

        Glide.with(getActivity()).load(getImage).into(etImage);
        etName.setText(getName);
        etPrice.setText(getPrice);
        etInfo.setText(getInfo);


        builder = new AlertDialog
                .Builder(getActivity())
                .setCancelable(false)
                .setView(updateLayout);

        // 메뉴 수정
        builder.setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // 메뉴 수정취소
        builder.setNegativeButton("수정취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(getActivity(), name+" 메뉴 수정을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                Log.d("uris", uri+"");
                String image= String.valueOf(uri);

            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean wantToCloseDialog = false;

                String name= etName.getText().toString();
                String image= String.valueOf(uri);
                String price= etPrice.getText().toString();
                String info= etInfo.getText().toString();

                if (uri==null){
                    Toast.makeText(getActivity(), "사진을 추가해주세요", Toast.LENGTH_SHORT).show();

                }else if(name.replace(" ", "").equals("")){
                    Toast.makeText(getActivity(), "메뉴이름을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(price.replace(" ", "").equals("")){
                    Toast.makeText(getActivity(), "메뉴가격을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(info.replace(" ", "").equals("")){
                    Toast.makeText(getActivity(), "메뉴정보를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else{
                    price= price.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

                    dbHelper.updateData("커피", name, price, image, info);
                    items.set(position, new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));

                    adapter.notifyDataSetChanged();
                    binding.recyclerCoffee.setAdapter(adapter);
                    binding.recyclerCoffee.smoothScrollToPosition(position);

                    Toast.makeText(getActivity(), name+" 메뉴를 수정하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("Images", image);
                    wantToCloseDialog= true;
                }

                if(wantToCloseDialog)
                    dialog.dismiss();


            }
        });
    }

    private ImageView image(){
        LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout updateLayout= (RelativeLayout) vi.inflate(R.layout.dialog_menu_update, null);

        ImageView etImage= updateLayout.findViewById(R.id.update_image);
        return etImage;
    }

    Uri uri;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        uri = result.getData().getData();

                        Glide.with(getActivity()).load(uri).into(image());
                        Log.d("ImgURI", uri+"");
                    }
                }
            });

    // 사진업로드 관련
    private void clickedImageSelect(){

        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityResult.launch(intent);

        Glide.with(getActivity()).load(uri).into(etImage);
    }
}
