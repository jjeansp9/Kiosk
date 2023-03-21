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
import kr.co.kiosk.model.MenuDBHelper;
import kr.co.kiosk.model.Price;
import kr.co.kiosk.model.PriceCategory;

public class HomeActivity extends AppCompatActivity {

    public static ActivityHomeBinding binding;

    public static Context context_home;

    // 이미 선택한 메뉴를 중복선택 하지않게 설정하는 변수
    public static ArrayList<ArrayList<Boolean>> selectList= new ArrayList<>();
    public static ArrayList<Boolean> selectCoffee= new ArrayList<>();
    public static ArrayList<Boolean> selectParfait= new ArrayList<>();
    public static ArrayList<Boolean> selectMilkTea= new ArrayList<>();
    public static ArrayList<Boolean> selectDessert= new ArrayList<>();
    public static ArrayList<Boolean> selectDrink= new ArrayList<>();

    PriceCategory priceCategory= new PriceCategory();

    // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    Boolean[] result= {false,false,false,false,false};

    // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수
    int categoryNum;
    public static int[] num;
    int addValue = 0;

    public static int oneTouch;

    public static RecyclerPriceListAdapter priceListAdapter;
    public static ArrayList<Price> priceListItems= new ArrayList<>();

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;

    MenuDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 이미 선택한 메뉴를 중복선택 하지않게 설정하는 변수
        selectList.add(0, selectCoffee);
        selectList.add(1, selectParfait);
        selectList.add(2, selectMilkTea);
        selectList.add(3, selectDessert);
        selectList.add(4, selectDrink);

        priceListAdapter= new RecyclerPriceListAdapter(this, priceListItems);
        binding.recyclerSelect.setAdapter(priceListAdapter);

        //for (int i=0; i<MenuDBHelper.DATABASE_NAME.length; i++ ) dbHelper.add(new MenuDBHelper(this, i));
        dbHelper= new MenuDBHelper(this);

        categoryNum= getIntent().getIntExtra("category", categoryNum);

        // 홈화면 버튼 클릭시 메인화면으로 이동
        binding.home.setOnClickListener(v-> clickedHome());
        clickedListMenu(categoryNum);
        createBNV();
        context_home= this;

