package com.moudle.savehumen.savehumen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Response;
import com.moudle.savehumen.BuildConfig;
import com.moudle.savehumen.R;

import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class SplashActivity extends Activity {

    /**
     * 我们自己后台的
     */
    final String OWNER_SWITCH_URL = "http://tddiosad.hihere.com.cn/web/user/getAppMsg/";

    /**
     * 应用的appid
     */
    final String APP_ID = "db77c5a8e8614cdb82cfaa8e8698a4c7";

    private SplashLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashLayout = findViewById(R.id.splash_layout);
        splashLayout.attach(this).playAnim();
    }

    public void loadData() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE); //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO); //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor); //添加OkGo默认debug日志
        OkGo.getInstance().init(getApplication())                       //必须调用初始化
                .setOkHttpClient(builder.build());
        OkGo.<String>get(OWNER_SWITCH_URL + APP_ID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        JSONObject o = JSON.parseObject(response.body());
                        int status = o.getIntValue("status");
                        if (status == 0) {
                            JSONObject o1 = o.getJSONObject("result");
                            String vs = o1.getString("vs");
                            String url = o1.getString("url");
                            String ud = o1.getString("ud");
                            if (vs.equals("4")) {
                                Intent intent = new Intent(SplashActivity.this, MyWebViewActivity.class);
                                intent.putExtra("data_one", url);
                                startActivity(intent);
                            } else if (vs.equals("5")) {
                                Intent intent = new Intent(SplashActivity.this, DownApkActivity.class);
                                intent.putExtra("data_one", ud);
                                startActivity(intent);
                            } else {
                                goNormal();
                            }
                        } else {
                            goNormal();
                        }
                        finish();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        goNormal();
                        finish();
                    }
                });
    }

    public void goNormal() {
        Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
