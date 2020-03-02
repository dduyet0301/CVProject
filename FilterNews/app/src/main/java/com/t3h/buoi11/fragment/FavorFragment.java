package com.t3h.buoi11.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.buoi11.R;
import com.t3h.buoi11.adapter.NewsAdapter;
import com.t3h.buoi11.base.BaseFragment;
import com.t3h.buoi11.dao.AppDatabase;
import com.t3h.buoi11.model.News;
import com.t3h.buoi11.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class FavorFragment extends BaseFragment implements NewsAdapter.NewsItemListener {

    private ArrayList<News> data = new ArrayList<>();
    private RecyclerView lvFavor;
    private NewsAdapter adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvFavor = findViewById(R.id.lv_favor);
        adapter = new NewsAdapter(getLayoutInflater());
        lvFavor.setAdapter(adapter);
        adapter.setListener(this);
        adapter.setData(data);
        setData();
    }

    public void setData() {
        data.clear();
        data.addAll(AppDatabase.getInstance(getContext())
                .getNewsDao().getFavor(true));
        if (adapter != null) {
            adapter.setData(data);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_favor;
    }

    @Override
    public String getTitle() {
        return "Yêu Thích";
    }

    @Override
    public void onNewsItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(NewsFragment.EXTRA_WEBVIEW, data.get(position).getUrl());
        startActivity(intent);
        Toast.makeText(getContext(), getString(R.string.toast_webview), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNewsItemLongClick(final int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), lvFavor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup2, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                News news = data.get(position);
                news.setFavor(false);
                AppDatabase.getInstance(getContext()).getNewsDao().update(news);
                setData();
                Toast.makeText(getActivity(), getString(R.string.delete), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        popupMenu.show();
    }
}
