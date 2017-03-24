package az.wormmark.app.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;

import az.wormmark.app.BookModel;

/**
 * Сущность для книги
 *
 * @author Emin Yahyayev
 */
@AutoValue
public abstract class Book implements BookModel, Parcelable {

    public static final BookModel.Factory<Book> FACTORY =
            new BookModel.Factory<>(AutoValue_Book::new);

    public static final RowMapper<Book> MAPPER = FACTORY.select_allMapper();

    public static final RowMapper<Book> MAPPER_BY_ID = FACTORY.select_by_idMapper();

}
