package zhangle.example.com.campaign;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.MyViewHolder> {

    private Context c;
    private List<StoreCampaign> itemList = new ArrayList<>();
    public List<StoreCampaign> getItemList() {return itemList; }
    public CampaignAdapter(Context c,List<StoreCampaign> itemList) {
        this.c = c;
        this.itemList = itemList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView spinimg;

        public MyViewHolder(View view) {
            super(view);
            spinimg = (ImageView) view.findViewById(R.id.imageViewSpinicon);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemcampaignlayout, parent, false);
        return new CampaignAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StoreCampaign campaign = itemList.get(position);
        Picasso.with(c).load(campaign.getImgurl()).into(holder.spinimg);
        //Picasso.with(this ,itemList.get(position).getImgurl(), holder.spinimg);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
