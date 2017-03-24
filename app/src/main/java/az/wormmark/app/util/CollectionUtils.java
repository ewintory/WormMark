package az.wormmark.app.util;

import android.support.annotation.Nullable;

import java.util.Collection;


/**
 * Утилиты для работы с коллекциями
 *
 * @author Emin Yahyayev
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new AssertionError("No instances.");
    }

    public static int safeSize(@Nullable Collection collection) {
        return (collection != null) ? collection.size() : 0;
    }

    public static boolean isEmpty(@Nullable Collection collection) {
        return (collection == null) || collection.isEmpty();
    }

    public static <T> boolean isEmpty(@Nullable T[] array) {
        return (array == null) || (array.length == 0);
    }
}
