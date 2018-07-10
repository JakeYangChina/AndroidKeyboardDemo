package jake.yang.keyboard.library;

public interface KeyboardStateCallback {
    void keyboardState(boolean isShow, int keyboardHeight);

    void navigationBarState(boolean isShow);
}
