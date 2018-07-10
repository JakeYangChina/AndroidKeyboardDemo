package jake.yang.keyboard.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import jake.yang.keyboard.library.CoreKeyboardState;
import jake.yang.keyboard.library.KeyboardStateCallback;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private CoreKeyboardState mCoreKeyboardState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoreKeyboardState = CoreKeyboardState.create()
                .setActivity(this)
                .setKeyboardStateCallback(new KeyboardStateCallback() {
                    @Override
                    public void keyboardState(boolean isShow, int keyboardHeight) {
                        if (isShow) {
                            Log.e(TAG, "键盘显示");
                            Log.e(TAG, "键盘高度：" + keyboardHeight);
                        } else {
                            Log.e(TAG, "键盘隐藏");
                        }
                    }

                    @Override
                    public void navigationBarState(boolean isShow) {
                        if (isShow) {
                            Log.e(TAG, "底部导航栏显示");
                        } else {
                            Log.e(TAG, "底部导航栏隐藏");
                        }
                    }
                }).build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCoreKeyboardState != null) {
            mCoreKeyboardState.destroy();
        }
    }
}
