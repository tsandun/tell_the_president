package com.example.menaka.tell_president;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

public class form_sinhala extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    public static final String KEY_DISCRIPTION = "discription";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS= "address";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";


    private EditText editTextDiscription1;
    private EditText editTextName1;
    private EditText editTextAddress1;
    private EditText editTextTelephone1;
    private EditText editTextMobile1;
    private EditText editTextEmail1;


    private Button buttonSave;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private long fileSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sinhala);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        buttonSave=(Button)findViewById(R.id.save_sinhala);


        editTextDiscription1=(EditText)findViewById(R.id.editText_disc_sinhala) ;
        editTextName1=(EditText)findViewById(R.id.edittxt_name_sinhala) ;
        editTextAddress1=(EditText)findViewById(R.id.edittxt_address_sinhala) ;
        editTextTelephone1=(EditText)findViewById(R.id.edittxt_tele_sinhala) ;
        editTextMobile1=(EditText)findViewById(R.id.edittxt_mobile_sinhala) ;
        editTextEmail1=(EditText)findViewById(R.id.edittxt_email_sinhala);

        buttonSave.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        editTextDiscription1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextDiscription1.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextName1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAddress1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextAddress1.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editTextTelephone1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextMobile1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editTextEmail1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        Intent i=new Intent(form_sinhala.this,Login.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonSave) {validation();}

    }
    public void validation(){
        final String email = editTextEmail1.getText().toString();
        if (!(ConnectivityReceiver.isConnected())){
            checkConnection();
        }

        if (ConnectivityReceiver.isConnected()) {


            if (editTextDiscription1.getText().toString().length() == 0) {
                editTextDiscription1.setError("විස්තරය ඇතුළත් කරන්න");
                editTextDiscription1.requestFocus();
            }
            else if (editTextName1.getText().toString().length() == 0) {
                editTextName1.setError("නම ඇතුළත් කරන්න");
                editTextName1.requestFocus();
            }

            else if (editTextAddress1.getText().toString().length() == 0) {
                editTextAddress1.setError("තැපැල් ලිපිනය ඇතුළත් කරන්න");
                editTextAddress1.requestFocus();
            }
            else if ((editTextTelephone1.getText().toString().length() == 0) || (!(editTextTelephone1.getText().toString().length() == 10))) {
                editTextTelephone1.setError("නිවැරදි අංකය ඇතුළත් කරන්න");
                editTextTelephone1.requestFocus();
            }

            else if ((editTextMobile1.getText().toString().length() == 0) || (!(editTextMobile1.getText().toString().length() == 10))) {
                editTextMobile1.setError("නිවැරදි අංකය ඇතුළත් කරන්න");
                editTextMobile1.requestFocus();
            }
            else {
                if (!isValidEmail(email)) {
                    editTextEmail1.setError("නිවැරදි විද්යුත් තැපැල් ලිපිනය ඇතුළත් කරන්න ");
                    editTextEmail1.requestFocus();


                }
            }
            if (!((editTextAddress1.getText().toString().length() == 0) &&
                    (editTextName1.getText().toString().length() == 0) &&
                    (editTextDiscription1.getText().toString().length() == 0)) && (
                    (editTextMobile1.getText().toString().length() == 10) &&
                            (editTextTelephone1.getText().toString().length() == 10) && (isValidEmail(email))
            )) {

                progress();
            }
        }
    }
    public void progress(){
        progressBar = new ProgressDialog(form_sinhala.this);
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
        }).start();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void registerUser() throws JSONException {
        final String discription = editTextDiscription1.getText().toString().trim();
        final String name = editTextName1.getText().toString().trim();
        final String address = editTextAddress1.getText().toString().trim();
        final int mobile=Integer.parseInt(editTextMobile1.getText().toString());
        final int telephone=Integer.parseInt(editTextTelephone1.getText().toString());
        final String email = editTextEmail1.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Intent i = new Intent(form_sinhala.this, result.class);
                        i.putExtra("id",response);
                        startActivity(i);
                        progressBar.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(form_sinhala.this,error.toString(),Toast.LENGTH_LONG).show();
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
                params.put(KEY_MOBILE,String.valueOf(mobile));
                params.put(KEY_EMAIL, email);
                return params;
            }


        } ;

        AppController.getInstance(form_sinhala.this).addTorequestque(stringRequest);

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
            editTextDiscription1.setEnabled(true);
            editTextName1.setEnabled(true);
            editTextAddress1.setEnabled(true);
            editTextTelephone1.setEnabled(true);
            editTextMobile1.setEnabled(true);
            editTextEmail1.setEnabled(true);

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
            editTextDiscription1.setEnabled(false);
            editTextName1.setEnabled(false);
            editTextAddress1.setEnabled(false);
            editTextTelephone1.setEnabled(false);
            editTextMobile1.setEnabled(false);
            editTextEmail1.setEnabled(false);


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
