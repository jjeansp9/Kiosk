package kr.co.kiosk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.RecyclerPriceItemBinding;
import kr.co.kiosk.model.Price;

public class RecyclerPriceListAdapter extends RecyclerView.Adapter<RecyclerPriceListAdapter.VH> {

    Context context;
    ArrayList<Price> items;

    public RecyclerPriceListAdapter(Context context, ArrayList<Price> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnItemClickListener{
        void onAddClick(View view, int position);
        void onSubTractClick(View view, int position);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener= onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_price_item, parent, false);
        return new RecyclerPriceListAdapter.VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Price item= items.get(position);

        holder.binding.tvMenuName.setText(item.menuName);
        holder.binding.tvMenuPrice.setText(item.menuPrice);
        holder.binding.tvMenuNumber.setText(item.menuNumber);

        //holder.binding.add.setOnClickListener(view -> itemClickListener.onAddClick(holder.binding.add,position));
        holder.binding.add.setOnClickListener(view ->  itemClickListener.onAddClick(holder.binding.add,position));
        holder.binding.subtract.setOnClickListener(view -> itemClickListener.onSubTractClick(holder.binding.subtract, position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class VH extends RecyclerView.ViewHolder{

        // recycler_menu_item.xml 바인딩
        RecyclerPriceItemBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);

            binding= RecyclerPriceItemBinding.bind(itemView);
        }
    }
}
