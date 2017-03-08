package com.example.serenade.newsound;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.example.serenade.newsound.find.fragment.FindFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup radioGroup;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        transaction=getSupportFragmentManager().beginTransaction();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FindFragment fragment = null;
        switch (checkedId) {
            case R.id.mine:
                break;
            case R.id.music:
                break;
            case R.id.find:
                fragment=new FindFragment();
                break;
            case R.id.synchronize:
                break;
        }
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }
}
