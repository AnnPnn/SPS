package granch.sps.graphics;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;

import java.io.IOException;

import granch.sps.R;

public class AlertPlayer {
    private final MediaPlayer mAlertPlayer;
    private final MediaPlayer mWarningPlayer;


    public AlertPlayer(Context context) {
        mAlertPlayer = MediaPlayer.create(context, R.raw.notification);
        mAlertPlayer.setOnPreparedListener(this::onPrepared);
        setSpeed(mAlertPlayer, 2);

        mWarningPlayer = MediaPlayer.create(context, R.raw.notification);
        mWarningPlayer.setOnPreparedListener(this::onPrepared);
        setSpeed(mWarningPlayer, 1);
    }

    private void setSpeed(MediaPlayer player, int speed)
    {
        PlaybackParams params = new PlaybackParams();
        params.setSpeed(speed);
        player.setPlaybackParams(params);
    }

    public void stopPlaying()
    {
        if (mAlertPlayer.isPlaying())
            mAlertPlayer.stop();
        if (mWarningPlayer.isPlaying())
            mWarningPlayer.stop();
    }

    public void  playWarning() throws IOException {
        stopPlaying();
        if (mWarningPlayer.isPlaying()) {
            restartFile(mWarningPlayer);
            return;
        }
        mWarningPlayer.prepare();
    }

    public void  playAlert() throws IOException {
        stopPlaying();
        if (mAlertPlayer.isPlaying()) {
            restartFile(mAlertPlayer);
            return;
        }
        mAlertPlayer.prepare();
    }

    private void restartFile(MediaPlayer player)
    {
        player.pause();
        player.seekTo(0);
        player.start();
    }

    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}
