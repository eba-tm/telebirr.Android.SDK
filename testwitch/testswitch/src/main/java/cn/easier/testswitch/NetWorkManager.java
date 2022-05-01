package cn.easier.testswitch;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class NetWorkManager {
    private static NetWorkManager mInstance;
    private Retrofit retrofit;
    private static volatile AngolaRequest request = null;

    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化必要对象和参数
     */
    public void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request=chain.request();
                        Request.Builder builder=request.newBuilder();
                        Request req=builder.addHeader("Content-Type","application/json").build();
                        return chain.proceed(req);
                    }
                }).build();
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(AngolaRequest.TEST_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public AngolaRequest getRequest() {
        if (request == null) {
            synchronized (NetWorkManager.class) {
                request = retrofit.create(AngolaRequest.class);
            }
        }
        return request;
    }
}
