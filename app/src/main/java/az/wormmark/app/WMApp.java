package az.wormmark.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import az.wormmark.app.data.DaggerDataComponent;
import az.wormmark.app.data.DataComponent;
import az.wormmark.app.data.DataModule;
import az.wormmark.app.util.logging.DebugTree;
import az.wormmark.app.util.logging.ReleaseTree;
import timber.log.Timber;

/**
 * Точка входа в приложение
 *
 * @author Emin Yahyayev
 */
public final class WMApp extends Application {

    private DataComponent dataComponent;
    private RefWatcher refWatcher;

    public static WMApp get(Context context) {
        return (WMApp) context.getApplicationContext();
    }

    @Override
    public final void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }

        refWatcher = BuildConfig.DEBUG
                ? LeakCanary.install(this)
                : RefWatcher.DISABLED;

        setupDagger();
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public DataComponent getDataComponent() {
        return dataComponent;
    }

    private void setupDagger() {
        final AppModule appModule = new AppModule(this);
        final DataModule dataModule = new DataModule();

        dataComponent = DaggerDataComponent.builder()
                .appModule(appModule)
                .dataModule(dataModule)
                .build();
    }
}
