package com.fhb.meeconnect.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fhb.meeconnect.CircleTransform;
import com.fhb.meeconnect.R;
import com.fhb.meeconnect.Activities.SearchDetails;
import com.fhb.meeconnect.DataElements.Student;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;

/**
 * Created by Faisal Haque Bappy on 09-Sep-19.
 */

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Student> students;
    private Context context;

    public SearchRecyclerAdapter(ArrayList<Student> students, Context context) {
        this.students = students;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_people_list, parent, false);

        return new SearchRecyclerAdapter.VHItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        String name = "", subtitle = "", photoURL = "", phone = "";

        final SearchRecyclerAdapter.VHItem vhItem = (SearchRecyclerAdapter.VHItem) holder;


            if(students.get(position).getRegistrationNo().startsWith("2")) {
                subtitle = students.get(position).getRegistrationNo();
                name = students.get(position).getName();
            }else{
                subtitle = students.get(position).getName();
                name = students.get(position).getNickname();
            }
            photoURL = students.get(position).getPhotoURL();
            phone = students.get(position).getPhone();
            vhItem.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchDetails.class);
                    intent.putExtra("name", students.get(position).getName());
                    intent.putExtra("nickname", students.get(position).getNickname());
                    intent.putExtra("reg", students.get(position).getRegistrationNo());
                    intent.putExtra("blood", students.get(position).getBloodGroup());
                    intent.putExtra("birthday", students.get(position).getBirthday());
                    intent.putExtra("messenger", students.get(position).getMessengerID());
                    intent.putExtra("phone", students.get(position).getPhone());
                    intent.putExtra("photo", students.get(position).getPhotoURL());
                    context.startActivity(intent);
                    Activity activity = (Activity) context;
                    activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });

        vhItem.name.setText(name);
        vhItem.subTitle.setText(subtitle);

        final String finalPhone = phone;
        vhItem.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + finalPhone));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions((AppCompatActivity) context,
                                new String[]{Manifest.permission.CALL_PHONE}, 1000);
                    }
                }
                context.startActivity(intent);
            }
        });


        final String finalPhotoURL = photoURL;
        Picasso.get()
                .load(photoURL)
                .transform(new CircleTransform())
                .placeholder(R.drawable.ic_login_icon)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(vhItem.photo, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(finalPhotoURL)
                                .transform(new CircleTransform())
                                .placeholder(R.drawable.ic_login_icon)
                                .fit()
                                .centerCrop()
                                .into(vhItem.photo);
                    }
                });
    }





    @Override
    public int getItemCount() {
        return students.size();
    }

    class VHItem extends RecyclerView.ViewHolder{
        ImageView photo, phone;
        TextView name, subTitle;
        CardView card;

        public VHItem(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.people_name);
            this.subTitle = itemView.findViewById(R.id.people_subtitle);
            this.phone = itemView.findViewById(R.id.people_phone);
            this.photo = itemView.findViewById(R.id.people_photo);
            this.card = itemView.findViewById(R.id.people_card);
        }
    }
}
