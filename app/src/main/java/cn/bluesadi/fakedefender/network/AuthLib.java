package cn.bluesadi.fakedefender.network;

import com.alibaba.fastjson.JSONObject;
import cn.bluesadi.fakedefender.network.wrapper.BaseResponse;
import cn.bluesadi.fakedefender.network.wrapper.ResponseHandler;
import cn.bluesadi.fakedefender.util.Settings;

public class AuthLib {

    private static String cookie = null;

    /**
     * 向服务器端发送登录请求
     *
     * @param email 用户电话号码
     * @param password 密码
     * @param handler 响应处理
     *
     * Response example: {"status": 1}
     * usage:
     *   authController.sendLoginRequest("123456", "password", response -> {
     *      //do something with response {@link BaseResponse}
     *   });
     */
    public static void sendLoginRequest(String email, String password, ResponseHandler<BaseResponse> handler){
        handler.onResponse(new BaseResponse(true));
//        JSONObject parameters = new JSONObject();
//        parameters.put("email", email);
//        parameters.put("password", password);
//        NetworkUtils.getInstance().sendPOST(Settings.serverURL + "/user/login",
//                parameters,
//                response -> {
//                    BaseResponse bResponse = new BaseResponse(response);
//                    if(bResponse.getSession() != null){
//                        cookie = bResponse.getSession();
//                    }
//                    handler.onResponse(bResponse);
//                }
//        );
    }

    /**
     * 向服务器端发送注册请求
     *
     * @param email 用户电话号码
     * @param password 密码
     * @param handler 响应处理
     *
     * Response example: {"status": 1}
     */
    public static void sendRegisterRequest(String email, String password, ResponseHandler<BaseResponse> handler){
        handler.onResponse(new BaseResponse(true));
//        JSONObject parameters = new JSONObject();
//        parameters.put("email", email);
//        parameters.put("password", password);
//        NetworkUtils.getInstance().sendPOST(Settings.serverURL + "/user/register",
//                parameters,
//                response -> handler.onResponse(new BaseResponse(response))
//        );
    }

    public static String getCookie() {
        return cookie;
    }

}
