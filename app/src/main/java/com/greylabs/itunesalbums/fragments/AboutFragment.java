package com.greylabs.itunesalbums.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.databinding.AboutFragmentBinding;
import com.greylabs.itunesalbums.tools.FunAnimator;

public class AboutFragment extends Fragment {
    public AboutFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ivGithubBtn.setOnClickListener(view1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(getString(R.string.about_link)));
            getContext().startActivity(browserIntent);
        });

        FunAnimator gitBtnAnimator = new FunAnimator(binding.ivGithubBtn);
        gitBtnAnimator.startAnimator();
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }
}
