package kr.co.kiosk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import kr.co.kiosk.R;
import kr.co.kiosk.databinding.RecyclerMenuItemBinding;
import kr.co.kiosk.model.Menu;

public class RecyclerMenuAdapter extends RecyclerView.Adapter<RecyclerMenuAdapter.VH> {

    Context context;
    ArrayList<Menu> items;

    public RecyclerMenuAdapter(Context context, ArrayList<Menu> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnItemClickListener{
        void onImageClick(View view, int position);
        void onInfoClick(View view, int position);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener){
        this.itemClickListener= onItemClickListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.recycler_menu_item, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        Menu item= items.get(position);

        holder.binding.name.setText(item.menuName);
        holder.binding.price.setText(item.menuPrice);
        Glide.with(context).load(item.menuImage).into(holder.binding.image);
        Glide.with(context).load(item.menuInfo).into(holder.binding.info);

        holder.binding.box.setOnClickListener(view -> itemClickListener.onImageClick(holder.binding.box,position));
        holder.binding.info.setOnClickListener(view -> itemClickListener.onInfoClick(holder.binding.info,position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class VH extends RecyclerView.ViewHolder{

        // recycler_menu_item.xml 바인딩
        RecyclerMenuItemBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);

            binding= RecyclerMenuItemBinding.bind(itemView);
        }
    }
}



