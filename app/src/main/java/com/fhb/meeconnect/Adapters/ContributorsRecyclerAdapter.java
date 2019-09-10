package com.fhb.meeconnect.Adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fhb.meeconnect.R;

import java.util.ArrayList;

/**
 * Created by Faisal Haque Bappy on 10-Sep-19.
 */
public class ContributorsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Pair<String, String>> contributors;

    public ContributorsRecyclerAdapter(ArrayList<Pair<String, String>> contributors){
        this.contributors = contributors;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contributor_list, parent, false);

        return new ContributorsRecyclerAdapter.VHItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name, batch;

        name = contributors.get(position).first;
        batch = contributors.get(position).second;

        ContributorsRecyclerAdapter.VHItem vhItem = (ContributorsRecyclerAdapter.VHItem) holder;

        vhItem.name.setText(name);
        vhItem.batch.setText(batch);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    class VHItem extends RecyclerView.ViewHolder{
        TextView name, batch;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.contributor_name);
            this.batch = itemView.findViewById(R.id.contributor_batch);
        }
    }
}
