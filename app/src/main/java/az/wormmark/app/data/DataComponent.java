package az.wormmark.app.data;


import javax.inject.Singleton;

import az.wormmark.app.AppModule;
import az.wormmark.app.ui.books.BooksActivity;
import dagger.Component;

/**
 * Инджектор зависимостей для {@link DataModule}
 *
 * @author Emin Yahyayev
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class})
public interface DataComponent {

    void inject(BooksActivity activity);

}
