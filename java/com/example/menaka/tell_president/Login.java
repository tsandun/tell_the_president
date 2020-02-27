package com.example.menaka.tell_president;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    Button english,sinhala,tamil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sinhala=(Button)findViewById(R.id.button_sinhala);
        tamil=(Button)findViewById(R.id.button_Tamil);
        english = (Button)findViewById(R.id.button_English);

        english.setOnClickListener(this);
        sinhala.setOnClickListener(this);
        tamil.setOnClickListener(this);
        /*checkConnection();*/
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Login.this.finish();
    }


    @Override
    public void onClick(View v) {
        if(v==english){
            Intent i=new Intent(this,form.class);
            startActivity(i);

        }
        if(v==sinhala){
            Intent i=new Intent(this,form_sinhala.class);
            startActivity(i);

        }
        if(v==tamil){
            Intent i=new Intent(this,form_tamil.class);
            startActivity(i);

        }

    }
    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
            Snackbar snackbar =Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor("#009900"));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();


        } else {
            message = "Not connected to internet";
            color = Color.WHITE;
            Snackbar snackbar =Snackbar
                    .make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
