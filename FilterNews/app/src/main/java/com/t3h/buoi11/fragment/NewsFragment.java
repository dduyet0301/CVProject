package com.t3h.buoi11.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.buoi11.MainActivity;
import com.t3h.buoi11.R;
import com.t3h.buoi11.adapter.NewsAdapter;
import com.t3h.buoi11.base.BaseFragment;
import com.t3h.buoi11.dao.AppDatabase;
import com.t3h.buoi11.download.DownloadAsync;
import com.t3h.buoi11.download.FileManager;
import com.t3h.buoi11.model.News;
import com.t3h.buoi11.web.WebViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment implements NewsAdapter.NewsItemListener {

    private ArrayList<News> data = new ArrayList<>();
    private RecyclerView lvNews;
    private NewsAdapter adapter;

    private FileManager manager;
//    private File[] files;

    public static final String EXTRA_WEBVIEW = "extra.webview";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvNews = findViewById(R.id.lv_news);
        adapter = new NewsAdapter(getLayoutInflater());
        adapter.setData(data);
        lvNews.setAdapter(adapter);
        adapter.setListener(this);
        manager = new FileManager();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    public String getTitle() {
        return "Tin Tức";
    }

    public void setData(List<News> data) {
        this.data.clear();
        this.data.addAll(data);
        if (adapter != null) {
            adapter.setData(this.data);
        }
    }

    @Override
    public void onNewsItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(EXTRA_WEBVIEW, data.get(position).getUrl());
        startActivity(intent);
        Toast.makeText(getActivity(), "go to webview", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewsItemLongClick(int position) {
        try {
            AppDatabase.getInstance(getContext()).getNewsDao().insert(data.get(position));
            MainActivity act = (MainActivity) getActivity();
            act.getFmSaved().setData();
                String url = data.get(position).getUrl();
                new DownloadAsync().execute(url);
                Toast.makeText(getContext(), getString(R.string.toast_saving), Toast.LENGTH_SHORT).show();
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getActivity(), getString(R.string.toast_already_saved), Toast.LENGTH_SHORT).show();
        }
    }
}
