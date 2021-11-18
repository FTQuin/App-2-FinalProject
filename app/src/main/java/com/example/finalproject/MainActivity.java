package com.example.finalproject;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.databinding.ActivityMainBinding;
import com.example.finalproject.feed.FeedFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FeedFragment feedFragment;
    private NewPostFragment newPostFragment;

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
                final Fragment fragmentInFrame = getSupportFragmentManager()
                        .findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().setTransition(FragmentTransaction
                            .TRANSIT_FRAGMENT_OPEN).add(binding.fragmentContainerView.getId(),
                            newPostFragment).addToBackStack("feed_frag").commit();
                    //binding.newPostBtn.setImageResource(R.drawable.ic_down_40);
                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            0, 45).setDuration(100).start();

                }else if (fragmentInFrame instanceof NewPostFragment){
                    fragmentManager.popBackStackImmediate();
                    //binding.newPostBtn.setImageResource(R.drawable.ic_add);
                    ObjectAnimator.ofFloat(binding.newPostBtn, "rotation",
                            45, 0).setDuration(100).start();

                }

                // TODO: animate appearance of fragment
            }
        });

        /*binding.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display chat fragment on button click.
                final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                            chatFragment).addToBackStack("chat_frag").commit();
                    binding.newPostBtn.setImageResource(R.drawable.ic_down_40);
                }else if (fragmentInFrame instanceof ChatFragment){
                    fragmentManager.popBackStackImmediate();
                    binding.newPostBtn.setImageResource(R.drawable.ic_chat);
                }
            }
        });*/

        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: display menu fragment on button click.
                /*final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

                if (fragmentInFrame instanceof FeedFragment){
                    fragmentManager.beginTransaction().add(binding.fragmentContainerView.getId(),
                            menuFragment).addToBackStack("feed_frag").commit();
                    binding.newPostBtn.setImageResource(R.drawable.ic_down_40);
                }else if (fragmentInFrame instanceof MenuFragment){
                    fragmentManager.popBackStackImmediate();
                    binding.newPostBtn.setImageResource(R.drawable.ic_menu);
                }*/
            }
        });
    }

    public void onBackPressed(){
        super.onBackPressed();
        ObjectAnimator.ofFloat(binding.newPostBtn, "rotation", 45, 0).setDuration(100).start();
        //binding.newPostBtn.setImageResource(R.drawable.ic_add);
        //binding.chatBtn.setImageResource(R.drawable.ic_chat);
        //binding.menuBtn.setImageResource(R.drawable.ic_menu);
    }
}