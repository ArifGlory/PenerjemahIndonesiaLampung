package myproject.kamuslampung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import myproject.kamuslampung.activity.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
