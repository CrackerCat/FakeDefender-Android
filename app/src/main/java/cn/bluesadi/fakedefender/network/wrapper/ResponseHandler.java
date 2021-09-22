package cn.bluesadi.fakedefender.network.wrapper;

public interface ResponseHandler<T> {
    void onResponse(T response);
}