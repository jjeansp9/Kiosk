package kr.co.kiosk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.RecyclerSetMenuItemBinding;
import kr.co.kiosk.model.SetMenuList;

public class RecyclerSetMenuListAdapter extends RecyclerView.Adapter<RecyclerSetMenuListAdapter.VH>{

    Context context;
    ArrayList<SetMenuList> items;

    public RecyclerSetMenuListAdapter(Context context, ArrayList<SetMenuList> items) {
        this.context = context;
        this.items = items;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onDelete(View view, int position);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener= onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_set_menu_item, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        SetMenuList item= items.get(position);

        holder.binding.setMenuName.setText(item.setMenuName);
        holder.binding.setMenuPrice.setText(item.setMenuPrice);
        holder.binding.setMenuInfo.setText(item.setMenuInfo);

        Glide.with(context).load(item.setMenuImage).into(holder.binding.setMenuImage);
        Glide.with(context).load(item.setMenuDelete).into(holder.binding.setMenuDelete);

        holder.binding.setMenuUpdate.setOnClickListener(v -> itemClickListener.onItemClick(holder.binding.setMenuUpdate, position));
        holder.binding.setMenuImage.setOnClickListener(v -> itemClickListener.onItemClick(holder.binding.setMenuImage, position));
        holder.binding.setMenuDelete.setOnClickListener(v -> itemClickListener.onDelete(holder.binding.setMenuDelete, position));




    }

    @Override
    public int getItemCount() {return items.size();}


    class VH extends RecyclerView.ViewHolder{

        // recycler_set_menu_item.xml 바인딩
        RecyclerSetMenuItemBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);

            binding= RecyclerSetMenuItemBinding.bind(itemView);
        }
    }

}
