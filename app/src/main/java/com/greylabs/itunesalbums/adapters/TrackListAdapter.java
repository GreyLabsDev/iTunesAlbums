package com.greylabs.itunesalbums.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.greylabs.itunesalbums.databinding.AlbumCardItemBinding;
import com.greylabs.itunesalbums.databinding.TrackListItemBinding;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.greylabs.itunesalbums.models.TrackModel;
import com.greylabs.itunesalbums.network.ITunesApiProcessor;

import java.util.ArrayList;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TracksViewHolder> {
    private ArrayList<TrackModel> items = new ArrayList();

    public TrackListAdapter(ArrayList<TrackModel> items) {
        this.items = items;
        Log.d("BindingTest", ""+ items.get(0).getArtistName() + " - " + items.get(0).getTrackName());
    }

    @Override
    public TrackListAdapter.TracksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TrackListItemBinding itemBinding = TrackListItemBinding.inflate(inflater, parent, false);
        return new TracksViewHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(TracksViewHolder holder, int position) {
        Log.d("BindingTest", ""+ items.get(position).getArtistName() + " - " + items.get(position).getTrackName());
        holder.binding.tvTrackTitle.setText(position + ". " + items.get(position).getArtistName() + " - " + items.get(position).getTrackName());
        holder.binding.tvTrackTime.setText("");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TracksViewHolder extends RecyclerView.ViewHolder {
        private final TrackListItemBinding binding;

        public TracksViewHolder(TrackListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
