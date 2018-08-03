package com.greylabs.itunesalbums.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.adapters.AlbumsListAdapter;
import com.greylabs.itunesalbums.adapters.TrackListAdapter;
import com.greylabs.itunesalbums.databinding.AlbumsCardFragmentBinding;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.greylabs.itunesalbums.models.TrackModel;
import com.greylabs.itunesalbums.network.ITunesApiProcessor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class AlbumCardFragment extends Fragment {
    public AlbumsCardFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (FragmentsController.getLastAlbum() != null) {
            binding.tvEmptyAlbum.setVisibility(View.INVISIBLE);

            binding.ivShareBtn.setVisibility(View.VISIBLE);
            binding.ivArtworkInCard.setVisibility(View.VISIBLE);
            binding.ivBlurBg.setVisibility(View.VISIBLE);
            binding.tvArtistInCard.setVisibility(View.VISIBLE);
            binding.tvCollectionNameInCard.setVisibility(View.VISIBLE);
            binding.tvGenreAndYearInCard.setVisibility(View.VISIBLE);
            binding.tvCopyrightInCard.setVisibility(View.VISIBLE);
            binding.tvReleaseDateInCard.setVisibility(View.VISIBLE);

            AlbumModel currentAlbum = FragmentsController.getLastAlbum();

            binding.tvCollectionNameInCard.setText(currentAlbum.getCollectionName());
            binding.tvArtistInCard.setText(currentAlbum.getArtistName());
            binding.tvCollectionNameInCard.setText(currentAlbum.getCollectionName());
            binding.tvGenreAndYearInCard.setText(currentAlbum.getPrimaryGenreName()
                    + " - "
                    + currentAlbum.getReleaseDate().split("-")[0]);
            binding.tvCopyrightInCard.setText(currentAlbum.getCopyright());

            binding.ivShareBtn.setOnClickListener(view1 -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, getContext().getResources().getString(R.string.share_text) + "\n" + currentAlbum.getCollectionViewUrl());
                if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(Intent.createChooser(shareIntent, "Share using"));
                }
            });

            String dateArray[] = currentAlbum.getReleaseDate().split("-");

            binding.tvReleaseDateInCard.setText(dateArray[0] + "." + dateArray[1] + "." + dateArray[2].substring(0,2));

            binding.clMainAlbumFSCardContainer.post(() -> {
                Picasso.get()
                        .load(FragmentsController.getLastAlbum().getArtworkUrl())
                        .placeholder(R.drawable.album_placeholder)
                        .into(binding.ivArtworkInCard);
                Picasso.get()
                        .load(FragmentsController.getLastAlbum()
                                .getArtworkUrl())
                        .placeholder(R.drawable.album_placeholder)
                        .into(binding.ivBlurBg);
                Blurry.with(getContext())
                        .radius(25)
                        .capture(binding.ivBlurBg)
                        .into(binding.ivBlurBg);
            });

            binding.pbTracksLoading.animate().alpha(1f).setDuration(100L).start();

            ITunesApiProcessor.getTracksAlbumDataObservable(currentAlbum.getCollectionId()).subscribe(trackList -> {
                binding.clMainAlbumFSCardContainer.post(() -> {
                    binding.pbTracksLoading.animate().alpha(0f).setDuration(100L).start();
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    binding.rvTrackList.setLayoutManager(llm);
                    TrackListAdapter adapter = new TrackListAdapter(trackList);
                    binding.rvTrackList.setAdapter(adapter);
                });
            });
        } else {
            binding.tvEmptyAlbum.setVisibility(View.VISIBLE);

            binding.ivShareBtn.setVisibility(View.INVISIBLE);
            binding.ivArtworkInCard.setVisibility(View.INVISIBLE);
            binding.ivBlurBg.setVisibility(View.INVISIBLE);
            binding.tvArtistInCard.setVisibility(View.INVISIBLE);
            binding.tvCollectionNameInCard.setVisibility(View.INVISIBLE);
            binding.tvGenreAndYearInCard.setVisibility(View.INVISIBLE);
            binding.tvCopyrightInCard.setVisibility(View.INVISIBLE);
            binding.tvReleaseDateInCard.setVisibility(View.INVISIBLE);
        }



        super.onViewCreated(view, savedInstanceState);
    }

    public static AlbumCardFragment newInstance() {
        return new AlbumCardFragment();
    }
}
