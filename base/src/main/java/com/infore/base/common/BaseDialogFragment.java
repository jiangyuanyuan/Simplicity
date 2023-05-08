package com.infore.base.common;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public abstract class BaseDialogFragment extends DialogFragment {

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (layoutResId() > 0) {
            rootView = inflater.inflate(layoutResId(), container, false);

        } else if (dialogView() != null){
            rootView = dialogView();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(isCancelable());
            dialog.setCanceledOnTouchOutside(isCancelableOutside());
            dialog.setOnKeyListener((dialog1, keyCode, event) ->
                    keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == KeyEvent.ACTION_DOWN
                            && !isCancelable());
        }


        initial(view);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) return;
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (getAnimRes() > 0) {
            window.setWindowAnimations(getAnimRes());
        }
        WindowManager.LayoutParams params = window.getAttributes();
        if (getDialogWidth() > 0) {
            params.width = getDialogWidth();
        } else {
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        if (getDialogHeight() > 0) {
            params.height = getDialogHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        params.dimAmount = getDimAmount();
        params.gravity = getGravity();
        window.setAttributes(params);
    }

    protected View getRootView() {
        return rootView;
    }

    private boolean isCancelableOutside() {
        return false;
    }

    protected abstract int layoutResId();

    protected abstract View dialogView();

    protected abstract void initial(View view);

    protected int getDialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public float getDimAmount() {
        return 0.2f;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected int getAnimRes() {
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
