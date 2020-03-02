package com.t3h.buoi15_a;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.t3h.basemodule.base.ActivityBase;
import com.t3h.basemodule.base.FragmentBase;
import com.t3h.buoi15_a.databinding.ActivityMainBinding;
import com.t3h.buoi15_a.fragment.album.AlbumFragment;
import com.t3h.buoi15_a.fragment.artist.ArtistFragment;
import com.t3h.buoi15_a.fragment.song.SongFragment;
import com.t3h.buoi15_a.service.Mp3Service;

public class MainActivity extends ActivityBase<ActivityMainBinding> implements BottomNavigationView.OnNavigationItemSelectedListener {

    private SongFragment fmSong = new SongFragment();
    private AlbumFragment fmAlbum = new AlbumFragment();
    private ArtistFragment fmArtist = new ArtistFragment();

    private Mp3Service service;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        doRequestPermission(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                new RequestPermissionCallback() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(MainActivity.this, Mp3Service.class);
                        bindService(intent, connection, BIND_AUTO_CREATE);
                    }

                    @Override
                    public void onDenied() {
                        finish();
                    }
                });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Mp3Service.Mp3Binder binder = (Mp3Service.Mp3Binder) iBinder;
            service = binder.getService();
            initFragment();
            binding.nav.setOnNavigationItemSelectedListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fmSong);
        transaction.add(R.id.container, fmAlbum);
        transaction.add(R.id.container, fmArtist);
        transaction.commit();
        showFragment(fmSong);
    }

    private void showFragment(FragmentBase fmShow) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.hide(fmSong);
        transaction.hide(fmAlbum);
        transaction.hide(fmArtist);

        transaction.show(fmShow);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_song:
                showFragment(fmSong);
                break;
            case R.id.nav_album:
                showFragment(fmAlbum);
                break;
            case R.id.nav_artist:
                showFragment(fmArtist);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    public Mp3Service getService() {
        return service;
    }
}
