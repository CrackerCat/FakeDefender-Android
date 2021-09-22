package cn.bluesadi.fakedefender.network.wrapper;

import android.util.Log;
import androidx.annotation.Nullable;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class FastJsonRequest extends JsonRequest<JSONObject> {

    public FastJsonRequest(
            int method,
            String url,
            @Nullable JSONObject jsonRequest,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(
                method,
                url,
                (jsonRequest == null) ? null : jsonRequest.toJSONString(),
                listener,
                errorListener);
    }

    public FastJsonRequest(
            String url,
            @Nullable JSONObject jsonRequest,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) {
        this(
                jsonRequest == null ? Method.GET : Method.POST,
                url,
                jsonRequest,
                listener,
                errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            JSONObject jsonObject = JSON.parseObject(jsonString);
            if(response.headers.containsKey("Set-Cookie")) {
                jsonObject.put("session", response.headers.get("Set-Cookie")
                        .replaceFirst("; HttpOnly.*", ""));
            }
            Log.d("Response", jsonObject.toJSONString());
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}