package com.greylabs.itunesalbums.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.databinding.AlbumCardItemBinding;
import com.greylabs.itunesalbums.fragments.FragmentType;
import com.greylabs.itunesalbums.fragments.FragmentsController;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumsListAdapter extends RecyclerView.Adapter<AlbumsListAdapter.AlbumsViewHolder>{
    private ArrayList<AlbumModel> items = new ArrayList();
    private FragmentManager fragmentManager;

    public AlbumsListAdapter(ArrayList<AlbumModel> items, FragmentManager fragmentManager) {
        this.items = new ArrayList<>(items);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public AlbumsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AlbumCardItemBinding itemBinding = AlbumCardItemBinding.inflate(inflater, parent, false);
        return new AlbumsViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(AlbumsViewHolder holder, int position) {
        holder.binding.tvCollectionName.setText(items.get(position).getCollectionName());
        holder.binding.ivArtwork.post(() -> {
            Picasso.get().load(items.get(position).getArtworkUrl()).placeholder(R.drawable.album_placeholder).into(holder.binding.ivArtwork);
        });
        holder.binding.tvYearCountry.setText(items.get(position).getReleaseDate().split("-")[0]
                + " - "
                + items.get(position).getCountry());

        holder.binding.clAlbumCardItemContainer.setOnClickListener(view -> {
            FragmentsController.setLastAlbum(items.get(position));
            FragmentsController.showFragment(FragmentType.AlbumCard);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class AlbumsViewHolder extends RecyclerView.ViewHolder {
        private final AlbumCardItemBinding binding;

        public AlbumsViewHolder(AlbumCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
