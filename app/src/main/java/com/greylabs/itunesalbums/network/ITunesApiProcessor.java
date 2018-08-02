package com.greylabs.itunesalbums.network;

import android.content.Context;
import android.util.Log;

import com.greylabs.itunesalbums.R;
import com.greylabs.itunesalbums.models.AlbumModel;
import com.greylabs.itunesalbums.models.TrackModel;
import com.greylabs.itunesalbums.tools.SnacksMachine;

import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ITunesApiProcessor {
    static String TAG = "ITunesApiProcessor";

    private static OkHttpClient client = new OkHttpClient();

    private static String ITUNES_API_BASE_URL = "https://itunes.apple.com/search?term=";
    private static String ITUNES_API_ALBUMS_POSTFIX = "&media=music&entity=album";
    private static String ITUNES_API_ALBUM_DATA_BASE_URL = "https://itunes.apple.com/lookup?id=";
    private static String ITUNES_API_ALBUM_DATA_POSTFIX = "&entity=song";

    private static Context context;

    private static String getPreparedStrind(String queryString) {
        if (queryString.contains(" ")) {
            return queryString.replaceAll(" ", "+");
        } else return queryString;
    }

    public static Observable<ArrayList<AlbumModel>> getAlbumsByAuthorObservable(String queryString) {
        Observable<ArrayList<AlbumModel>> outObservable = Observable.create(emitter -> {
            String searchUrl = ITUNES_API_BASE_URL
                    + getPreparedStrind(queryString)
                    + ITUNES_API_ALBUMS_POSTFIX;

            Request req = new Request.Builder()
                    .url(searchUrl)
                    .get()
                    .build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SnacksMachine.showSnackbar(context.getResources().getString(R.string.network_error), "red");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonResp = new JSONObject(response.body().string());
                        JSONArray albumsArray = jsonResp.getJSONArray("results");
                        if (albumsArray.length() == 0) {
                            ArrayList<AlbumModel> emptyModel = new ArrayList<>();
                            emitter.onNext(emptyModel);
                        } else {
                            emitter.onNext(getAlbumsFromJsonArray(albumsArray));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        return outObservable;
    }

    public static Observable<ArrayList<TrackModel>> getTracksAlbumDataObservable(String collectionId) {
        Observable<ArrayList<TrackModel>> outObservable = Observable.create(emitter -> {
            String searchUrl = ITUNES_API_ALBUM_DATA_BASE_URL
                    + collectionId
                    + ITUNES_API_ALBUM_DATA_POSTFIX;

            Request req = new Request.Builder()
                    .url(searchUrl)
                    .get()
                    .build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    SnacksMachine.showSnackbar(context.getResources().getString(R.string.network_error), "red");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonResp = new JSONObject(response.body().string());
                        JSONArray albumDataArray = jsonResp.getJSONArray("results");
                        emitter.onNext(getTracksFromJsonArray(albumDataArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SnacksMachine.showSnackbar(context.getResources().getString(R.string.query_error), "orange");
                    }
                }
            });
        });

        return outObservable;
    }

    private static ArrayList<AlbumModel> getAlbumsFromJsonArray(JSONArray inArray) throws JSONException {
        ArrayList<AlbumModel> outAlbums = new ArrayList();
        for (int i = 0; i < inArray.length(); i++) {
            JSONObject album = inArray.getJSONObject(i);

            AlbumModel albumModel = new AlbumModel();
            albumModel.setArtistName(album.getString("artistName"));
            albumModel.setCollectionName(album.getString("collectionName"));
            albumModel.setArtworkUrl(album.getString("artworkUrl100").replaceAll("100", "220"));
            albumModel.setCollectionPrise(album.getString("collectionPrice"));
            albumModel.setTrackCount(album.getString("trackCount"));
            albumModel.setCopyright(album.getString("copyright"));
            albumModel.setCountry(album.getString("country"));
            albumModel.setCurrency(album.getString("currency"));
            albumModel.setReleaseDate(album.getString("releaseDate"));
            albumModel.setPrimaryGenreName(album.getString("primaryGenreName"));
            albumModel.setCollectionId(album.getString("collectionId"));
            albumModel.setCollectionViewUrl(album.getString("collectionViewUrl"));

            outAlbums.add(albumModel);
        }
    
        Collections.sort(outAlbums, new Comparator<AlbumModel>() {
            @Override
            public int compare(AlbumModel albumModel, AlbumModel t1) {
                return albumModel.getCollectionName().compareToIgnoreCase(t1.getCollectionName());
            }
        });

        return outAlbums;
    }

    private static ArrayList<TrackModel> getTracksFromJsonArray(JSONArray inArray) throws JSONException {
        ArrayList<TrackModel> outTracks = new ArrayList();
        for (int i = 1; i < inArray.length(); i++) {
            JSONObject track = inArray.getJSONObject(i);

            TrackModel trackModel = new TrackModel();
            trackModel.setArtistName(track.getString("artistName"));
            trackModel.setCollectionName(track.getString("collectionCensoredName"));
            trackModel.setTrackName(track.getString("trackName"));

            outTracks.add(trackModel);
        }

        return outTracks;
    }

    public static void setContext(Context context) {
        ITunesApiProcessor.context = context;
    }
}
