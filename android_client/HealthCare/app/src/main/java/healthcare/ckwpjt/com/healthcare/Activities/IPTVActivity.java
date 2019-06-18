package healthcare.ckwpjt.com.healthcare.Activities;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.solver.Cache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import healthcare.ckwpjt.com.healthcare.R;

public class IPTVActivity extends AppCompatActivity {

    VideoView mVideoView;
    ImageButton mBtnPlayPause;
    ProgressDialog mDialog;

    final String videoURL = "rtsp://192.168.0.11:8080/h264_ulaw.sdp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iptv);

        mVideoView = (VideoView) findViewById(R.id.iptv_surface_view);
        mBtnPlayPause = (ImageButton) findViewById(R.id.iptv_btn_play_pause);
        mBtnPlayPause.setOnClickListener(pauseClickListener);
       }

    View.OnClickListener pauseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog = new ProgressDialog(IPTVActivity.this);
            mDialog.setMessage("Please wait...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();

            try {
                if (!mVideoView.isPlaying()) {
                    Uri uri = Uri.parse(videoURL);
                    mVideoView.setVideoURI(uri);
                    mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mBtnPlayPause.setImageResource(R.drawable.ic_play);
                        }
                    });
                } else {
                    mVideoView.pause();
                    mBtnPlayPause.setImageResource(R.drawable.ic_play);
                }
            } catch (Exception e) {


            }

            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mDialog.dismiss();
                    mp.setLooping(true);

                    mVideoView.start();
                    mBtnPlayPause.setImageResource(R.drawable.ic_pause);
                }
            });
        }
    };

}
