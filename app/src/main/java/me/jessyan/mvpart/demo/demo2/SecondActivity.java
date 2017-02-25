package me.jessyan.mvpart.demo.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.jessyan.mvpart.demo.R;

/**
 * Created by jess on 25/02/2017 18:04
 * Contact with jess.yan.effort@gmail.com
 * 这个Activity只是为了包裹,需要展示的fragment
 */

public class SecondActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new SecondFragment())
                .commit();
    }

}
