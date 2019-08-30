package com.fhb.meeconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Faisal Haque Bappy on 30-Aug-19.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    ArrayList<Catagory> list;
    Context context;

    public HomeRecyclerAdapter(Context context, ArrayList<Catagory> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_catagory_header, parent, false);
            return new VHHeader(v);
        }
        else if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_catagory_card, parent, false);
            return new VHItem(v);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof VHHeader){

        }else if(holder instanceof VHItem){
            String current = list.get(position-1).getCatagoryName();
            final VHItem vhItem = (VHItem) holder;
            vhItem.title.setText(current);

            Picasso.get()
                    .load(list.get(position-1).getCoverImageURL())
                    .fit()
                    .centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(vhItem.cover, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(list.get(position-1).getCoverImageURL())
                                    .fit()
                                    .centerCrop()
                                    .into(vhItem.cover);
                        }
                    });

            vhItem.catagoryCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PeopleList.class);
                    intent.putExtra("Catagory Name", list.get(position-1).getCatagoryName());
                    intent.putExtra("Catagory Index", position-1);

                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class VHItem extends RecyclerView.ViewHolder{
        TextView title;
        ImageView cover;
        CardView catagoryCard;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.title);
            this.cover = itemView.findViewById(R.id.cover);
            this.catagoryCard = itemView.findViewById(R.id.catagory_card);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder{

        public VHHeader(@NonNull View itemView) {
            super(itemView);
        }
    }
}
