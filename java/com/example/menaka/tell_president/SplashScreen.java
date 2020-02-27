
package com.example.menaka.tell_president;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.View;
        import android.widget.ProgressBar;

        import java.io.IOException;


public class SplashScreen extends Activity {
    ProgressBar pg;
    int progress=0;
    Handler h= new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pg=(ProgressBar)findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                    for(int i=0;i<=5;i++){

                        progress+=25;
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                pg.setProgress(progress);
                                if(progress==pg.getMax()){
                                    Intent i=new Intent(SplashScreen.this,Login.class);
                                    startActivity(i);

                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){

                        }
                    }
            }
        }).start();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        SplashScreen.this.finish();
    }

}