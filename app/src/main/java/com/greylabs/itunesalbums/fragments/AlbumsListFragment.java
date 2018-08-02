package com.greylabs.itunesalbums.fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.adapters.AlbumsListAdapter;
import com.greylabs.itunesalbums.databinding.AlbumsListFragmentBinding;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.greylabs.itunesalbums.network.ITunesApiProcessor;
import com.greylabs.itunesalbums.tools.SnacksMachine;

import java.util.ArrayList;

public class AlbumsListFragment extends Fragment{
    String TAG = "AlbumsListFragment";
    private AlbumsListFragmentBinding binding;
    private Boolean searchIsOpen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SnacksMachine.setCurrentView(binding.clMainAlbumsListFragmentContainer);

        if (!FragmentsController.getLastAlbumSet().isEmpty()) {
            updateView(FragmentsController.getLastAlbumSet());
            searchIsOpen = false;
        }
        
        binding.floatingActionButton.setOnClickListener(view12 -> {
            if (searchIsOpen) {
                if (!binding.etSearchField.getText().toString().equals("")) {
                    binding.rvFoundAlbumsList.animate().alpha(0f).setDuration(100L).start();
                    binding.pbAlbumsLoading.animate().alpha(1f).setDuration(100L).start();
                    ITunesApiProcessor.getAlbumsByAuthorObservable(binding.etSearchField.getText().toString()).subscribe(
                            foundAlbums -> {

                                if (foundAlbums.isEmpty()) {
                                    binding.pbAlbumsLoading.post(() -> binding.pbAlbumsLoading.animate().alpha(0f).setDuration(100L).start());
                                    SnacksMachine.showSnackbar(getResources().getString(R.string.query_error), "orange");
                                } else {
                                    Log.d(TAG, "onCreate: " + foundAlbums.get(0).getArtworkUrl());
                                    FragmentsController.setLastAlbumSet(foundAlbums);
                                    updateView(foundAlbums);
                                }
                            }
                    );
                } else {
                    SnacksMachine.showSnackbar(getResources().getString(R.string.empty_text), "orange");
                }

            } else {
                searchIsOpen = !searchIsOpen;
                binding.clSearchContainer.setVisibility(View.VISIBLE);
                binding.clSearchContainer.animate().alpha(1f).setDuration(300L).start();
            }
        });
        
        binding.rvFoundAlbumsList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    binding.floatingActionButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                searchIsOpen = false;
                if (dy > 0 || dy < 0) {
                    if (binding.floatingActionButton.isShown()) {
                    binding.floatingActionButton.hide();
                    binding.clSearchContainer.animate().alpha(0f).setDuration(150L).start();
                    binding.clSearchContainer.setVisibility(View.INVISIBLE);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public static AlbumsListFragment newInstance() {
        return new AlbumsListFragment();
    }
    
    private void updateView(ArrayList<AlbumModel> albumsToView) {
        binding.clMainAlbumsListFragmentContainer.post(() -> {
            binding.rvFoundAlbumsList.animate().alpha(1f).setDuration(100L).start();
            binding.pbAlbumsLoading.animate().alpha(0f).setDuration(100L).start();
            GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
            binding.rvFoundAlbumsList.setLayoutManager(glm);
            AlbumsListAdapter adapter = new AlbumsListAdapter(albumsToView, getFragmentManager());
            binding.rvFoundAlbumsList.setAdapter(adapter);
        });
    
        binding.tvSongTitle.post(() -> {
            binding.tvSongTitle.setText(albumsToView.get(0).getArtistName());
        });
    }
}
