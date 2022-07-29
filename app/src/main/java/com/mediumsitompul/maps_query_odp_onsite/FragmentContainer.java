package com.mediumsitompul.maps_query_odp_onsite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mediumsitompul.maps_query_odp_onsite.ui.changepassword.ChangePasswordFragment;
import com.mediumsitompul.maps_query_odp_onsite.ui.home.HomeFragment;
import com.mediumsitompul.maps_query_odp_onsite.ui.log.LogFragment;

public class FragmentContainer extends AppCompatActivity {
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        getSupportActionBar().hide();
        key = getIntent().getStringExtra("key");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (key){
            case "LOG CALANG":
                Fragment homeFragment = new HomeFragment();
                transaction.replace(R.id.fragment_framelayout,homeFragment);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "LOG INFO":
                Fragment logInfoFragment = new LogFragment();
                transaction.replace(R.id.fragment_framelayout,logInfoFragment);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "CHANGE PASSWORD":
                Fragment changePasswordFragment = new ChangePasswordFragment();
                transaction.replace(R.id.fragment_framelayout,changePasswordFragment);
//                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                finish();
        }
    }
}
