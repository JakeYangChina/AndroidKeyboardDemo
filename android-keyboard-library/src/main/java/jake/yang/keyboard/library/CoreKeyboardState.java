package jake.yang.keyboard.library;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;
import android.view.Window;

import java.lang.ref.SoftReference;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class CoreKeyboardState {
    private boolean mIsHidden = false;
    private final int NAVIGATION_BAR_STATE_OPEN = 1;
    private final int NAVIGATION_BAR_STATE_CLOSE = 2;
    private final int KEYBOARD_STATE_OPEN = 3;
    private final int KEYBOARD_STATE_CLOSE = 4;
    private int mCurrentNavigationBarState = 0;
    private int mPreNavigationBarState = 0;
    private int mCurrentKeyboardState = 0;
    private int mPreKeyboardState = 0;
    private int mKeyboardHeight = 0;
    private SoftReference<Activity> mActivitySoftReference;
    private SoftReference<Window> mWindowSoftReference;
    private SoftReference<ViewTreeObserver.OnGlobalLayoutListener> mGlobalLayout;
    private KeyboardStateCallback mCallback;

    CoreKeyboardState(Activity activity, KeyboardStateCallback callback) {
        this.mCallback = callback;
        mActivitySoftReference = new SoftReference<>(activity);
        mWindowSoftReference = new SoftReference<>(activity.getWindow());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Window window = mWindowSoftReference.get();
        globalLayoutListener(window, displayMetrics);
    }

    private void globalLayoutListener(final Window window, final DisplayMetrics displayMetrics) {
        ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                globalLayout(window, displayMetrics);
            }
        };

        window.getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(globalLayoutListener);
        mGlobalLayout = new SoftReference<>(globalLayoutListener);
    }

    private void globalLayout(Window window, DisplayMetrics displayMetrics) {
        Rect rect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        int dis = rect.bottom - rect.top;
        int height = window.getDecorView().getHeight();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        if (dis > height / 3 && dis < height - getNavigationBarHeight() - getStateBarHeight()) {
            mIsHidden = true;
            mCurrentKeyboardState = KEYBOARD_STATE_OPEN;
            mKeyboardHeight = (displayMetrics.heightPixels - getStateBarHeight() - dis);
            if (displayMetrics.heightPixels == window.getDecorView().getHeight()) {
                mCurrentNavigationBarState = NAVIGATION_BAR_STATE_CLOSE;
            } else if (displayMetrics.heightPixels == window.getDecorView().getHeight() - getNavigationBarHeight()) {
                mCurrentNavigationBarState = NAVIGATION_BAR_STATE_OPEN;
            }

        } else if (mIsHidden && dis >= height - getNavigationBarHeight() - getStateBarHeight()) {
            mIsHidden = false;
            mCurrentKeyboardState = KEYBOARD_STATE_CLOSE;
            if (displayMetrics.heightPixels == window.getDecorView().getHeight()) {
                mCurrentNavigationBarState = NAVIGATION_BAR_STATE_CLOSE;
            } else if (displayMetrics.heightPixels == window.getDecorView().getHeight() - getNavigationBarHeight()) {
                mCurrentNavigationBarState = NAVIGATION_BAR_STATE_OPEN;
            }
        }

        if (displayMetrics.heightPixels == window.getDecorView().getHeight()) {
            mCurrentNavigationBarState = NAVIGATION_BAR_STATE_CLOSE;
        } else if (displayMetrics.heightPixels == window.getDecorView().getHeight() - getNavigationBarHeight()) {
            mCurrentNavigationBarState = NAVIGATION_BAR_STATE_OPEN;
        }

        if (mCallback != null && mCurrentKeyboardState != mPreKeyboardState) {
            switch (mCurrentKeyboardState) {
                case KEYBOARD_STATE_OPEN:
                    mCallback.keyboardState(true, mKeyboardHeight);
                    break;
                case KEYBOARD_STATE_CLOSE:
                    mCallback.keyboardState(false, 0);
                    break;
                default:
                    break;
            }
        }

        if (mCallback != null && mCurrentNavigationBarState != mPreNavigationBarState) {
            switch (mCurrentNavigationBarState) {
                case NAVIGATION_BAR_STATE_OPEN:
                    mCallback.navigationBarState(true);
                    break;
                case NAVIGATION_BAR_STATE_CLOSE:
                    mCallback.navigationBarState(false);
                    break;
                default:
                    break;
            }
        }
        mPreKeyboardState = mCurrentKeyboardState;
        mPreNavigationBarState = mCurrentNavigationBarState;
    }

    private int getNavigationBarHeight() {
        if (mActivitySoftReference.get() == null) {
            throw new RuntimeException("mActivity is null.");
        }
        Resources resources = mActivitySoftReference.get().getResources();
        int height = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    private int getStateBarHeight() {
        if (mActivitySoftReference.get() == null) {
            throw new RuntimeException("mActivity is null.");
        }
        Resources resources = mActivitySoftReference.get().getResources();
        int height = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static CoreKeyboardHolder create() {
        return new CoreKeyboardHolder();
    }

    public void destroy() {
        if (mWindowSoftReference.get() != null && mGlobalLayout.get() != null) {
            mWindowSoftReference.get().getDecorView()
                    .getViewTreeObserver()
                    .removeOnGlobalLayoutListener(mGlobalLayout.get());
            mWindowSoftReference.clear();
            mGlobalLayout.clear();
        }

        if (mActivitySoftReference != null) {
            mActivitySoftReference.clear();
        }
    }
}
