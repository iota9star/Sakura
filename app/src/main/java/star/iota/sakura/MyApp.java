package star.iota.sakura;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.model.HttpHeaders;
import com.scwang.smartrefresh.header.StoreHouseHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.zzhoujay.richtext.RichText;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;


public class MyApp extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            StoreHouseHeader storeHouseHeader = new StoreHouseHeader(context);
            storeHouseHeader.initWithString(context.getString(R.string.app_name), 28);
            storeHouseHeader.setLineWidth(4);
            storeHouseHeader.setTextColor(0xff5384);
            storeHouseHeader.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.v64dp));
            storeHouseHeader.setDropHeight(context.getResources().getDimensionPixelSize(R.dimen.v128dp));
            return storeHouseHeader;
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> new ClassicsFooter(context));
    }

    public static OkHttpClient makeOkHttpClient() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        return new OkHttpClient.Builder()
                .readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJarImpl(new SPCookieStore(mContext)))
                .hostnameVerifier((s, sslSession) -> true)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initOkGo();
        RichText.initCacheDir(mContext);
        Fabric.with(this, new Crashlytics());
    }

    private void initOkGo() {
        HttpHeaders headers = new HttpHeaders();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 UBrowser/6.1.2716.5 Safari/537.36");
        OkGo.getInstance()
                .init(this)
                .setOkHttpClient(makeOkHttpClient())
                .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3)
                .addCommonHeaders(headers);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RichText.recycle();
    }
}
