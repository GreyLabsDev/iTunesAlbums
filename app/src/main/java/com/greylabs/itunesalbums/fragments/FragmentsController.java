package com.greylabs.itunesalbums.fragments;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.greylabs.itunesalbums.models.TrackModel;
import java.util.ArrayList;

public class FragmentsController {
	
	private static ArrayList<AlbumModel> lastAlbumSet = new ArrayList<>();
	private static ArrayList<TrackModel> lastTrackList = new ArrayList<>();
	private static AlbumModel lastAlbum = null;
	
	private static ArrayList<FragmentType> state = new ArrayList<>();
	
	private static BottomNavigationView bnvToControl = null;
	
	private static FragmentManager baseFragmentManager;
	
	public static void showPreviousFragment() {
		if (state.size() == 2) {
			switch (state.get(0)) {
				case AlbumsList: {
					bnvToControl.getMenu().findItem(R.id.item_search).setChecked(true);
					break;
				}
				case AlbumCard: {
					bnvToControl.getMenu().findItem(R.id.item_last_album).setChecked(true);
					break;
				}
				case About: {
					bnvToControl.getMenu().findItem(R.id.item_about).setChecked(true);
					break;
				}
				default:
					break;
			}
			showFragment(state.get(0));
		}
	}
	
	public static void showFragment(FragmentType fragmentType) {
		if ((state.size() >= 1 && state.get(state.size() - 1) != fragmentType) || (state.size() == 0)) {
			switch (fragmentType) {
				case AlbumCard: {
					FragmentTransaction fTransaction = baseFragmentManager.beginTransaction()
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					fTransaction.addToBackStack("card")
							.replace(R.id.clFragmentContainer, AlbumCardFragment.newInstance());
					bnvToControl.getMenu().findItem(R.id.item_last_album).setChecked(true);
					fTransaction.commit();
					updateFragmentState(fragmentType);
					break;
				}
				case AlbumsList: {
					FragmentTransaction fTransaction = baseFragmentManager.beginTransaction()
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					fTransaction.replace(R.id.clFragmentContainer, AlbumsListFragment.newInstance());
					bnvToControl.getMenu().findItem(R.id.item_search).setChecked(true);
					fTransaction.commit();
					updateFragmentState(fragmentType);
					break;
				}
				case About: {
					FragmentTransaction fTransaction = baseFragmentManager.beginTransaction()
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					fTransaction.replace(R.id.clFragmentContainer, AboutFragment.newInstance());
					bnvToControl.getMenu().findItem(R.id.item_about).setChecked(true);
					fTransaction.commit();
					updateFragmentState(fragmentType);
					break;
				}
				default:
					break;
			}
		}
		
	}
	
	private static void updateFragmentState(FragmentType fragmentType) {
		if (state.size() <= 1) {
			switch (state.size()) {
				case 0: {
					state.add(fragmentType);
					break;
				}
				case 1: {
					if (state.get(state.size() - 1) != fragmentType) {
						state.add(fragmentType);
						break;
					}
				}
			}
		} else if (state.size() == 2 && state.get(state.size() - 1) != fragmentType) {
			state.add(fragmentType);
			state.remove(0);
		}
	}
	
	public static BottomNavigationView getBnvToControl() {
		return bnvToControl;
	}
	
	public static void setBnvToControl(BottomNavigationView bnvToControl) {
		FragmentsController.bnvToControl = bnvToControl;
	}
	
	public static FragmentManager getFragmentManager() {
		return baseFragmentManager;
	}
	
	public static void setFragmentManager(FragmentManager fragmentManager) {
		baseFragmentManager = fragmentManager;
	}
	
	public static ArrayList<AlbumModel> getLastAlbumSet() {
		return lastAlbumSet;
	}
	
	public static void setLastAlbumSet(ArrayList<AlbumModel> inLastAlbumSet) {
		lastAlbumSet = inLastAlbumSet;
	}
	
	public static ArrayList<TrackModel> getLastTrackList() {
		return lastTrackList;
	}
	
	public static void setLastTrackList(ArrayList<TrackModel> inLastTrackList) {
		lastTrackList = inLastTrackList;
	}
	
	public static AlbumModel getLastAlbum() {
		return lastAlbum;
	}
	
	public static void setLastAlbum(AlbumModel inLlastAlbum) {
		lastAlbum = inLlastAlbum;
	}
	
}
