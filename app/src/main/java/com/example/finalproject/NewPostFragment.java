package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalproject.database.DBViewModel;
import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentNewPostBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentNewPostBinding binding;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();;
    private DatabaseReference mPostRef = mRootRef.child("posts");

    private String editTitle, editContent, date;
    private LatLng location;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DBViewModel viewModel;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewPostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewPostFragment newInstance(String param1, String param2) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewPostBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DBViewModel.class);

        binding.publishPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTitle = binding.titleInput.getText().toString();
                editContent = binding.contentInput.getText().toString();

                //Ensures title and content are not blank before attempting to write to DB.
                if(TextUtils.isEmpty(editTitle) || TextUtils.isEmpty(editContent) ){
                    Toast.makeText(getContext(), "ERROR: All fields must not be blank.", Toast.LENGTH_SHORT).show();
                    Log.d("===TESTING: NEW_POST===", "Publish unsuccessful: Blank field.");
                }else{
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy. hh:mm aa", Locale.getDefault());
                    date = df.format(d);

                    location = new LatLng( 50.665493, -120.332842); //TODO: Get proper location.

                    String postId = mRootRef.push().getKey();

                    Log.d("New PostId:", postId);

                    Post post = new Post(postId, editTitle, editContent, date, location.latitude, location.longitude, 1, 0);

                    viewModel.insertPost(post);
                    //TODO: close new post fragment and return to feed fragment.
                }
            }
        });
    }
}