package com.vlee78.android.vl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class VLListViewActivity extends VLActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        VLListView mListView = new VLListView(this);
        mListView.setBackgroundColor(0xffffffff);
        mListView.setLayoutParams(VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        setContentView(mListView);

        for(int i=0;i<40;i++)
            mListView.dataAddTail(VLListView.VLDummyStringType.class, "data : " + i );
        mListView.dataCommit(VLListView.RELOAD_SCROLL_TOP);


        mListView.setListHeader(new VLListView.VLListHeader()
        {
            TextView mTextView;

            @Override
            public void onCreate(LayoutInflater inflater, ViewGroup root)
            {
                mTextView = new TextView(VLListViewActivity.this);
                mTextView.setLayoutParams(VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.WRAP_CONTENT));
                mTextView.setText("下拉加载更多");
                root.addView(mTextView);
            }

            @Override
            public void onStateChanged(int from, int to)
            {
                Log.i("prodialog", " prodialog : onStateChanged from=" + from + ",to=" + to);
                if(to== VLListView.VLListHeader.STATE_LOADING)
                {
                    VLScheduler.instance.schedule(2000, VLScheduler.THREAD_MAIN, new VLBlock()
                    {
                        @Override
                        protected void process(boolean canceled)
                        {
                            reset();
                        }
                    });
                }
            }

            @Override
            public void onPushHeight(int height)
            {
                Log.i("prodialog", " prodialog : header onPushHeight height=" + height + ", contentHeight=" + getContentHeight());
            }
        });
        mListView.getListHeader().setContentOccupy(false);

        mListView.setListFooter(new VLListView.VLListFooter()
        {
            TextView mTextView;

            @Override
            public void onCreate(LayoutInflater inflater, ViewGroup root)
            {
                mTextView = new TextView(VLListViewActivity.this);
                mTextView.setLayoutParams(VLUtils.paramsGroup(VLUtils.MATCH_PARENT, VLUtils.WRAP_CONTENT));
                mTextView.setText("下啦加载更多");
                root.addView(mTextView);
            }

            @Override
            public void onStateChanged(int from, int to)
            {
                Log.i("prodialog", " prodialog : onStateChanged from=" + from + ",to=" + to);
                if(to== VLListView.VLListHeader.STATE_LOADING)
                {
                    VLScheduler.instance.schedule(2000, VLScheduler.THREAD_MAIN, new VLBlock()
                    {
                        @Override
                        protected void process(boolean canceled)
                        {
                            reset();
                        }
                    });
                }
            }

            @Override
            public void onPushHeight(int height)
            {
                Log.i("prodialog", " prodialog : footer onPushHeight height=" + height + ", contentHeight=" + getContentHeight());
            }
        });

    }
}
