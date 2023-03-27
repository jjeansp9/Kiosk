package kr.co.kiosk.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.activities.MenuListActivity;
import kr.co.kiosk.adapters.RecyclerSetMenuListAdapter;
import kr.co.kiosk.databinding.FragmentCoffeeSetBinding;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.SetMenuList;

public class SetCoffeeFragment extends Fragment {

    public static FragmentCoffeeSetBinding binding;
    public static RecyclerSetMenuListAdapter adapter;

    public static ArrayList<SetMenuList> items= new ArrayList<>();

    AlertDialog.Builder builder;

    MenuDBHelper dbHelper;
    ImageView etImage;
    EditText etPrice;

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

        adapter= new RecyclerSetMenuListAdapter(getActivity(), items);
        binding.recyclerCoffee.setAdapter(adapter);
        clickedItems();



        clickedListMenu();
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
                items.add(new SetMenuList(cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), R.drawable.ic_baseline_cancel_24));
            }
        }


    } // clickedListMenu()

    private void clickedItems(){
        adapter.setItemClickListener(new RecyclerSetMenuListAdapter.OnItemClickListener() {

            // 아이템 클릭시 수정기능이 있는 다이얼로그 띄움
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

            // X 아이콘 클릭시 아이템삭제기능이 있는 다이얼로그 띄움
            @Override
            public void onDelete(View view, int position) {
                showDialogForDelete(items.get(position).setMenuName, position);
            }
        });
    }

    // 등록되어있는 메뉴 삭제
    public void showDialogForDelete(String menuName, int position){
        builder = new AlertDialog
                .Builder(getActivity())
                .setCancelable(true)
                .setTitle(menuName)
                .setMessage("정말로 메뉴를 삭제하시겠습니까?");

        // 메뉴데이터 삭제
        builder.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dbHelper.deleteData(menuName);
                items.remove(position);
                adapter.notifyDataSetChanged();
                binding.recyclerCoffee.smoothScrollToPosition(position);

                Toast.makeText(getActivity(), menuName+"를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 삭제취소
        builder.setNegativeButton("삭제취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), menuName+" 삭제를 취소하였습니다.", Toast.LENGTH_SHORT).show();
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
        etPrice= updateLayout.findViewById(R.id.et_update_price);
        EditText etInfo= updateLayout.findViewById(R.id.et_update_info);

        etImage.setOnClickListener(v -> clickedImageSelect()); // 사진 파일 접근

        Glide.with(getActivity()).load(getImage).into(etImage);
        etName.setText(getName);
        etPrice.setText(getPrice);
        etInfo.setText(getInfo);

        etPrice.addTextChangedListener(commaAddForNumber()); // 가격 천단위 콤마

        builder = new AlertDialog
                .Builder(getActivity())
                .setCancelable(true)
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
                uri=null;
                Toast.makeText(getActivity(), getName+" 메뉴 수정을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog= builder.create();

        dialog.show();

        WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
        params.width= 800;
        params.height= 950;
        dialog.getWindow().setAttributes(params);

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean sameName= false;
                String updateName= "";

                String image= "";

                if (uri==null) image= getImage;
                else image= String.valueOf(uri);

                String name= etName.getText().toString().trim();
                String price= etPrice.getText().toString().trim();
                String info= etInfo.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getActivity(), "메뉴이름을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if (!name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*")) {
                    Toast.makeText(getActivity(), "특수문자를 제외하고 이름을 등록해주세요.", Toast.LENGTH_SHORT).show();

                }else if(price.equals("")){
                    Toast.makeText(getActivity(), "메뉴가격을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else if(info.equals("")){
                    Toast.makeText(getActivity(), "메뉴정보를 입력해주세요", Toast.LENGTH_SHORT).show();

                }else{

                    Cursor cursor= dbHelper.getDataAll();

                    StringBuffer buffer= new StringBuffer();
                    while (cursor.moveToNext()) {

                        buffer.append("id : " + cursor.getString(0) + "\n");
                        buffer.append("category : " + cursor.getString(1) + "\n");
                        buffer.append("name : " + cursor.getString(2) + "\n");

                        Log.e("name","name : " + cursor.getString(2) + ", "+name.replace(" ", "")+ ", id : " + cursor.getString(0));

                        if (cursor.getString(2).equals(name)) {
                            sameName = true;
                        }

                        if (items.get(position).setMenuName.equals(cursor.getString(2))) {
                            updateName= cursor.getString(0);
                            Log.d("updateName", updateName);
                        }
                    }

                    if (!sameName || items.get(position).setMenuName.equals(name)){
                        dbHelper.updateData("커피", name, price, image, info, updateName);
                        items.set(position, new SetMenuList(name, price, image, info, R.drawable.ic_baseline_cancel_24));

                        adapter.notifyDataSetChanged();
                        binding.recyclerCoffee.smoothScrollToPosition(position);

                        Toast.makeText(getActivity(), name+" 메뉴를 수정하였습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("Images", updateName);
                        dialog.dismiss();
                    }else{
                        Toast.makeText(getActivity(), "["+name+"]은(는) 이미 등록한 메뉴입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    Uri uri;

    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if( result.getResultCode() == RESULT_OK && result.getData() != null){

                        uri = result.getData().getData();

                        Glide.with(getActivity()).load(uri).into(etImage);
                        Log.d("ImgURI", uri+"");
                    }
                }
            });

    // 사진업로드 관련
    private void clickedImageSelect(){

        Intent intent= new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityResult.launch(intent);
    }


    // 숫자 천단위에 [,]를 찍기위한 변수
    DecimalFormat myFormatter= new DecimalFormat("###,###");
    String result= "";

    // 메뉴가격 숫자 입력란에 천단위마다 [,]를 표시하기 위한 메소드
    TextWatcher commaAddForNumber(){
        TextWatcher watcher= new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(result)){
                    result= myFormatter.format(Double.parseDouble(s.toString().replaceAll(",","")));
                    etPrice.setText(result);
                    etPrice.setSelection(result.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
        return watcher;
    } // commaAddForNumber()

}






























