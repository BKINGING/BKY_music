package com.example.music_list;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

//这是一个Service服务类
public class MusicService extends Service {
    //声明一个MediaPlayer引用
    private MediaPlayer player;
    //声明一个计时器引用
    private Timer timer;
    private int currentSongIndex = 0; // Add this variable to keep track of the current song index
    private int totalSongs = 5; // Update this variable with the total number of songs
    //构造函数
    public MusicService() {}
    @Override
    public  IBinder onBind(Intent intent){
        return new MusicControl();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        //创建音乐播放器对象
        player=new MediaPlayer();
    }
    //添加计时器用于设置音乐播放器中的播放进度条
    public void addTimer(){
        //如果timer不存在，也就是没有引用实例
        if(timer==null){
            //创建计时器对象
            timer=new Timer();
            TimerTask task=new TimerTask() {
                @Override
                public void run() {
                    if (player==null) return;
                    int duration=player.getDuration();//获取歌曲总时长
                    int currentPosition=player.getCurrentPosition();//获取播放进度
                    Message msg= MusicActivity.handler.obtainMessage();//创建消息对象
                    //将音乐的总时长和播放进度封装至bundle中
                    Bundle bundle=new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentPosition",currentPosition);
                    //再将bundle封装到msg消息对象中
                    msg.setData(bundle);
                    //最后将消息发送到主线程的消息队列
                    MusicActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒（0.5s）执行一次
            timer.schedule(task,5,500);
        }
    }
    private void playSong(int songIndex) {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + "music" + songIndex);
        try {
            player.reset();
            player = MediaPlayer.create(getApplicationContext(), uri);
            player.start();
            currentSongIndex = songIndex;
            addTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 在 MusicService 中添加以下方法
    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    //Binder是一种跨进程的通信方式
    class MusicControl extends Binder{
        // 在 MusicService 中添加以下方法
        public int getCurrentSongIndex() {
            return currentSongIndex;
        }
        public void play(int i){//String path
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/"+"music"+i);
            try{
                //重置音乐播放器
                player.reset();
                //加载多媒体文件
                player=MediaPlayer.create(getApplicationContext(),uri);
                player.start();//播放音乐
                addTimer();//添加计时器
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //下面的暂停继续和退出方法全部调用的是MediaPlayer自带的方法
        public void pausePlay(){
            player.pause();//暂停播放音乐
        }
        public void continuePlay(){
            player.start();//继续播放音乐
        }
        public void seekTo(int progress){
            player.seekTo(progress);//设置音乐的播放位置
        }

        public void playPrevious() {
            if (currentSongIndex > 0) {
                currentSongIndex--;
                playSong(currentSongIndex);
            }
        }

        public void playNext() {
            if (currentSongIndex < totalSongs - 1) {
                currentSongIndex++;
                playSong(currentSongIndex);
            }
        }
    }

    //销毁多媒体播放器
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(player==null) return;
        if(player.isPlaying()) player.stop();//停止播放音乐
        player.release();//释放占用的资源
        player=null;//将player置为空
    }
}

