package com.t3h.buoi11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.t3h.buoi11.adapter.NewsPagerAdapter;
import com.t3h.buoi11.api.ApiBuilder;
import com.t3h.buoi11.dao.AppDatabase;
import com.t3h.buoi11.download.FileManager;
import com.t3h.buoi11.fragment.FavorFragment;
import com.t3h.buoi11.fragment.NewsFragment;
import com.t3h.buoi11.fragment.SavedFragment;
import com.t3h.buoi11.model.News;
import com.t3h.buoi11.model.NewsResponse;
import com.t3h.buoi11.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Callback<NewsResponse> {

    private static final int REQUEST_PERMISSIONS = 123;
    private TabLayout tab;
    private ViewPager pager;
    private NewsPagerAdapter adapter;
    private NewsFragment fmNews = new NewsFragment();
    private SavedFragment fmSaved = new SavedFragment();
    private FavorFragment fmFavor = new FavorFragment();
    private Dialog dialog;
    private SearchView searchView;
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public SavedFragment getFmSaved() {
        return fmSaved;
    }

    public FavorFragment getFmFavor() {
        return fmFavor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (PermissionUtils.checkPermissions(this, PERMISSIONS)) {
            initViews();
        } else
            PermissionUtils.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
    }

    private void initViews() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);
        adapter = new NewsPagerAdapter(
                getSupportFragmentManager(),
                fmNews, fmSaved, fmFavor);
        pager.setAdapter(adapter);
        tab.setupWithViewPager(pager);
        // TODO
        List<News> data = AppDatabase.getInstance(this)
                .getNewsDao().getNews();
        fmNews.setData(data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean result = PermissionUtils.checkPermissions(this, permissions);
        if (result) {
            initViews();
        } else
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView(); //androidx
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        dialog.show();
        pager.setCurrentItem(0);
        ApiBuilder.getInstance().searchNews(query,
                "85f83e2e0cba470facd96db9a243719f",
                "vi")
                .enqueue(this);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
        NewsResponse newsResponse = response.body();
        ArrayList<News> data = newsResponse.getArticles();
        fmNews.setData(data);
        News[] news = new News[data.size()];
        data.toArray(news);
        AppDatabase.getInstance(this).getNewsDao().update(news);
        dialog.dismiss();
    }

    @Override
    public void onFailure(Call<NewsResponse> call, Throwable t) {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
    }
}
