package az.wormmark.app.ui.books;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxrelay.PublishRelay;

import az.wormmark.app.R;
import az.wormmark.app.data.model.Book;
import az.wormmark.app.ui.GenericAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * Адаптер для списка книг
 *
 * @author Emin Yahyayev
 */
public final class BooksAdapter extends GenericAdapter<Book, BooksAdapter.BookHolder> {

    private final PublishRelay<Book> clickRelay = PublishRelay.create();

    public BooksAdapter(@NonNull Context context) {
        super(context);
    }

    public Observable<Book> getClickObservable() {
        return clickRelay.asObservable();
    }

    @Override
    public BookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookHolder(getInflater().inflate(R.layout.item_book, parent, false));
    }

    @Override
    public void onBindViewHolder(BookHolder holder, int position) {
        final Book book = getItem(position);

        holder.titleView.setText(book.title());
        holder.descriptionView.setText(book.description());
    }

    final class BookHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_title) TextView titleView;
        @BindView(R.id.book_description) TextView descriptionView;

        @BindView(R.id.button_read) View readButton;

        BookHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
