package com.t3h.buoi11.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.buoi11.R;
import com.t3h.buoi11.model.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> data;
    private LayoutInflater inflater;
    private NewsItemListener listener;

    public NewsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setData(ArrayList<News> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setListener(NewsItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_new, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, final int position) {
        final News item = data.get(position);
        holder.bindData(item);

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNewsItemClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onNewsItemLongClick(position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder {

        private ImageView imNews;
        private TextView tvTitle, tvDes, tvPublish;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);

            imNews = itemView.findViewById(R.id.im_news);
            tvTitle = itemView.findViewById(R.id.tv_newsTitle);
            tvPublish = itemView.findViewById(R.id.tv_NewsPublishedTime);
        }

        public void bindData(News item) {
            tvTitle.setText(item.getTitle());
            tvPublish.setText(item.getPublish());
            loadImage(imNews, item.getImg());
        }
    }

    private void loadImage(ImageView im, String link) {
        Glide.with(im)
                .load(link)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(im);
    }

    public interface NewsItemListener {
        void onNewsItemClick(int position);

        void onNewsItemLongClick(int position);
    }
}
