package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.adapters.RecyclerPriceListAdapter;
import kr.co.kiosk.databinding.ActivityHomeBinding;
import kr.co.kiosk.fragments.CoffeeFragment;
import kr.co.kiosk.fragments.DessertFragment;
import kr.co.kiosk.fragments.DrinkFragment;
import kr.co.kiosk.fragments.MilkTeaFragment;
import kr.co.kiosk.fragments.ParfaitFragment;
import kr.co.kiosk.model.Menu;
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.Price;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    public static Context context_home;

    public static ArrayList<ArrayList<Boolean>> selectList= new ArrayList<>();

    public ArrayList<Boolean> selectCoffee= new ArrayList<>();
    public ArrayList<Boolean> selectParfait= new ArrayList<>();
    public ArrayList<Boolean> selectMilkTea= new ArrayList<>();
    public ArrayList<Boolean> selectDessert= new ArrayList<>();
    public ArrayList<Boolean> selectDrink= new ArrayList<>();

    // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    Boolean[] result= {false,false,false,false,false};

    // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수
    int categoryNum;
    private int[] num;

    public static int oneTouch=0;

    public static RecyclerPriceListAdapter priceListAdapter;
    public static ArrayList<Price> priceListItems= new ArrayList<>();

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;

    ArrayList<MenuDBHelper> dbHelper= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectList.add(0, selectCoffee);
        selectList.add(1, selectParfait);
        selectList.add(2, selectMilkTea);
        selectList.add(3, selectDessert);
        selectList.add(4, selectDrink);


        priceListAdapter= new RecyclerPriceListAdapter(this, priceListItems);
        binding.recyclerSelect.setAdapter(priceListAdapter);

        dbHelper.add(new MenuDBHelper(this, 0));
        dbHelper.add(new MenuDBHelper(this, 1));
        dbHelper.add(new MenuDBHelper(this, 5));

        categoryNum= getIntent().getIntExtra("category", categoryNum);

        // 홈화면 버튼 클릭시 메인화면으로 이동
        binding.home.setOnClickListener(v-> clickedHome());
        createBNV();

        context_home= this;

        clickedPlusOrMinus(); // [ + , - ] 버튼 눌렀을때 반응하는 메소드
        clickedListMenu(); // db에 저장되어있는 데이터 불러오는 메소드
    }

    // [ + , - ] 버튼 눌렀을때 반응하는 메소드
    void clickedPlusOrMinus(){
        priceListAdapter.setItemClickListener(new RecyclerPriceListAdapter.OnItemClickListener() {

            int tableIndex=-1;


            // 선택한 메뉴 항목에 [+] 버튼을 눌렀을 때
            @Override
            public void onAddClick(View view, int position) {
                try {
                    num[position]+=1;
                    Log.d("addTouch", "items.size() : "+priceListItems.size()+ ", position :" +position + ", num : " +num.length);
                    priceListItems.set(position, new Price(priceListItems.get(position).menuName, num[position]+"", resultPrice(priceListItems.get(position).menuPrice), R.drawable.plus, R.drawable.minus));
                    priceListAdapter.notifyDataSetChanged();
                }catch (RuntimeException e){
                    Log.d("exceptions", e.getMessage());
                }


            }

            // 선택한 메뉴 항목에 [-] 버튼을 눌렀을 때
            @Override
            public void onSubTractClick(View view, int position) {

                if (num[position]>1){
                    num[position]-=1;
                    priceListItems.set(position, new Price(priceListItems.get(position).menuName, num[position]+"", priceListItems.get(position).menuPrice, R.drawable.plus, R.drawable.minus));
                    priceListAdapter.notifyDataSetChanged();

                }else if (priceListItems.get(position).menuNumber.equals("1") && selectList.size() !=0){

                    // 커피
                    Cursor cursor= dbHelper.get(0).getDataAll();
                    StringBuffer buffer= new StringBuffer();

                    while (cursor.moveToNext()){
                        buffer.append("name : " + cursor.getString(1)+"\n");

                        tableIndex++;

                        if (cursor.getString(1).equals(priceListItems.get(position).menuName)){
                            Log.d("touch", "tableIndex : "+tableIndex+", menuName : " + priceListItems.get(position).menuName + ", sqlDB : " + cursor.getString(1) + ", position : "+ position);
                            selectList.get(0).set(tableIndex, false);
                            priceListItems.remove(position);
                            break;
                        }
                        Log.d("touch1", tableIndex+"");
                    }
                    tableIndex= -1;
                    num[position]=0;
                    priceListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private String resultPrice(String price){

        String s= price.replaceAll(",","");

        int add=0;
        int num = Integer.parseInt(s)+add;
        Log.d("values", num+"num");

        if (add==0){
            add= num;
        }
        Log.d("values", add+"add");

        String result= num+"";
        Log.d("values", result+"result");
        return result;
    }

    // 홈버튼 클릭
    void clickedHome(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void createBNV(){

        // 5개의 카테고리 fragment 생성
        fragments.add(0, new CoffeeFragment());
        fragments.add(1, new ParfaitFragment());
        fragments.add(2, new MilkTeaFragment());
        fragments.add(3, new DessertFragment());
        fragments.add(4, new DrinkFragment());

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, fragments.get(categoryNum)).commit();
        result[categoryNum] = true;

        binding.tabCoffee.setOnClickListener(v -> clickedFragment(0));
        binding.tabParfait.setOnClickListener(v -> clickedFragment(1));
        binding.tabMilkTea.setOnClickListener(v -> clickedFragment(2));
        binding.tabDessert.setOnClickListener(v -> clickedFragment(3));
        binding.tabDrink.setOnClickListener(v -> clickedFragment(4));
        binding.buy.setOnClickListener(v-> clickedBuy());
        binding.cancel.setOnClickListener(v-> clickedCancel());
    }

    // 클릭한 카테고리 값을 얻어와서 값에 해당하는 카테고리 탭 열기
    void clickedFragment(int num){
        FragmentTransaction tran= fragmentManager.beginTransaction();

        if (!result[num]){
            tran.add(R.id.fragment_container, fragments.get(num));
            result[num] = true;
        }
        for (int i=0; i<fragments.size(); i++){
            if (fragments.get(i)!=null){ tran.hide(fragments.get(i)); }
        }
        tran.show(fragments.get(num)).commit();
    }

    // 주문하기 버튼 클릭했을 때
    void clickedBuy(){

        if(priceListItems.size() == 0){
            Toast.makeText(this, "메뉴를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent= new Intent(HomeActivity.this, MyOrderActivity.class);
            intent.putExtra("select_menu", priceListItems);
            startActivity(intent);
        }
    }

    // 취소하기 버튼 클릭했을 때
    void clickedCancel(){

        priceListItems.clear();
        selectList.get(0).clear();

        selectList.add(0, selectCoffee);
        selectList.add(1, selectParfait);
        selectList.add(2, selectMilkTea);
        selectList.add(3, selectDessert);
        selectList.add(4, selectDrink);

        HomeActivity.oneTouch= 0;
        Log.d("selectSize", selectList.size()+"" + HomeActivity.oneTouch);

        clickedListMenu();

        priceListAdapter.notifyDataSetChanged();
    }

    int[] clickedListMenu(){

        Cursor cursor= dbHelper.get(0).getDataAll();
        StringBuffer buffer= new StringBuffer();

        int i=0;

        while (cursor.moveToNext()){
            buffer.append("id : " + cursor.getString(0)+"\n");
            buffer.append("name : " + cursor.getString(1)+"\n");
            buffer.append("price : " + cursor.getString(2)+"\n");
            buffer.append("image : " + cursor.getString(3)+"\n\n");
            buffer.append("info : " + cursor.getString(4)+"\n\n");

            num= new int[i];
            i++;
            Log.d("numValuse", num.length+"");
        }

        return num;
    } // clickedListMenu()
}






