        clickedPlusOrMinus(); // [ + , - ] 버튼 눌렀을때 반응하는 메소드
    }


    // [ + , - ] 버튼 눌렀을때 반응하는 메소드
    void clickedPlusOrMinus(){
        priceListAdapter.setItemClickListener(new RecyclerPriceListAdapter.OnItemClickListener() {

            int tableIndex=-1;

            // 선택한 메뉴 항목에 [+] 버튼을 눌렀을 때
            @Override
            public void onAddClick(View view, int position) {
                try {



                }catch (RuntimeException e){
                    Log.d("exceptions", e.getMessage());
                }
                num[position]+=1;
                Log.d("addTouch", "items.size() : "+priceListItems.size()+ ", position :" +position + ", num[position] : " +num[position]);
                priceListItems.set(position, new Price(priceListItems.get(position).menuName, num[position]+"", addPrice(position , priceListItems.get(position).menuPrice), R.drawable.plus, R.drawable.minus));
                binding.resultPrice.setText(resultPrice());

                priceListAdapter.notifyDataSetChanged();
            }

            // 선택한 메뉴 항목에 [-] 버튼을 눌렀을 때
            @Override
            public void onSubTractClick(View view, int position) {

                if (num[position]>1){
                    num[position]-=1;
                    priceListItems.set(position, new Price(priceListItems.get(position).menuName, num[position]+"", subTractPrice(position , priceListItems.get(position).menuPrice), R.drawable.plus, R.drawable.minus));
                    binding.resultPrice.setText(resultPrice());

                    priceListAdapter.notifyDataSetChanged();

                }else if (priceListItems.get(position).menuNumber.equals("1") && selectList.size() !=0){

                    try {
                        Cursor cursor= dbHelper.getDataAll();

                        StringBuffer buffer= new StringBuffer();

                        while (cursor.moveToNext()){
                            buffer.append("name : " + cursor.getString(0)+"\n");

                            tableIndex++;

                            if (cursor.getString(0).equals("커피")){

                                Log.d("touch", "tableIndex : "+tableIndex+", menuName : " + priceListItems.get(position).menuName + ", sqlDB : " + cursor.getString(1) + ", position : "+ position);

                                //selectList.get().set(tableIndex, false);
                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());

                                priceListAdapter.notifyDataSetChanged();
                                break;
                            }
                            Log.d("touch1", tableIndex+"");
                            tableIndex= -1;
                        }

                    }catch (Exception e){
                        Log.d("exception", e.getMessage());
                    }

                }
            }
        });
    }


    // 메뉴 수량증가 할 때마다 금액 더하기
    private String addPrice(int position, String price){

        String result= "";
        String s= price.replaceAll(",","");


        if (addValue==0){
            addValue= Integer.parseInt(s);
        }


//        if (categoryNum==0) if (priceCategory.coffee.get(position).equals(0)) value= priceCategory.coffee.set(position, Integer.parseInt(s));
//        if (categoryNum==1) if (priceCategory.parfait.get(position).equals(0)) value= priceCategory.parfait.set(position, Integer.parseInt(s));
//        if (categoryNum==2) if (priceCategory.milkTea.get(position).equals(0)) value= priceCategory.milkTea.set(position, Integer.parseInt(s));
//        if (categoryNum==3) if (priceCategory.dessert.get(position).equals(0)) value= priceCategory.dessert.set(position, Integer.parseInt(s));
//        if (categoryNum==4) if (priceCategory.drink.get(position).equals(0)) value= priceCategory.drink.set(position, Integer.parseInt(s));

        result= Integer.parseInt(s)+addValue+"";

        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    // 메뉴 수량감소 할 때마다 금액 빼기
    public String subTractPrice(int position, String price){

        String result= "";
        String s= price.replaceAll(",","");
        int value=0;

        if (categoryNum==0) if (priceCategory.coffee.get(position).equals(0)) value= priceCategory.coffee.set(position, Integer.parseInt(s));
        if (categoryNum==1) if (priceCategory.parfait.get(position).equals(0)) value= priceCategory.parfait.set(position, Integer.parseInt(s));
        if (categoryNum==2) if (priceCategory.milkTea.get(position).equals(0)) value= priceCategory.milkTea.set(position, Integer.parseInt(s));
        if (categoryNum==3) if (priceCategory.dessert.get(position).equals(0)) value= priceCategory.dessert.set(position, Integer.parseInt(s));
        if (categoryNum==4) if (priceCategory.drink.get(position).equals(0)) value= priceCategory.drink.set(position, Integer.parseInt(s));

        result= Integer.parseInt(s)-value+"";

        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }

    public String resultPrice(){

        String result= "";

        int num = 0;
        for (int i=0; i<priceListItems.size(); i++){
            String s= priceListItems.get(i).menuPrice.replaceAll(",","");
            num+= Integer.parseInt(s);
        }
        result= num+"";
        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

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

        clickedListMenu(num); // db에 저장되어있는 데이터를 불러오는 메소드
    }

    // 주문하기 버튼 클릭했을 때
    void clickedBuy(){

        if(priceListItems.size() == 0){
            Toast.makeText(this, "메뉴를 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent= new Intent(HomeActivity.this, MyOrderActivity.class);
            intent.putExtra("select_menu", priceListItems);
            intent.putExtra("price_result", resultPrice());
            startActivity(intent);
        }
    }

    // 취소하기 버튼 클릭했을 때
    void clickedCancel(){

        priceListItems.clear();
        for (int i=0; i<selectList.size(); i++) selectList.get(i).clear();

        selectList.add(0, selectCoffee);
        selectList.add(1, selectParfait);
        selectList.add(2, selectMilkTea);
        selectList.add(3, selectDessert);
        selectList.add(4, selectDrink);

        binding.resultPrice.setText(resultPrice());

        CoffeeFragment.oneTouch= 0;
        ParfaitFragment.oneTouch= 0;
        MilkTeaFragment.oneTouch= 0;
        DessertFragment.oneTouch= 0;
        DrinkFragment.oneTouch= 0;

        Log.d("selectSize", selectList.size()+"" + HomeActivity.oneTouch);

        priceListAdapter.notifyDataSetChanged();
    }

    public int[] clickedListMenu(int category){

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        Log.d("categoryStart", category+"");

        int i=0;

        while (cursor.moveToNext()){
            buffer.append("category : " + cursor.getString(0)+"\n");
            buffer.append("name : " + cursor.getString(1)+"\n");
            buffer.append("price : " + cursor.getString(2)+"\n");
            buffer.append("image : " + cursor.getString(3)+"\n\n");
            buffer.append("info : " + cursor.getString(4)+"\n\n");

            num= new int[i];
            i++;
            Log.d("numValuse", num.length+"");
        }

        for (int j=0; j<num.length; j++){
            num[j]= 1;
        }

//        if (category==0) for (int k=0; k<=num.length; k++) priceCategory.coffee.add(k, 0);
//        if (category==1) for (int k=0; k<=num.length; k++) priceCategory.parfait.add(k, 0);
//        if (category==2) for (int k=0; k<=num.length; k++) priceCategory.milkTea.add(k, 0);
//        if (category==3) for (int k=0; k<=num.length; k++) priceCategory.dessert.add(k, 0);
//        if (category==4) for (int k=0; k<=num.length; k++) priceCategory.drink.add(k, 0);
//        Log.d("parfaite", priceCategory.parfait.size()+"");



        return num;
    } // clickedListMenu()
}






























