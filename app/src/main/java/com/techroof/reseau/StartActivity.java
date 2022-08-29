package com.techroof.reseau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.techroof.reseau.Authentication.LoginActivity;
import com.techroof.reseau.Creator.CreatorHomeActivity;
import com.techroof.reseau.Supporter.SupporterHomeActivity;

import io.paperdb.Paper;

public class StartActivity extends AppCompatActivity {

    private Button creatorBtn,supporterBtn;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        supporterBtn=findViewById(R.id.supporter_btn);
        creatorBtn=findViewById(R.id.creator_btn);

        supporterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userType="supporter";

                Paper.book("userType").write("type",userType);

                Intent login=new Intent(StartActivity.this, SupporterHomeActivity.class);
                startActivity(login);

            }
        });

        creatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userType="creator";

                Paper.book("userType").write("type",userType);

                Intent login=new Intent(StartActivity.this, CreatorHomeActivity.class);
                startActivity(login);

            }
        });
    }
}