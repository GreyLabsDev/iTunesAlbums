package com.greylabs.itunesalbums;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import com.greylabs.itunesalbums.databinding.ActivityMainBinding;
import com.greylabs.itunesalbums.fragments.AlbumsListFragment;
import com.greylabs.itunesalbums.fragments.FragmentType;
import com.greylabs.itunesalbums.fragments.FragmentsController;
import com.greylabs.itunesalbums.network.ITunesApiProcessor;
import com.greylabs.itunesalbums.tools.SnacksMachine;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    String TAG = "MainActivity";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SnacksMachine.initColors();

        ITunesApiProcessor.setContext(this);

        FragmentsController.setFragmentManager(getSupportFragmentManager());
        FragmentsController.setBnvToControl(binding.bnvNavigation);
        FragmentsController.showFragment(FragmentType.AlbumsList);

        binding.bnvNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_search: {
                                FragmentsController.showFragment(FragmentType.AlbumsList);
                                break;
                            }
                            case R.id.item_last_album: {
                                FragmentsController.showFragment(FragmentType.AlbumCard);
                                break;
                            }
                            case R.id.item_about: {
                                FragmentsController.showFragment(FragmentType.About);
                                break;
                            }
                            default: break;

                        }
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        FragmentsController.showPreviousFragment();
    }
}
