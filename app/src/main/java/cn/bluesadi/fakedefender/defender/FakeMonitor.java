package cn.bluesadi.fakedefender.defender;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import cn.bluesadi.fakedefender.MainActivity;
import cn.bluesadi.fakedefender.R;
import cn.bluesadi.fakedefender.ui.base.BaseActivity;
import cn.bluesadi.fakedefender.util.Log;
import cn.bluesadi.fakedefender.util.ScreenShot;
import cn.bluesadi.fakedefender.util.Settings;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

public class FakeMonitor extends Service {

    private static final String PLACEHOLDER = "--";
    private static final String CHANNEL_ID = "FakeDefender";
    NotificationManager notificationManager;
    private static FakeMonitor instance;
    private long startTime = 0;
    private Handler monitorHandler;
    private ScreenShot screenShot;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        startNotificationForeground();
    }

    public static FakeMonitor getInstance() {
        return instance;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        screenShot = new ScreenShot(this, intent.getParcelableExtra("result_data"));
        screenShot.startScreenShot();
        run();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void tick(){
        long runningTime = (System.currentTimeMillis() - startTime) / 1000;
        long hour = runningTime / 3600;
        long minute = (runningTime % 3600) / 60;
        long second = runningTime % 60;
        if(BaseActivity.getTopActivity() instanceof MainActivity){
            MainActivity activity = (MainActivity) BaseActivity.getTopActivity();
            TextView txRunningHour = activity.findViewById(R.id.var_running_hour);
            TextView txRunningMinute = activity.findViewById(R.id.var_running_minute);
            TextView txRunningSecond = activity.findViewById(R.id.var_running_second);
            txRunningHour.setText(String.valueOf(hour));
            txRunningMinute.setText(String.valueOf(minute));
            txRunningSecond.setText(String.valueOf(second));
        }
        if(runningTime % Settings.period == 0) {
            Bitmap bitmap = screenShot.capture();
            if (bitmap != null) {
                Log.d("Capture");
                FakeChecker.getInstance().check(bitmap, null, false);
            } else {
                Log.d("Null capture");
            }
        }
        monitorHandler.postDelayed(this::tick, Settings.ONE_SECOND);
    }

    public void run(){
        startTime = System.currentTimeMillis();
        monitorHandler = new Handler();
        monitorHandler.postDelayed(this::tick, 0);
    }

    public void stop(){
        screenShot.destroy();
        screenShot = null;
        monitorHandler.removeCallbacksAndMessages(null);
        monitorHandler = null;
        stopSelf();
        if(BaseActivity.getTopActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) BaseActivity.getTopActivity();
            TextView txRunningHour = activity.findViewById(R.id.var_running_hour);
            TextView txRunningMinute = activity.findViewById(R.id.var_running_minute);
            TextView txRunningSecond = activity.findViewById(R.id.var_running_second);
            txRunningHour.setText(PLACEHOLDER);
            txRunningMinute.setText(PLACEHOLDER);
            txRunningSecond.setText(PLACEHOLDER);
        }
    }

    private void startNotificationForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);//设置提示灯
            channel.setLightColor(Color.RED);//设置提示灯颜色
            channel.setShowBadge(true);//显示logo
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(this)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(false)
                    .setContentTitle(Settings.getString(R.string.app_name))//标题
                    .setContentText(Settings.getString(R.string.monitor_running))//内容
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .build();
            startForeground(1, notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了
        }
    }

    public void notifyUser(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Settings.getString(R.string.app_name))
                .setPriority(PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentText(Settings.getString(R.string.warning));

        //设置点击通知之后的响应，启动SettingActivity类
        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(2, notification);
    }

}
