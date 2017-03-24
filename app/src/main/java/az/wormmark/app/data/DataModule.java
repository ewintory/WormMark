package az.wormmark.app.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import az.wormmark.app.BuildConfig;
import az.wormmark.app.data.db.WBDatabase;
import az.wormmark.app.data.helper.PrefsHelper;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Зависимости по работе c данными
 *
 * @author Emin Yahyayev
 */
@Module
public final class DataModule {

    private static final int DISK_CACHE_SIZE = 100 * 1024 * 1024; // 100MB
    private static final int CONNECTION_TIMEOUT_SECONDS = 20;

    public DataModule() {}

    @Provides
    @Singleton SharedPreferences sharedPreferences(final Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton PrefsHelper prefsHelper(final SharedPreferences preferences) {
        return new PrefsHelper(preferences);
    }

    @Provides
    @Singleton Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                //.registerTypeAdapterFactory(CustomAdapterFactory.create())
                .create();
    }

    @Provides
    @Singleton Cache okHttpCache(final Application application) {
        File cacheDir = new File(application.getCacheDir(), "http");
        return new Cache(cacheDir, DISK_CACHE_SIZE);
    }

    @Provides
    @Singleton OkHttpClient okHttpClient(final Cache cache) {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        builder.addInterceptor(logging);
        builder.cache(cache);
        return builder.build();
    }

    @Provides
    @Singleton SQLiteOpenHelper openHelper(final Application application) {
        return new WBDatabase(application);
    }

    @Provides
    @Singleton SqlBrite sqlBrite() {
        return new SqlBrite.Builder()
                .logger(message -> Timber.tag("Database").v(message))
                .build();
    }

    @Provides
    @Singleton BriteDatabase database(final SqlBrite sqlBrite, final SQLiteOpenHelper helper) {
        final BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
        db.setLoggingEnabled(true);
        return db;
    }
}
