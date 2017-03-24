package az.wormmark.app.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import az.wormmark.app.WMApp;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Base class for all fragments. Binds views and watches memory leaks
 *
 * @author Emin Yahyayev (yahyayev@iteratia.com)
 * @see ButterKnife
 * @see RefWatcher
 */
public abstract class BaseFragment extends RxFragment {

    private Toast mToast;
    private Unbinder mUnBinder;

    @CallSuper
    @Override public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);
        mUnBinder = ButterKnife.bind(this, view);
    }

    @CallSuper
    @Override public void onDestroyView() {
        mUnBinder.unbind();
        super.onDestroyView();
    }

    @CallSuper
    @Override public void onDestroy() {
        super.onDestroy();
        WMApp.get(getActivity())
                .getRefWatcher()
                .watch(this);
    }

    @SuppressWarnings("unused")
    protected final void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    protected final void showToast(String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, duration);
        mToast.show();
    }

    protected final void logError(Throwable throwable) {
        Timber.e(throwable, throwable.getMessage());
    }
}
