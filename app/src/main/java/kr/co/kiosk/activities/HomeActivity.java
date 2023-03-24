package kr.co.kiosk.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

    private ArrayList<String> categorys= new ArrayList<>();

    // 프래그먼트가 이미 add된 경우 또 add하는 상황을 방지하기 위한 변수
    Boolean[] result= {false,false,false,false,false};

    // 클릭한 카테고리에 따라 해당 화면을 보여주기 위한 변수
    int categoryNum;
    public static int[] num;

    Cursor cursor;
    StringBuffer buffer;

    public static int oneTouch;

    public static RecyclerPriceListAdapter priceListAdapter;
    public static ArrayList<Price> priceListItems= new ArrayList<>();

    ArrayList<Fragment> fragments= new ArrayList<>();
    FragmentManager fragmentManager= null;
    private String priceResult="";

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

        firstTabColor();

        // 홈화면 버튼 클릭시 메인화면으로 이동
        binding.home.setOnClickListener(v-> clickedHome());
        clickedListMenu();
        createBNV();
        context_home= this;

        clickedPlusOrMinus(); // [ + , - ] 버튼 눌렀을때 반응하는 메소드
    }

    private void firstTabColor(){
        if (categoryNum==0) binding.tabCoffee.setBackgroundColor(Color.parseColor("#000000"));
        else if (categoryNum==1) binding.tabParfait.setBackgroundColor(Color.parseColor("#000000"));
        else if (categoryNum==2) binding.tabMilkTea.setBackgroundColor(Color.parseColor("#000000"));
        else if (categoryNum==3) binding.tabDessert.setBackgroundColor(Color.parseColor("#000000"));
        else if (categoryNum==4) binding.tabDrink.setBackgroundColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.cancel.setVisibility(View.INVISIBLE);
        clickedListMenu();
        binding.resultPrice.setText(resultPrice());
        priceListAdapter.notifyDataSetChanged();
    }

    // [ + , - ] 버튼 눌렀을때 반응하는 메소드
    void clickedPlusOrMinus(){
        priceListAdapter.setItemClickListener(new RecyclerPriceListAdapter.OnItemClickListener() {

            // 선택한 메뉴 항목에 [+] 버튼을 눌렀을 때
            @Override
            public void onAddClick(View view, int position) {

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

                }else if(priceListItems.size()==1){

                    cursor= dbHelper.getDataAll();

                    buffer= new StringBuffer();

                    int[] i={0,0,0,0,0};

                    while (cursor.moveToNext()){
                        buffer.append("category : " + cursor.getString(1)+"\n");
                        buffer.append("cursorName : " + cursor.getString(2)+"\n");

                        if (cursor.getString(1).equals("커피")){
                            if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                selectList.get(0).set(i[0], false);

                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                binding.cancel.setVisibility(View.INVISIBLE);
                                break;
                            }
                            i[0]++;

                        }else if (cursor.getString(1).equals("파르페")){
                            if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                selectList.get(1).set(i[1], false);

                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                binding.cancel.setVisibility(View.INVISIBLE);
                                break;
                            }
                            i[1]++;

                        }else if (cursor.getString(1).equals("밀크티")){
                            if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                selectList.get(2).set(i[2], false);

                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                binding.cancel.setVisibility(View.INVISIBLE);
                                break;
                            }
                            i[2]++;

                        }else if (cursor.getString(1).equals("디저트")){
                            if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                selectList.get(3).set(i[3], false);

                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                binding.cancel.setVisibility(View.INVISIBLE);
                                break;
                            }
                            i[3]++;

                        }else if (cursor.getString(1).equals("음료")){
                            if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                selectList.get(4).set(i[4], false);

                                priceListItems.remove(position);
                                binding.resultPrice.setText(resultPrice());
                                priceListAdapter.notifyDataSetChanged();
                                binding.cancel.setVisibility(View.INVISIBLE);
                                break;
                            }
                            i[4]++;
                        }

                    }


                }else if (priceListItems.get(position).menuNumber.equals("1")){

                    try {
                        cursor= dbHelper.getDataAll();
                        buffer= new StringBuffer();

                        int[] i={0,0,0,0,0};

                        while (cursor.moveToNext()){

                            buffer.append("category : " + cursor.getString(1)+"\n");
                            buffer.append("cursorName : " + cursor.getString(2)+"\n");
                            buffer.append("price : " + cursor.getString(3)+"\n\n");

                            if (cursor.getString(1).equals("커피")){
                                if (priceListItems.get(position).menuName.equals(cursor.getString(2))){

                                    selectList.get(0).set(i[0], false);
                                    priceListItems.remove(position);

                                    for (int j=0; j<priceListItems.size(); j++) num[position+j]= num[position+j+1];

                                    binding.resultPrice.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[0]++;

                            }
                            if (cursor.getString(1).equals("파르페")){
                                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){

                                    selectList.get(1).set(i[1], false);
                                    priceListItems.remove(position);

                                    for (int j=0; j<priceListItems.size(); j++) num[position+j]= num[position+j+1];

                                    binding.resultPrice.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[1]++;

                            }
                            if (cursor.getString(1).equals("밀크티")){
                                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){

                                    selectList.get(2).set(i[2], false);
                                    priceListItems.remove(position);

                                    for (int j=0; j<priceListItems.size(); j++) num[position+j]= num[position+j+1];

                                    binding.resultPrice.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[2]++;
                            }
                            if (cursor.getString(1).equals("디저트")){
                                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){

                                    selectList.get(3).set(i[3], false);
                                    priceListItems.remove(position);

                                    for (int j=0; j<priceListItems.size(); j++) num[position+j]= num[position+j+1];

                                    binding.resultPrice.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[3]++;
                            }
                            if (cursor.getString(1).equals("음료")){
                                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){

                                    selectList.get(4).set(i[4], false);
                                    priceListItems.remove(position);

                                    if (num[position]==priceListItems.size()){
                                        num[position]= 0;
                                    }else{
                                        for (int j=0; j<priceListItems.size(); j++) num[position+j]= num[position+j+1];
                                    }


                                    binding.resultPrice.setText(resultPrice());
                                    priceListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                i[4]++;
                            }
                        } // while

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
        int value=0;

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        while (cursor.moveToNext()){
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");

            if (cursor.getString(1).equals("커피")){
                Log.d("DBNAME", cursor.getString(2));

                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }

            }else if(cursor.getString(1).equals("파르페")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("밀크티")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("디저트")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("음료")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }
        } // while

        result= Integer.parseInt(s)+value+"";

        // 천 단위마다 [,] 추가
        result= result.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");

        return result;
    }


    // 메뉴 수량감소 할 때마다 금액 빼기
    public String subTractPrice(int position, String price){

        String result= "";
        String s= price.replaceAll(",","");
        int value=0;

        cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        while (cursor.moveToNext()){
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");

            if (cursor.getString(1).equals("커피")){
                Log.d("DBNAME", cursor.getString(2));

                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("파르페")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("밀크티")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("디저트")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }else if(cursor.getString(1).equals("음료")){
                if (cursor.getString(2).equals(priceListItems.get(position).menuName)){
                    value= Integer.parseInt(cursor.getString(3).replaceAll(",", ""));
                }
            }
        } // while

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

        binding.tabCoffee.setOnClickListener(v -> clickedFragment("커피", 0));
        binding.tabParfait.setOnClickListener(v -> clickedFragment("파르페",1));
        binding.tabMilkTea.setOnClickListener(v -> clickedFragment("밀크티",2));
        binding.tabDessert.setOnClickListener(v -> clickedFragment("디저트",3));
        binding.tabDrink.setOnClickListener(v -> clickedFragment("음료",4));
        binding.buy.setOnClickListener(v-> clickedBuy());
        binding.cancel.setOnClickListener(v-> clickedCancel());
    }

    // 클릭한 카테고리 값을 얻어와서 값에 해당하는 카테고리 탭 열기
    void clickedFragment(String category, int num){

        if (category.equals("커피")){binding.tabCoffee.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.tabCoffee.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("파르페")){binding.tabParfait.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.tabParfait.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("밀크티")){binding.tabMilkTea.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.tabMilkTea.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("디저트")){binding.tabDessert.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.tabDessert.setBackgroundColor(Color.parseColor("#8A8A8A"));}

        if (category.equals("음료")){binding.tabDrink.setBackgroundColor(Color.parseColor("#000000"));}
        else{binding.tabDrink.setBackgroundColor(Color.parseColor("#8A8A8A"));}

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
            intent.putExtra("price_result", resultPrice());
            startActivity(intent);
        }
    }

    // 취소하기 버튼 클릭했을 때

    public void clickedCancel(){

        priceListItems.clear();

        for (int i=0; i<selectList.size(); i++) selectList.get(i).clear();

        selectList.clear();

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

        Log.d("selectSize", selectList.size()+"");

        priceListAdapter.notifyDataSetChanged();
        binding.cancel.setVisibility(View.INVISIBLE);
    }

    public int[] clickedListMenu(){

        Cursor cursor= dbHelper.getDataAll();
        StringBuffer buffer= new StringBuffer();

        int i=0;

        while (cursor.moveToNext()){
            buffer.append("id : " + cursor.getString(0)+"\n");
            buffer.append("category : " + cursor.getString(1)+"\n");
            buffer.append("name : " + cursor.getString(2)+"\n");
            buffer.append("price : " + cursor.getString(3)+"\n\n");
            buffer.append("image : " + cursor.getString(4)+"\n\n");
            buffer.append("info : " + cursor.getString(5)+"\n\n");

            categorys.add(cursor.getString(1));
            Log.d("categoryList", "number : " + cursor.getString(0) + ", category : " + categorys.get(i));

            i++;
        }
        num= new int[i];
        Log.d("numValuse", num.length+"");

        for (int j=0; j<num.length; j++){
            num[j]= 1;
        }
        i=0;

        return num;
    } // clickedListMenu()
}






























