package cn.bluesadi.fakedefender.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import cn.bluesadi.fakedefender.network.wrapper.FastJsonRequest;

public class NetworkUtils {


    private final RequestQueue queue;
    private static NetworkUtils instance;

    private NetworkUtils(Context context){
        this.queue = Volley.newRequestQueue(context);
    }

    public static void init(Context context){
        instance = new NetworkUtils(context);
    }

    public static NetworkUtils getInstance() {
        return instance;
    }

    /**
     * 发送POST请求
     *
     * @param url 服务器URL
     * @param parameters POST请求参数
     * @param listener 响应处理
     */
    public void sendPOST(String url, @Nullable JSONObject parameters, Response.Listener<JSONObject> listener) {
        FastJsonRequest fastJsonRequest = new FastJsonRequest(Request.Method.POST, url, parameters, listener,
                Throwable::printStackTrace
        );
        try {
            if(AuthLib.getCookie() != null) {
                fastJsonRequest.getHeaders().put("Cookie", AuthLib.getCookie());
            }
            Log.d("Headers", fastJsonRequest.getHeaders().toString());
        }catch (AuthFailureError e){
            e.printStackTrace();
        }
        fastJsonRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 1, 1.0f));
        queue.add(fastJsonRequest);
    }


    public RequestQueue getQueue() {
        return queue;
    }


}
