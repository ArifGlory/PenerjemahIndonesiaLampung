package myproject.kamuslampung.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import myproject.kamuslampung.R;

public class BantuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
    }

    public void keHome(View view) {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
}
