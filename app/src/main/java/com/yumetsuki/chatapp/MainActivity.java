package com.yumetsuki.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this)
                    .get(MainViewModel.class);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.stopTimer();
    }
}
