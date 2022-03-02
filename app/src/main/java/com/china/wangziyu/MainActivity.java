package com.china.wangziyu;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Author: wangziyu
 * Date: 2022/3/2 14:59
 * Description: 主页
 */
public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable net = Observable.just("send lock request to server");
        Observable ble = Observable.just("send lock command to lock");
        Observable hardwareManager = Observable
                .just("ready open lock", "open lock", "open lock fail", "open lock complete")
                .flatMap(s -> {
                    if ("open lock".equals(s)) {
                        return net;
                    } else if ("open lock fail".equals(s)) {
                        return ble;
                    }
                    return Observable.just(s);
                });
        Observer user = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: " + d.isDisposed());
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable t) {
                Log.i(TAG, "onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        };
        hardwareManager.subscribe(user);
    }
}
