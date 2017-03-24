package az.wormmark.app.ui.books;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqldelight.SqlDelightStatement;

import javax.inject.Inject;

import az.wormmark.app.R;
import az.wormmark.app.WMApp;
import az.wormmark.app.data.model.Book;
import az.wormmark.app.ui.BaseActivity;
import az.wormmark.app.ui.view.BetterViewAnimator;
import az.wormmark.app.util.CollectionUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Экран со списком сохраненных книг
 *
 * @author Emin Yahyayev
 */
public class BooksActivity extends BaseActivity {

    @IdRes private static final int ANIMATOR_VIEW_CONTENT = R.id.recycler_view;
    @IdRes private static final int ANIMATOR_VIEW_EMPTY = R.id.empty_view;
    @IdRes private static final int ANIMATOR_VIEW_PROGRESS = R.id.progress_view;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.view_animator) BetterViewAnimator viewAnimator;

    @Inject BriteDatabase db;

    private BooksAdapter adapter;

    @NonNull @Override
    protected View getSnackbarView() {
        return ButterKnife.findById(this, R.id.main_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        WMApp.get(this).getDataComponent()
                .inject(this);

        setSupportActionBar(toolbar);

        adapter = new BooksAdapter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SqlDelightStatement query = Book.FACTORY.select_all();
        db.createQuery(Book.TABLE_NAME, query.statement)
                .mapToList(Book.MAPPER::map)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(books -> {
                    adapter.setItems(books);
                    safeSetView(CollectionUtils.isEmpty(books)
                            ? ANIMATOR_VIEW_EMPTY : ANIMATOR_VIEW_CONTENT);
                }, throwable -> {
                    Timber.e(throwable);
                    safeSetView(ANIMATOR_VIEW_EMPTY);
                });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toolbar.setTitle(R.string.books_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                showSnackbar("Экран настроек еще не реализован");
                return true;
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle(R.string.about_title)
                        .setMessage(R.string.about_text)
                        .setPositiveButton(android.R.string.ok, (d, i) -> d.dismiss())
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab_add) void onAddBook() {
        showSnackbar("Добавление книги еще не реализовано");
    }

    private void safeSetView(@IdRes int resId) {
        if (viewAnimator != null)
            viewAnimator.setDisplayedChildId(resId);
        else
            Timber.w("viewAnimator==null");
    }
}
