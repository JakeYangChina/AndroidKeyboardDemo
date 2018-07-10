# android-keyboard-library
监听软键盘显示和隐藏，获取软键盘高度，部分手机可以设置显示或隐藏底部导航栏，此库可以监听底部导航栏显示和隐藏

## API说明：CoreKeyboardState类
#### ```public static CoreKeyboardHolder create()```
作用：获取类CoreKeyboardHolder，进行设置参数
#### ```public void destroy()```
作用：释放内存空间，防止内存泄漏，建议关闭页面时调用
<br>
## API说明：CoreKeyboardHolder类，主要是设置参数
#### ```public CoreKeyboardHolder setActivity(Activity activity)```
作用：设置当前activity
作用：获取状态栏高度
#### ```public CoreKeyboardHolder setKeyboardStateCallback(KeyboardStateCallback callback)```
作用：设置监听类，获取显示状态
#### ```public CoreKeyboardState build()```
作用：返回CoreKeyboardState类对象
<br>
#### 使用方法
```
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
```
```
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCoreKeyboardState != null) {
            mCoreKeyboardState.destroy();//释放内存空间，防止内存泄漏，建议关闭页面时调用
        }
    }
```

<br>
<hr>
<br>

## 从github clone 代码到本地放到AS后，发现并不能点“Run”键运行app，当强制点击运行app，会弹出窗口，在最下方提示Error:Please select Android SDK。解决办法如下：
解决办法：在Android Studio内找到File --> Project Structure 选中app，再点击右侧上方 Properties 修改Build Tools Version版本即可
