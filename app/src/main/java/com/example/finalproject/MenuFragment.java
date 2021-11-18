package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.databinding.FragmentMenuBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;

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

        binding.menuPopup.setVisibility(View.INVISIBLE);
        binding.popupUpgradeBtn.setVisibility(View.INVISIBLE);

        //binding.yourLocationsBtn.setEnabled(false);
        //binding.yourPostsBtn.setEnabled(false);

        binding.upgradeBtn.setOnClickListener(view13 -> {
            //TODO: if version == upgraded. hide or disable button.
            String title = "Upgrade";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.upgrade_text);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.setVisibility(View.VISIBLE);
        });

        binding.helpAndSupportBtn.setOnClickListener(view12 -> {
            String title = "Help and Support";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.help_text);
            binding.popupUpgradeBtn.setVisibility(View.INVISIBLE);
            binding.menuPopup.setVisibility(View.VISIBLE);
        });

        binding.yourPostsBtn.setOnClickListener(view1 -> {
            //TODO: if version == upgraded. show user's posts
            //Note: would have to add user id's to posts so we may not do this.
            String title = "Your Posts";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.your_posts_text_base_version);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.setVisibility(View.VISIBLE);
        });

        binding.yourLocationsBtn.setOnClickListener(view1 -> {
            //TODO: if version == upgraded. open map fragment with list of saved locations.
            String title = "Your Locations";
            binding.menuPopupTitleText.setText(title);
            binding.menuPopupContentText.setText(R.string.your_locations_text_base_version);
            binding.popupUpgradeBtn.setVisibility(View.VISIBLE);
            binding.menuPopup.setVisibility(View.VISIBLE);
        });

        binding.popupUpgradeBtn.setOnClickListener(view14 -> {
            //TODO: Add upgrade functionality.
        });


    }
}