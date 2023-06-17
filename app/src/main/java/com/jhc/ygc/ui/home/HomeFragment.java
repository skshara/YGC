package com.jhc.ygc.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jhc.ygc.AudiobookActivity;
import com.jhc.ygc.QuizActivity;
import com.jhc.ygc.R;
import com.jhc.ygc.VideoActivity;
import com.jhc.ygc.databinding.FragmentHomeBinding;
import com.jhc.ygc.eBookActivity;

public class HomeFragment extends Fragment {

    ImageButton video,quiz,eBook,aBook;


private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

    binding = FragmentHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        video = (ImageButton)root.findViewById(R.id.video);
        eBook = (ImageButton)root.findViewById(R.id.e_book);
        aBook = (ImageButton)root.findViewById(R.id.Audio);
        quiz = (ImageButton)root.findViewById(R.id.quiz);

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VideoActivity.class));
            }
        });

        eBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), eBookActivity.class));
            }
        });

        aBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AudiobookActivity.class));
            }
        });

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), QuizActivity.class));
            }
        });
        return root;




    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}