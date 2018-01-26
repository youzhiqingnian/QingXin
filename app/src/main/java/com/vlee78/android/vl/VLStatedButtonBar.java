package com.vlee78.android.vl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class VLStatedButtonBar extends LinearLayout implements View.OnClickListener {
    public interface VLStatedButtonBarDelegate {
        void onStatedButtonBarCreated(VLStatedButtonBar buttonBar);

        void onStatedButtonBarChanged(VLStatedButtonBar buttonBar, int offset);
    }

    private List<VLStatedButton> mButtons;
    private VLStatedButtonBarDelegate mDelegate;
    private int mOffset;

    public VLStatedButton getButton(int offset) {
        return mButtons.get(offset);
    }

    public VLStatedButtonBar(Context context) {
        this(context, null, 0);
    }

    public VLStatedButtonBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLStatedButtonBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        mButtons = new ArrayList<>();
        mDelegate = null;
        mOffset = -1;
    }

    public void addStatedButton(VLStatedButton button) {
        button.setLayoutParams(VLUtils.paramsLinear(0, VLUtils.MATCH_PARENT, 1));
        button.setOnClickListener(this);
        mButtons.add(button);
        addView(button);
    }

    public void addStatedButton(VLStatedButton button, LayoutParams params) {
        button.setLayoutParams(params);
        button.setOnClickListener(this);
        mButtons.add(button);
        addView(button);
    }

    public void addStatedView(View view, float weight) {
        view.setLayoutParams(VLUtils.paramsLinear(0, VLUtils.MATCH_PARENT, weight));
        addView(view);
    }

    public void addStatedView(View view, LayoutParams params) {
        view.setLayoutParams(params);
        addView(view);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mButtons.size(); i++) {
            if (mButtons.get(i) == v) {
                setChecked(i);
                break;
            }
        }
    }

    public void setChecked(int offset) {
        if (offset < 0)
            offset = 0;
        if (offset >= mButtons.size())
            offset = offset % mButtons.size();
        if (mOffset == offset)
            return;
        for (int i = 0; i < mButtons.size(); i++) {
            VLStatedButton button = mButtons.get(i);
            if (i == offset) {
                button.setButtonState(VLStatedButton.VLButtonState.StateChecked);
                mOffset = i;
                if (mDelegate != null)
                    mDelegate.onStatedButtonBarChanged(this, mOffset);
            } else
                button.setButtonState(VLStatedButton.VLButtonState.StateNormal);
        }
    }

    public void setStatedButtonBarDelegate(VLStatedButtonBarDelegate delegate) {
        mButtons.clear();
        removeAllViews();
        mOffset = -1;
        mDelegate = delegate;
        mDelegate.onStatedButtonBarCreated(this);
    }

    public void update() {
        int offset = mOffset;
        mButtons.clear();
        removeAllViews();
        mOffset = -1;
        if (mDelegate != null)
            mDelegate.onStatedButtonBarCreated(this);
        if (offset >= mButtons.size())
            offset = mButtons.size() - 1;
        if (offset >= 0)
            setChecked(offset);
    }

    public static class VLStatedButton extends FrameLayout {
        private VLButtonState mButtonState;
        private int mUserState;
        private VLStatedButtonDelegate mDelegate;

        public interface VLStatedButtonDelegate {
            void onStatedButtonCreated(VLStatedButton button, LayoutInflater inflater);

            void onStatedButtonChanged(VLStatedButton button, VLButtonState buttonState, int userState);
        }

        public enum VLButtonState {
            StateNormal, StateHoverd, StatePressed, StateChecked,
        }

        public VLStatedButton(Context context) {
            this(context, null, 0);
        }

        public VLStatedButton(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public VLStatedButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            mButtonState = VLButtonState.StateNormal;
            mUserState = 0;
        }

        public void setButtonState(VLButtonState buttonState) {
            if (mButtonState == buttonState)
                return;
            mButtonState = buttonState;
            notifyStateChanged();
        }

        public void setUserState(int userState) {
            if (mUserState == userState)
                return;
            mUserState = userState;
            notifyStateChanged();
        }

        public void notifyStateChanged() {
            if (mDelegate != null)
                mDelegate.onStatedButtonChanged(this, mButtonState, mUserState);
        }

        public void setStatedButtonDelegate(VLStatedButtonDelegate delegate) {
            VLDebug.Assert(mDelegate == null && delegate != null);
            mDelegate = delegate;
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (mDelegate != null) {
                mDelegate.onStatedButtonCreated(this, inflater);
                mDelegate.onStatedButtonChanged(this, mButtonState, mUserState);
            }
        }
    }

    public void setRedRound(int offset) {
        if (offset < 0)
            offset = 0;
        if (offset >= mButtons.size())
            offset = mButtons.size() - 1;
        for (int i = 0; i < mButtons.size(); i++) {
            VLStatedButton button = mButtons.get(i);
            if (i == offset) {
                button.setUserState(1);
            } else {
                button.setUserState(2);
            }
        }
    }

    public void setRedImRound(int offset) {
        if (offset < 0)
            offset = 0;
        if (offset >= mButtons.size())
            offset = mButtons.size() - 1;
        for (int i = 0; i < mButtons.size(); i++) {
            VLStatedButton button = mButtons.get(i);
            if (i == offset) {
                button.setUserState(3);
            } else {
                button.setUserState(4);
            }
        }
    }

    public void cancelRedRound(int offset) {
        if (offset < 0)
            offset = 0;
        if (offset >= mButtons.size())
            offset = mButtons.size() - 1;
        for (int i = 0; i < mButtons.size(); i++) {
            VLStatedButton button = mButtons.get(i);
            if (i == offset) {
                button.setUserState(2);
            }
        }
    }

    public void cancelRedIMRound(int offset) {
        if (offset < 0)
            offset = 0;
        if (offset >= mButtons.size())
            offset = mButtons.size() - 1;
        for (int i = 0; i < mButtons.size(); i++) {
            VLStatedButton button = mButtons.get(i);
            if (i == offset) {
                button.setUserState(4);
            }
        }
    }
}
