package zhangle.example.com.campaign;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    private List<Store> itemList = new ArrayList<>();

    public List<Store> getItemList() {return itemList; }

    public ItemAdapter(List<Store> itemList) {
        this.itemList = itemList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView storename, campcount;

        public MyViewHolder(View view) {
            super(view);
            storename = (TextView) view.findViewById(R.id.storeName);
            //storename.setBackgroundResource(R.color.colorPrimary);
            campcount = (TextView) view.findViewById(R.id.campCount);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Store store = itemList.get(position);
        holder.storename.setText(store.getStorename());
        holder.campcount.setText(store.getCampaignno()+" Campaigns");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}