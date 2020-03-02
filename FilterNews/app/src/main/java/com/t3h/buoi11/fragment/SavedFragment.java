package com.t3h.buoi11.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.buoi11.MainActivity;
import com.t3h.buoi11.R;
import com.t3h.buoi11.adapter.NewsAdapter;
import com.t3h.buoi11.base.BaseFragment;
import com.t3h.buoi11.dao.AppDatabase;
import com.t3h.buoi11.download.FileManager;
import com.t3h.buoi11.model.News;
import com.t3h.buoi11.web.WebViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends BaseFragment implements NewsAdapter.NewsItemListener {

    private ArrayList<News> data = new ArrayList<>();
    private RecyclerView lvSaved;
    private NewsAdapter adapter;
    private FileManager manager = new FileManager();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvSaved = findViewById(R.id.lv_saved);
        adapter = new NewsAdapter(getLayoutInflater());
        lvSaved.setAdapter(adapter);
        adapter.setData(data);
        adapter.setListener(this);
        setData();
    }

    public void setData() {
        data.clear();
        data.addAll(AppDatabase.getInstance(getContext())
                .getNewsDao().getNews());
        if (adapter != null) {
            adapter.setData(data);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_saved;
    }

    @Override
    public String getTitle() {
        return "Đã Lưu";
    }

    @Override
    public void onNewsItemClick(int position) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        String fileName = data.get(position).getUrl().replaceAll("[/_.:-]", "");
        String path = manager.getRootPath() + "/Buoi11/" + fileName + ".html";

        if (manager.checkFileExist(path)) {
            String webViewPath = "file:///" + manager.getRootPath() + "/Buoi11/" + fileName + ".html";
            intent.putExtra(NewsFragment.EXTRA_WEBVIEW, webViewPath);
            startActivity(intent);
            Toast.makeText(getContext(), getString(R.string.toast_saved_webview), Toast.LENGTH_SHORT).show();
        } else {
            intent.putExtra(NewsFragment.EXTRA_WEBVIEW, data.get(position).getUrl());
            startActivity(intent);
            Toast.makeText(getContext(), getString(R.string.toast_webview), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNewsItemLongClick(final int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), lvSaved);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuYeuThich:
                        News news = data.get(position);
                        news.setFavor(true);
                        AppDatabase.getInstance(getContext()).getNewsDao().update(news);
                        MainActivity act = (MainActivity) getActivity();
                        act.getFmFavor().setData();
                        Toast.makeText(getContext(), getString(R.string.toast_favor), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuXoa:
                        String fileName = data.get(position).getUrl().replaceAll("[/_.:-]", "");
                        String path = manager.getRootPath() + "/Buoi11/" + fileName + ".html";
                        manager.deleteFile(path);

                        AppDatabase.getInstance(getContext()).getNewsDao().delete(data.get(position));
                        setData();
                        MainActivity act1 = (MainActivity) getActivity();
                        act1.getFmFavor().setData();

                        Toast.makeText(getContext(), getString(R.string.delete), Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
