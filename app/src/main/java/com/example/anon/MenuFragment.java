package com.example.anon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.anon.databinding.FragmentMenuBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private int btnOption;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String upgrade = "Upgrade";
        String signOutConfirm = "Yes, Sign me out";

        binding.background.animate().alpha(1.0f).setStartDelay(150).setDuration(150).setInterpolator(new LinearInterpolator());

        binding.menuPopup.animate().alpha(0.0f).setDuration(0);
        //binding.menuPopup.setVisibility(View.INVISIBLE);
        binding.popupUpgradeBtn.setVisibility(View.INVISIBLE);

        //binding.yourLocationsBtn.setEnabled(false);
        //binding.yourPostsBtn.setEnabled(false);

        binding.upgradeBtn.setOnClickListener(view13 -> {
            //TODO: if version == upgraded. hide or disable button.
            btnOption = 0;
            String title = "Upgrade";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.upgrade_text);
            binding.popupUpgradeBtn.setText(upgrade);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.animate().alpha(1.0f).setDuration(200);
        });

        binding.helpAndSupportBtn.setOnClickListener(view12 -> {
            String title = "Help and Support";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.help_text);
            binding.popupUpgradeBtn.setVisibility(View.INVISIBLE);
            binding.menuPopup.animate().alpha(1.0f).setDuration(200);
        });

        binding.yourPostsBtn.setOnClickListener(view1 -> {
            //TODO: if version == upgraded. show user's posts
            btnOption = 0;
            //Note: would have to add user id's to posts so we may not do this.
            String title = "Your Posts";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.your_posts_text_base_version);
            binding.popupUpgradeBtn.setText(upgrade);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.animate().alpha(1.0f).setDuration(200);
        });

        binding.yourLocationsBtn.setOnClickListener(view1 -> {
            //TODO: if version == upgraded. open map fragment with list of saved locations.
            btnOption = 0;
            String title = "Your Locations";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.your_locations_text_base_version);
            binding.popupUpgradeBtn.setText(upgrade);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.animate().alpha(1.0f).setDuration(200);
        });

        binding.popupUpgradeBtn.setOnClickListener(view14 -> {

            if(btnOption == 0) {
                //TODO: Add upgrade functionality.
                Toast.makeText(getContext(), "Upgrading", Toast.LENGTH_SHORT).show();
            } else if (btnOption == 1) {
                //TODO: sign out of google. Show sign in screen.
                Toast.makeText(getContext(), "Signing Out", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();
                getActivity().moveTaskToBack(true);
                getActivity().finish();
            }
        });

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOption = 1;
                String title = "Sign Out";
                String signOutMsg = "Are you sure you want to sign out? This will return you to" +
                        " the sign in screen. Tap the back button to cancel.";
                binding.menuPopupTitleText.setText(title);
                binding.menuPopupContentText.setText(signOutMsg);
                binding.popupUpgradeBtn.setText(signOutConfirm);
                binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
                binding.menuPopup.animate().alpha(1.0f).setDuration(200);
            }
        });
    }
}