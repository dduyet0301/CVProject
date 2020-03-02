package com.t3h.buoi15_a.fragment.song;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.t3h.basemodule.base.FragmentBase;
import com.t3h.basemodule.utils.ToastUtils;
import com.t3h.buoi15_a.MainActivity;
import com.t3h.buoi15_a.adapter.AppAdapter;
import com.t3h.buoi15_a.R;
import com.t3h.buoi15_a.controller.MediaController;
import com.t3h.buoi15_a.data.SystemData;
import com.t3h.buoi15_a.databinding.FragmentSongBinding;
import com.t3h.buoi15_a.model.Song;
import com.t3h.buoi15_a.service.Mp3Service;

public class SongFragment extends FragmentBase<FragmentSongBinding> implements SongItemListener {

    private AppAdapter<Song> adapter;
    private SystemData data;
    //    private MediaController controller;
    private Mp3Service service;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new AppAdapter<>(getLayoutInflater(), R.layout.item_song);
        data = new SystemData(getContext());
        binding.lvSong.setAdapter(adapter);
        adapter.setData(data.readData());
        adapter.setListener(this); //implement SongItemListener

        MainActivity act = (MainActivity) getActivity();
//        act.getService().setSongData(adapter.getData());
//        controller = act.getService().getController();
        service = act.getService();
    }

    @Override
    public void onItemSongClick(Song song) {
        int index = adapter.getData().indexOf(song);
        service.setSongData(adapter.getData());
//        controller.create(index);
        service.getController().create(index);
    }
}
