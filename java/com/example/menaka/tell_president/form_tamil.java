package com.example.menaka.tell_president;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class form_tamil extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    public static final String KEY_DISCRIPTION = "discription";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS= "address";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";

    private EditText editTextDiscription;
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextTelephone;
    private EditText editTextMobile;
    private EditText editTextEmail;

    private Button buttonSave;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_form_tamil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        buttonSave = (Button) findViewById(R.id.save_tamil);


        editTextDiscription = (EditText) findViewById(R.id.editText_disc_tamil);
        editTextName = (EditText) findViewById(R.id.edittxt_name_tamil);
        editTextAddress = (EditText) findViewById(R.id.edittxt_address_tamil);
        editTextTelephone = (EditText) findViewById(R.id.edittxt_tele_tamil);
        editTextMobile = (EditText) findViewById(R.id.edittxt_mobile_tamil);
        editTextEmail = (EditText) findViewById(R.id.edittxt_email_tamil);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        buttonSave.setOnClickListener(this);

        editTextDiscription.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextDiscription.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAddress.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAddress.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextTelephone.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextMobile.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId==EditorInfo.IME_ACTION_GO){
                    validation();
                }
                return false;
            }

        });

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Intent i=new Intent(form_tamil.this,Login.class);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonSave){validation();}

    }
    public void validation(){
        final String email = editTextEmail.getText().toString();

        {
            if (!(ConnectivityReceiver.isConnected())){
                checkConnection();
            }

            if (ConnectivityReceiver.isConnected()) {


                if (editTextDiscription.getText().toString().length() == 0) {
                    editTextDiscription.setError("Discription உள்ளிட்ட");
                    editTextDiscription.requestFocus();
                }
                else if (editTextName.getText().toString().length() == 0) {
                    editTextName.setError("பெயரிட உள்ளிட்ட");
                    editTextName.requestFocus();
                }

                else if (editTextAddress.getText().toString().length() == 0) {
                    editTextAddress.setError("முகவரி தேவை");
                    editTextAddress.requestFocus();
                }
                else if ((editTextTelephone.getText().toString().length() == 0) || (!(editTextTelephone.getText().toString().length() == 10))) {
                    editTextTelephone.setError("தவறான எண்");
                    editTextTelephone.requestFocus();
                }

                else if ((editTextMobile.getText().toString().length() == 0) || (!(editTextMobile.getText().toString().length() == 10))) {
                    editTextMobile.setError("தவறான எண்");
                    editTextMobile.requestFocus();
                }else {
                    if (!isValidEmail(email)) {
                        editTextEmail.setError("தவறான மின்னஞ்சல்");
                        editTextEmail.requestFocus();
                    }}

                if (!((editTextAddress.getText().toString().length() == 0) &&
                        (editTextName.getText().toString().length() == 0) &&
                        (editTextDiscription.getText().toString().length() == 0)) && (
                        (editTextMobile.getText().toString().length() == 10) &&
                                (editTextTelephone.getText().toString().length() == 10) && (isValidEmail(email))
                )) {progress();}
            }
        }}
    public void progress(){
        progressBar = new ProgressDialog(form_tamil.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Processing.....");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        progressBarStatus = 0;
        fileSize = 0;

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 100) {


                    progressBarStatus = downloadFile();


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });
                }

                if (progressBarStatus >= 100) {

                    try {

                        registerUser();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();}


    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void registerUser() throws JSONException {
        final String discription = editTextDiscription.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final int mobile=Integer.parseInt(editTextMobile.getText().toString());
        final int telephone=Integer.parseInt(editTextTelephone.getText().toString());
        final String email = editTextEmail.getText().toString().trim();




        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Intent i = new Intent(form_tamil.this, result.class);
                        i.putExtra("id",response);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(form_tamil.this,"No Connection",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_DISCRIPTION, discription);
                params.put(KEY_NAME, name);
                params.put(KEY_ADDRESS, address);
                params.put(KEY_TELEPHONE, String.valueOf(telephone));
                params.put(KEY_MOBILE, String.valueOf(mobile));
                params.put(KEY_EMAIL, email);
                return params;
            }


        } ;

        AppController.getInstance(form_tamil.this).addTorequestque(stringRequest);

    }
    public int downloadFile() {
        while (fileSize <= 1000000) {
            fileSize++;

            if (fileSize == 100000) {
                return 10;
            } else if (fileSize == 200000) {
                return 20;
            } else if (fileSize == 300000) {
                return 30;
            } else if (fileSize == 400000) {
                return 40;
            } else if (fileSize == 500000) {
                return 50;
            } else if (fileSize == 700000) {
                return 70;
            } else if (fileSize == 800000) {
                return 80;
            }
        }
        return 100;
    }
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
            buttonSave.setEnabled(true);
            editTextDiscription.setEnabled(true);
            editTextName.setEnabled(true);
            editTextAddress.setEnabled(true);
            editTextTelephone.setEnabled(true);
            editTextMobile.setEnabled(true);
            editTextEmail.setEnabled(true);

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
            buttonSave.setEnabled(false);
            editTextDiscription.setEnabled(false);
            editTextName.setEnabled(false);
            editTextAddress.setEnabled(false);
            editTextTelephone.setEnabled(false);
            editTextMobile.setEnabled(false);
            editTextEmail.setEnabled(false);


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