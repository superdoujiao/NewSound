package com.example.serenade.newsound.find.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.serenade.newsound.R;

/**
 * Created by 刘瑞忻 on 2017/3/4.
 */

public class FindFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    private FragmentManager manager;

    private RadioGroup rg;
    private RadioButton rb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.findfragment,container,false);

        rg= (RadioGroup) view.findViewById(R.id.findfragment_rg);
        rg.setOnCheckedChangeListener(this);

//        rb= (RadioButton) view.findViewById(R.id.findfragment_rg_find);
//        rb.setChecked(true);
        manager=getFragmentManager();
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Fragment fragment=null;

        switch (checkedId){
            case R.id.findfragment_rg_find:
                fragment=new FindFragment_Find();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.findfragment_framelayout,fragment);
                transaction.commit();
                break;

            case R.id.findfragment_rg_nearby:
                fragment=new FindFragment_Nearby();
                FragmentTransaction transaction2=manager.beginTransaction();
                transaction2.replace(R.id.findfragment_framelayout,fragment);
                transaction2.commit();
                break;
        }


    }
}
