package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //Quin: Test comment to ensure commits are going through
    //Shea test commit

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Implementing View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Example of using view binding so I dont have to look it up:
        /*binding.<elementId>.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.textView01.setText("Welcome!");
            }
        });*/
    }
}