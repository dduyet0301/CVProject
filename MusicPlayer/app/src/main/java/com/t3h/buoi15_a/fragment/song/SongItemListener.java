package com.t3h.buoi15_a.fragment.song;

import com.t3h.basemodule.base.AdapterBaseListener;
import com.t3h.buoi15_a.model.Song;

public interface SongItemListener extends AdapterBaseListener {
    void onItemSongClick(Song song);
}
