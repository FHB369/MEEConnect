package com.fhb.meeconnect.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.fhb.meeconnect.DataElements.Faculty;
import com.fhb.meeconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;

/**
 * Created by Faisal Haque Bappy on 28-Aug-19.
 */
public class FacultySliderAdapter extends PagerAdapter {
    private ArrayList<Faculty> posts;
    private Context context;

    public FacultySliderAdapter(Context context, ArrayList<Faculty> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_slide_student, container, false);

        final ImageView cover = view.findViewById(R.id.box_cover);
        TextView name = view.findViewById(R.id.student_slide_nickname);
        TextView designation = view.findViewById(R.id.student_slide_name);
        TextView email = view.findViewById(R.id.student_slide_reg);
        TextView bloodGroup = view.findViewById(R.id.slide_blood_group);
        TextView birthday = view.findViewById(R.id.slide_birthday);
        Button messenger = view.findViewById(R.id.slide_messenger);
        Button phone = view.findViewById(R.id.slide_phone);

        if(posts.get(position).getMessengerID().trim().equals("NA")){
            LinearLayout layout = view.findViewById(R.id.slide_messenger_container);
            layout.setVisibility(View.GONE);
        }else{
            messenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(posts.get(position).getMessengerID().trim()));
                    if(posts.get(position).getMessengerID().trim().toLowerCase().startsWith("http")) {
                        context.startActivity(intent);
                    }else{
                        context.startActivity(Intent.createChooser(intent, "Choose browser"));
                    }
                }
            });
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions((AppCompatActivity) context,
                                new String[]{Manifest.permission.CALL_PHONE}, 1000);
                    }
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+posts.get(position).getPhone().trim()));
                context.startActivity(intent);
            }
        });

        name.setText(posts.get(position).getName().trim());

        designation.setText(posts.get(position).getDesignation().trim());

        email.setText(posts.get(position).getEmail().trim());

        if(posts.get(position).getBloodGroup().equals("Choose Blood Group...")){
            LinearLayout layout = view.findViewById(R.id.slide_blood_group_container);
            layout.setVisibility(View.GONE);
        }else{
            bloodGroup.setText(posts.get(position).getBloodGroup().trim());
        }

        if(posts.get(position).getBirthday().equals("Day Month")){
            LinearLayout layout = view.findViewById(R.id.slide_birthday_container);
            layout.setVisibility(View.GONE);
        }else{
            String birthDate = posts.get(position).getBirthday().substring(0,2).trim();
            String birthMonth = posts.get(position).getBirthday().substring(2).trim().toUpperCase().substring(0,3);
            birthday.setText(birthDate+"\n"+birthMonth);
        }

        Picasso.get()
                .load(posts.get(position).getPhotoURL())
                .placeholder(R.color.colorAccent)
                .fit()
                .centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(cover, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(posts.get(position).getPhotoURL())
                                .placeholder(R.color.colorAccent)
                                .fit()
                                .centerCrop()
                                .into(cover);
                    }
                });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
