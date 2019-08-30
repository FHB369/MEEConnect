package com.fhb.meeconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Faisal Haque Bappy on 30-Aug-19.
 */
public class PeopleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Student> students;
    private ArrayList<Faculty> faculties;
    private Context context;
    private String catagoryName;

    public PeopleRecyclerAdapter(String catagoryName, ArrayList<Student> students, ArrayList<Faculty> faculties, Context context) {
        this.catagoryName = catagoryName;
        this.students = students;
        this.faculties = faculties;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_people_list, parent, false);

        return new VHItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String name = "", subtitle= "", photoURL= "", phone= "";
        if(faculties==null && students!=null){
            name = students.get(position).getName();
            subtitle = students.get(position).getRegistrationNo();
            photoURL = students.get(position).getPhotoURL();
            phone = students.get(position).getPhone();
        }else if(students==null && faculties!=null){
            name = faculties.get(position).getName();
            subtitle = faculties.get(position).getDesignation();
            photoURL = faculties.get(position).getPhotoURL();
            phone = faculties.get(position).getPhone();
        }

        final VHItem vhItem = (VHItem) holder;

        vhItem.name.setText(name);
        vhItem.subTitle.setText(subtitle);
        Picasso.get()
                .load(photoURL)
                .transform(new CircleTransform())
                .fit()
                .centerCrop()
                .into(vhItem.photo);
    }

    @Override
    public int getItemCount() {
        if(faculties==null && students!=null){
            return students.size();
        }else if(students==null && faculties!=null){
            return faculties.size();
        }else {
            return 0;
        }
    }

    class VHItem extends RecyclerView.ViewHolder{
        ImageView photo, phone;
        TextView name, subTitle;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.people_name);
            this.subTitle = itemView.findViewById(R.id.people_subtitle);
            this.phone = itemView.findViewById(R.id.people_phone);
            this.photo = itemView.findViewById(R.id.people_photo);
        }
    }
}
