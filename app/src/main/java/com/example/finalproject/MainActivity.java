package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.feed.FeedFragment;
import com.example.finalproject.viewpost.CommentRecycler;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FeedFragment feedFragment;
    private NewPostFragment newPostFragment;
    private MapsFragment mapsFragment;
    private CommentRecycler viewPostFragment;
    private MenuFragment menuFragment;

    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedFragment = new FeedFragment();
        newPostFragment = new NewPostFragment();

        //To remove top app title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        //Implementing View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Setting feed fragment to open when app launches.
        fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                feedFragment).commit();

        binding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display map fragment on button click.
            }
        });

        binding.newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                //If statement to allow clicking button to toggle fragment.
                if (fragmentInFrame instanceof FeedFragment){
                    Log.d("debug", "Feed fragment displayed. would show new post fragment");
                    fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                            newPostFragment).addToBackStack("new_post_frag").commit();
                } else if (fragmentInFrame instanceof NewPostFragment){
                    Log.d("debug", "new post fragment shown. would remove new post fragment.");
                    fragmentManager.beginTransaction().remove(newPostFragment).commit();
                }

                // TODO: animate appearance of fragment. disable touch actions on feed feed fragment.
            }
        });

        binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display chat fragment on button click.
            }
        });

        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display menu fragment on button click.
            }
        });
    }
}