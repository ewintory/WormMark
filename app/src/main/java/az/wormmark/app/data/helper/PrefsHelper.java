package az.wormmark.app.data.helper;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.RxSharedPreferences;

/**
 * Вспомогательный класс для работы с {@link SharedPreferences}
 *
 * @author Emin Yahyayev
 */
public final class PrefsHelper {

    private final SharedPreferences prefs;
    private final RxSharedPreferences rxPrefs;

    public PrefsHelper(SharedPreferences prefs) {
        this.prefs = prefs;
        rxPrefs = RxSharedPreferences.create(prefs);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

}
