package com.example.anon;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.anon.database.DBViewModel;
import com.example.anon.database.Post;
import com.example.anon.databinding.FragmentNewPostBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewPostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewPostFragment extends Fragment {
    private static final String ARG_LOCALITY = "locality";
    private static final String ARG_SUB_ADMIN_AREA = "sub_admin_area";

    private FragmentNewPostBinding binding;

    private String editTitle, editContent, date, key;

    private String locality, subAdminArea;
    private DBViewModel viewModel;

    public NewPostFragment() {
    }

    public static NewPostFragment newInstance(String locality, String subAdminArea) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCALITY, locality);
        args.putString(ARG_SUB_ADMIN_AREA, subAdminArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            locality = getArguments().getString(ARG_LOCALITY);
            subAdminArea = getArguments().getString(ARG_SUB_ADMIN_AREA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewPostBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.background.animate().alpha(1.0f).setStartDelay(150).setDuration(150).setInterpolator(new LinearInterpolator());

        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        binding.publishPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTitle = binding.titleInput.getText().toString();
                editContent = binding.contentInput.getText().toString();

                //Checks if title and content are blank before attempting to write to DB.
                if (TextUtils.isEmpty(editTitle) || TextUtils.isEmpty(editContent)) {
                    Toast.makeText(getContext(), "All fields must be filled in", Toast.LENGTH_SHORT).show();
                } else {
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy. hh:mm aa", Locale.getDefault());
                    date = df.format(d);
                    String loc = locality;
                    String saa = subAdminArea;
                    key = viewModel.getNewKey();

                    Post post = new Post(key, editTitle, editContent, date, loc, saa, 1, 0);

                    viewModel.insertPost(post);
                    viewModel.refreshFeed();

                    binding.titleInput.setText("");
                    binding.contentInput.setText("");

                    // hide keyboard
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                    //Closes new post fragment.
                    //NOTE: Fragment must be call addToBackStack() before commit() in main
                    getActivity().getSupportFragmentManager().popBackStackImmediate();

                }
            }
        });
    }
}