package az.wormmark.app.util.logging;

import timber.log.Timber;

/**
 * Логгирование для debug сборки приложения
 *
 * @author Emin Yahyayev
 */
public class DebugTree extends Timber.DebugTree {

    // Adds the line number to the tag
    @Override
    protected String createStackElementTag(StackTraceElement element) {
        return super.createStackElementTag(element) + ":" + element.getLineNumber();
    }

}
