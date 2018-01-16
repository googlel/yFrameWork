package com.liy.today.network;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/19
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class RestRetrofit {

    private static HttpClient mClient;
    private static RxThreadCallAdapater mThreadAdapter;
    private static String mBaseApiUrl;

    /**
     * get Restrofit Instance
     *
     * @param context context
     * @return Instance
     */
    public static void init(Context context, String baseApiUrl) {
        mClient = HttpClient.getInstance(context);
        mThreadAdapter = RxThreadCallAdapater.create(Schedulers.io()
                , AndroidSchedulers.mainThread());
        mBaseApiUrl = baseApiUrl;
    }

    /**
     * getHttpClient
     *
     * @return httpclient
     */
    public static HttpClient getHttpClient() {
        return mClient;
    }

    /**
     * 获得当前Bean
     *
     * @param beanClass beanClass
     * @param <T>       <T>
     * @return Bean
     */
    public static <T> T getBeanOfClass(Class<T> beanClass) {
        if (null == beanClass) {
            throw new IllegalArgumentException("");
        }

        String baseUrl = mBaseApiUrl;
        ApiUrl apiUrl = beanClass.getAnnotation(ApiUrl.class);
        if (null != apiUrl && !TextUtils.isEmpty(apiUrl.value())) {
            baseUrl = apiUrl.value();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(mThreadAdapter)
                .client(mClient.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        return retrofit.create(beanClass);
    }

    /**
     * 获取缓存
     *
     * @return cache
     */
    public static ObjectCache getObjectCache() {
        return mClient.getObjectCache();
    }

    /**
     * RxThreadCallAdapater
     */
    public static final class RxThreadCallAdapater extends CallAdapter.Factory {

        RxJavaCallAdapterFactory rxFactory = RxJavaCallAdapterFactory.create();
        private Scheduler subscribeScheduler;
        private Scheduler observerScheduler;

        /**
         * RxThreadCallAdapater
         *
         * @return RxThreadCallAdapater
         */
        public static RxThreadCallAdapater create(Scheduler subscribeScheduler
                , Scheduler observerScheduler) {
            return new RxThreadCallAdapater(subscribeScheduler, observerScheduler);
        }

        /**
         * RxThreadCallAdapater
         *
         * @param subscribeScheduler subscribeScheduler
         * @param observerScheduler  observerScheduler
         */
        public RxThreadCallAdapater(Scheduler subscribeScheduler, Scheduler observerScheduler) {
            this.subscribeScheduler = subscribeScheduler;
            this.observerScheduler = observerScheduler;
        }

        @Override
        public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            CallAdapter<Observable<?>> callAdapter =
                    (CallAdapter<Observable<?>>) rxFactory.get(returnType, annotations, retrofit);
            return callAdapter != null ? new ThreadCallAdapter(callAdapter) : null;
        }

        /**
         * ThreadCallAdapter
         */
        final class ThreadCallAdapter implements CallAdapter<Observable<?>> {
            CallAdapter<Observable<?>> delegateAdapter;

            ThreadCallAdapter(CallAdapter<Observable<?>> delegateAdapter) {
                this.delegateAdapter = delegateAdapter;
            }

            @Override
            public Type responseType() {
                return delegateAdapter.responseType();
            }

            @Override
            public <T> Observable<?> adapt(Call<T> call) {
                return delegateAdapter.adapt(call)
                        .subscribeOn(subscribeScheduler)
                        .observeOn(observerScheduler);
            }
        }
    }

}
