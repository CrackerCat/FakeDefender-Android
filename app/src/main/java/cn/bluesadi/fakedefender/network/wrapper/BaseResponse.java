package cn.bluesadi.fakedefender.network.wrapper;

import com.alibaba.fastjson.JSONObject;

public class BaseResponse {

    private boolean success = false;
    private String session = null;

    public BaseResponse(boolean success){
        this.success = success;
    }

    /**
     * 判断是否为合法的响应
     *
     * @param jsonObject 响应的JSON数据
     * @param keys 必须包含的键
     * */
    private boolean containsKeys(JSONObject jsonObject, String... keys){
        for(String key : keys){
            if(!jsonObject.containsKey(key)){
                return false;
            }
        }
        return true;
    }

    public BaseResponse(JSONObject jsonObject){
        if(jsonObject != null && containsKeys(jsonObject, "status")) {
            this.success = jsonObject.getInteger("status") == 1;
        }
        if(jsonObject.containsKey("session")){
            this.session = jsonObject.getString("session");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSession() {
        return session;
    }
}
