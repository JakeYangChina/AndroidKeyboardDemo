package jake.yang.keyboard.library;

import android.app.Activity;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class CoreKeyboardHolder {
    private Activity mActivity;
    private KeyboardStateCallback mCallback;

    CoreKeyboardHolder() {
    }

    public CoreKeyboardHolder setActivity(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    public CoreKeyboardHolder setKeyboardStateCallback(KeyboardStateCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public CoreKeyboardState build() {
        return new CoreKeyboardState(mActivity, mCallback);
    }
}
