package net.onest.timestoryprj.util;

import android.content.Context;
import android.media.AudioManager;

public class AudioUtil {
    private AudioManager audioManager;
    private static AudioUtil mInstance;

    private AudioUtil(Context context){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public synchronized static AudioUtil getInstance(Context context){
        if (mInstance == null){
            mInstance = new AudioUtil(context);
        }
        return mInstance;
    }
    //获取多媒体最大音量
    public int getMediaMaxVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
    //获取多媒体音量
    public int getMediaVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    //获取通话最大音量
    public int getCallMaxVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
    }
    //获取系统音量最大值
    public int getSystemMaxVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }
    //获取系统音量
    public int getSystemVolume(){
        return audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }
    //获取提示音量最大值
    public int getAlermMaxVolume(){
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
    }
    //设置多媒体音量
    public void setMediaVolume(int volume){
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,//音量类型
                volume,
                AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
    }

}
