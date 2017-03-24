package az.wormmark.app.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import az.wormmark.app.util.CollectionUtils;
import rx.functions.Action1;

/**
 * Базовый адаптер для {@link RecyclerView}
 *
 * @author Emin Yahyayev (yahyayev@iteratia.com)
 */
public abstract class GenericAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements Action1<List<T>> {

    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    protected List<T> items = new ArrayList<>();

    public GenericAdapter(@NonNull Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    protected final LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(items) ? 0 : items.size();
    }

    public void setItems(List<T> items) {
        if (items == null) items = new ArrayList<>();
        this.items = items;
        notifyDataSetChanged();
    }

    public void add(@NonNull T newItem) {
        items.add(newItem);
        notifyItemInserted(items.size());
    }

    public void removeLastNull() {
        for (int i = items.size() - 1; i >= 0; i--) {
            T item = items.get(i);
            if (item == null) {
                items.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public void replaceFirstNull(@NonNull T newItem) {
        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            if (item == null) {
                items.remove(i);
                items.add(i, newItem);
                notifyItemChanged(i);
                return;
            }
        }
        items.add(newItem);
        notifyItemInserted(items.size());
    }

    public void add(@NonNull List<T> newItems) {
        if (!newItems.isEmpty()) {
            int currentSize = items.size();
            int amountInserted = newItems.size();

            items.addAll(newItems);
            notifyItemRangeInserted(currentSize, amountInserted);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public void call(@Nullable List<T> newItems) {
        if (CollectionUtils.isEmpty(newItems))
            setItems(new ArrayList<>());
        else
            setItems(newItems);
    }

    @NonNull
    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    public T getItem(int position) {
        return !CollectionUtils.isEmpty(items) ? items.get(position) : null;
    }
}
