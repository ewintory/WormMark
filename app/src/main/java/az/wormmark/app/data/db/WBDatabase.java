package az.wormmark.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import az.wormmark.app.BookModel;
import az.wormmark.app.R;
import az.wormmark.app.data.model.Book;
import timber.log.Timber;

/**
 * Описание базы данных приложения
 *
 * @author Emin Yahyayev
 */
public final class WBDatabase extends SQLiteOpenHelper {

    /** Версия базы */
    private static final int DATABASE_VERSION = 1;
    /** Название базы */
    private static final String DATABASE_NAME = "wm.db";

    private final Context context;

    public WBDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Book.CREATE_TABLE);

        populate(db);
    }

    private void populate(SQLiteDatabase db) {
        Timber.i("Populating initial data");

        Book.Insert_row insertRow = new BookModel.Insert_row(db);
        insertRow.bind(
                context.getString(R.string.placeholder_book_title),
                context.getString(R.string.placeholder_book_description));
        long bookId = insertRow.program.executeInsert();
        Timber.i("Book %d inserted", bookId);

        Timber.i("Populated initial data");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.i("Upgrading %s from %d to %d", DATABASE_NAME, oldVersion, newVersion);

        deleteDatabase(context);

        onCreate(db);
    }

    private static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
