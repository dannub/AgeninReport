package com.agenin.report.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.agenin.report.Fragment.PesananFragment;
import com.agenin.report.Fragment.UserFragment;
import com.agenin.report.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private UserFragment userFragment;
    private PesananFragment pesananFragment;


    private String name;
    private String email;
    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        frameLayout = findViewById(R.id.frame_layout);

        userFragment = new UserFragment();
        pesananFragment = new PesananFragment();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    setFragment(userFragment,false);
                }
                if (tab.getPosition()==1){
                    setFragment(pesananFragment,false);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.getTabAt(0).select();
        setFragment(userFragment,false);

    }

    private void setFragment(Fragment fragment, boolean setBundle ){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (setBundle) {
//            Bundle bundle = new Bundle();
//            bundle.putString("Name", name);
//            bundle.putString("Email", email);
//            bundle.putString("Photo", photo);
//            fragment.setArguments(bundle);
//        }else {
//            Bundle bundle = new Bundle();
//            bundle.putString("Email", email);
//            fragment.setArguments(bundle);
//        }
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}