package com.fhb.meeconnect.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fhb.meeconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class SearchDetails extends AppCompatActivity {

    private ImageView cover;
    private TextView name, nickname, regNo, bloodGroup, birthday;
    private Button messenger, phone;

    private String NAME, NICKNAME, REG, BLOOD, BIRTHDAY, MESSENGER, PHONE, PHOTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        cover = findViewById(R.id.search_detail_photo);
        name = findViewById(R.id.search_detail_name);
        nickname = findViewById(R.id.search_detail_nickname);
        regNo = findViewById(R.id.search_deatil_reg);
        bloodGroup = findViewById(R.id.search_detail_blood_group);
        birthday = findViewById(R.id.search_detail_birthday);
        messenger = findViewById(R.id.search_detail_messenger);
        phone = findViewById(R.id.search_detail_phone);

        getDataFromIntent();

        setData();
    }

    public void getDataFromIntent(){
        NAME = getIntent().getStringExtra("name");
        NICKNAME = getIntent().getStringExtra("nickname");
        REG = getIntent().getStringExtra("reg");
        BLOOD = getIntent().getStringExtra("blood");
        BIRTHDAY = getIntent().getStringExtra("birthday");
        MESSENGER = getIntent().getStringExtra("messenger");
        PHONE = getIntent().getStringExtra("phone");
        PHOTO  = getIntent().getStringExtra("photo");
    }

    public void setData(){
        name.setText(NAME);
        name.setShadowLayer(40, 0, 2, R.color.black);
        nickname.setText(NICKNAME);
        nickname.setShadowLayer(40, 0, 2, R.color.black);
        regNo.setText(REG);
        if(BLOOD.equals("Choose Blood Group...")){
            LinearLayout layout = findViewById(R.id.search_detail_blood_group_container);
            layout.setVisibility(View.GONE);
        }else{
            bloodGroup.setText(BLOOD);
        }

        if(BIRTHDAY.equals("Day Month")){
            LinearLayout layout = findViewById(R.id.search_detail_birthday_container);
            layout.setVisibility(View.GONE);
        }else{
            String birthDate = BIRTHDAY.substring(0,2).trim();
            String birthMonth = BIRTHDAY.substring(2).trim().toUpperCase().substring(0,3);
            birthday.setText(birthDate+"\n"+birthMonth);
        }

        Picasso.get()
                .load(PHOTO)
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
                                .load(PHOTO)
                                .placeholder(R.color.colorAccent)
                                .fit()
                                .centerCrop()
                                .into(cover);
                    }
                });

        if(MESSENGER.equals("NA")){
            LinearLayout layout = findViewById(R.id.search_detail_messenger_container);
            layout.setVisibility(View.GONE);
        }else{
            messenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(MESSENGER));
                    startActivity(intent);
                }
            });
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+PHONE));
                startActivity(intent);
            }
        });
    }
}
