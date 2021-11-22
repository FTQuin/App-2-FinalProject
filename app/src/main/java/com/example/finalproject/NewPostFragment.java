package com.example.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.database.DBViewModel;
import com.example.finalproject.database.Post;
import com.example.finalproject.databinding.FragmentNewPostBinding;
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

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_DEVICE_LOC = "device_location";
    //private static final String ARG_DEVICE_LAT = "location_latitude";
    //private static final String ARG_DEVICE_LONG = "location_longitude";
    private static final String ARG_LOCALITY = "locality";
    private static final String ARG_SUB_ADMIN_AREA = "sub_admin_area";

    private FragmentNewPostBinding binding;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();;
    private DatabaseReference mPostRef = mRootRef.child("posts");

    private String editTitle, editContent, date, locationName;

    private String locality, subAdminArea;
    private DBViewModel viewModel;

    public NewPostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewPostFragment.
     */
    //public static NewPostFragment newInstance(String deviceLocation) {
    public static NewPostFragment newInstance(String locality, String subAdminArea) {
        NewPostFragment fragment = new NewPostFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_DEVICE_LOC, deviceLocation);
        args.putString(ARG_LOCALITY, locality);
        args.putString(ARG_SUB_ADMIN_AREA, subAdminArea);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            //deviceLocation = getArguments().getString(ARG_DEVICE_LOC);
            locality = getArguments().getString(ARG_LOCALITY);
            subAdminArea = getArguments().getString(ARG_SUB_ADMIN_AREA);
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

        String loc = locality;
        String saa = subAdminArea;

        Toast.makeText(getContext(), "Location:\n" + loc + "\n"+ saa, Toast.LENGTH_SHORT).show();

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

                    //String loc = locality;
                    //String saa = subAdminArea;

                    //TODO: delete these and uncomment above.
                    /*Just had them hardcoded because location isn't fully working yet.*/
                    String loc = "Kamloops";
                    String saa = "Thompson-Nicola";

                    String postId = mRootRef.push().getKey();

                    Log.d("New PostId:", postId);

                    Post post = new Post(postId, editTitle, editContent, date, loc, saa, 1, 0);

                    viewModel.insertPost(post);

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