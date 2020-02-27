package com.example.menaka.tell_president;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result_tamil extends AppCompatActivity implements View.OnClickListener {

    Button back_home;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_tamil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        back_home=(Button)findViewById(R.id.back);
        textView=(TextView)findViewById(R.id.txt_tamil);
        back_home.setOnClickListener(this);
        Intent i=getIntent();
        String id=(String) i.getSerializableExtra("id");
        textView.setText(id);

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        result_tamil.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v==back_home){
            Intent i=new Intent(this,Login.class);
            startActivity(i);

        }
    }
}
