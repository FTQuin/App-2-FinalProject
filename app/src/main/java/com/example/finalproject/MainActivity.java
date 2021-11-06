package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Implementing View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Setting feed fragment to open when app launches.
        fragmentTransaction.add(binding.fragmentContainerView.getId(), new FeedFragment());
        fragmentTransaction.commit();

        //==========================================================================================
        //Ignore this shit between =====. Just me figuring out how to use Firebase lol.

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("message");

        ref.setValue("Hello World.");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String value = snapshot.getValue(String.class);
                Log.d("====FIREBASE", "Value: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to read value.
                Log.w("FIREBASE", "Failed to read value. ", error.toException());
            }
        });
        //==========================================================================================

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display map fragment on button click.
                binding.locationBtn.setText("Kelowna");

                //Below code causes app to crash.
                //fragmentTransaction.replace(binding.fragmentContainerView.getId(), new MapsFragment());
                //fragmentTransaction.commit();
            }
        });

        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display menu fragment on button click.
            }
        });

        binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display chat fragment on button click.
            }
        });
    }
}