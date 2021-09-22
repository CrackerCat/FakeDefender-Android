package cn.bluesadi.fakedefender.defender;

import android.graphics.Bitmap;
import android.util.Base64;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import cn.bluesadi.fakedefender.network.NetworkUtils;
import cn.bluesadi.fakedefender.ui.adapter.RecordListAdapter;
import cn.bluesadi.fakedefender.util.Settings;

public class FakeChecker {

    private static final FakeChecker INSTANCE = new FakeChecker();
    private static final int MAX_RECORD_LIST_SIZE = 200;
    private final List<CheckRecord> recordList;

    private FakeChecker(){
        recordList = new LinkedList<>();
    }

    private JSONObject buildPacket(Bitmap bitmap){
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            String base64 = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
            JSONObject packet = new JSONObject();
            packet.put("image", base64);
            return packet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void check(Bitmap bitmap, @Nullable Response.Listener<JSONObject> listener, boolean manual){
        JSONObject packet = buildPacket(bitmap);
        Date checkTime = new Date();
        NetworkUtils.getInstance().sendPOST("http://10.136.126.13:5000/predict", packet, response -> {
            addRecord(new CheckRecord(bitmap, response, checkTime, manual));
            RecordListAdapter.getInstance().notifyDataSetChanged();
            notifyIfNeed();
            if(listener != null) {
                listener.onResponse(response);
            }
        });
    }

    public static FakeChecker getInstance() {
        return INSTANCE;
    }

    public void addRecord(CheckRecord record){
        if(recordList.size() >= MAX_RECORD_LIST_SIZE){
            recordList.remove(recordList.size() - 1);
        }
        recordList.add(0, record);
    }

    public CheckRecord getLastRecord() {
        if(!recordList.isEmpty()){
            return recordList.get(0);
        }
        return null;
    }

    public List<CheckRecord> getRecordList() {
        return recordList;
    }

    public CheckRecord getRecord(int index){
        return recordList.get(index);
    }

    public void notifyIfNeed(){
        int fakeNum = 0;
        int cnt = 0;
        for(int i = 0;i < recordList.size() && cnt < 10;i ++){
            CheckRecord record = recordList.get(i);
            if(!record.isManual()) {
                System.out.println(fakeNum);
                cnt++;
                if (recordList.get(i).getHighestScore() >= Settings.threshold) {
                    fakeNum++;
                    if(fakeNum >= 4){
                        System.out.println("notify");
                        FakeMonitor.getInstance().notifyUser();
                    }
                }
            }
        }
    }

}
