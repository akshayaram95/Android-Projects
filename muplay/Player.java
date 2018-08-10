
package com.example.akshaya.muplay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    SeekBar sb;
    Button b_pp,b_p,b_n,b_ff,b_fb;
    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        b_p = (Button) findViewById(R.id.b_p);
        b_fb = (Button) findViewById(R.id.b_fb);
        b_pp = (Button) findViewById(R.id.b_pp);
        b_ff = (Button) findViewById(R.id.b_ff);
        b_n = (Button) findViewById(R.id.b_n);

        b_p.setOnClickListener(this);
        b_fb.setOnClickListener(this);
        b_pp.setOnClickListener(this);
        b_ff.setOnClickListener(this);
        b_n.setOnClickListener(this);
        sb = (SeekBar) findViewById(R.id.seekBar);

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                int td = mp.getDuration();
                int cp=0;
                while(cp<td){
                    try{
                        sleep(500);
                        cp=mp.getCurrentPosition();
                        sb.setProgress(cp);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mp!=null)
        {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekbar.start();


        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch(id)
        {
            case R.id.b_pp:
                if(mp.isPlaying()){
                    mp.pause();
                    b_pp.setText("Play");
                }
                else
                {
                    mp.start();
                    b_pp.setText("Pause");
                }
                break;
            case R.id.b_ff:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.b_fb:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.b_n:
                mp.stop();
                mp.release();
                Uri u = Uri.parse(mySongs.get((position+1)%mySongs.size()).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.b_p:
                mp.stop();
                mp.release();
                position = position-1<0?mySongs.size()-1:position-1;
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

        }
    }
}
