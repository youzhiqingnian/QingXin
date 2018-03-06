package com.qingxin.medical.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qingxin.medical.QingXinAdapter;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListFragment;
import com.qingxin.medical.app.vip.VipListFragment;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.indicator.CommonNavigator;
import com.qingxin.medical.widget.indicator.CommonNavigatorAdapter;
import com.qingxin.medical.widget.indicator.IPagerIndicator;
import com.qingxin.medical.widget.indicator.IPagerTitleView;
import com.qingxin.medical.widget.indicator.LinePagerIndicator;
import com.qingxin.medical.widget.indicator.MagicIndicator;
import com.qingxin.medical.widget.indicator.SimplePagerTitleView;
import com.qingxin.medical.widget.indicator.ViewPagerHelper;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */
public class SearchActivity extends QingXinActivity implements View.OnClickListener {

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private static final String SEARCH_RESULT = "SEARCH_RESULT";
    private SearchResultAdapter mSearchResultAdapter;
    private TextView mClearHistoryTv, mHistoryTv, mSearchResultTv;
    private EditText mSearchEt;
    private LinearLayout mSearchHistoryLl, mSearchResultLl;
    private FrameLayout mSearchResultView;
    private VipListFragment mVipListFragment;
    private GoddessDiaryListFragment mGoddessDiaryListFragment;

    private List<String> mSearchResults;
    private final static ObjectMapper MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mClearHistoryTv = findViewById(R.id.clearHistoryTv);
        mHistoryTv = findViewById(R.id.historyTv);

        ImageView deleteIv = findViewById(R.id.deleteIv);
        TextView cancelTv = findViewById(R.id.cancelTv);
        String result = getSharedPreferences().getString(SEARCH_RESULT, null);
        if (null != result) {
            try {
                mSearchResults = MAPPER.readValue(result, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null == mSearchResults || mSearchResults.size() == 0) {
            if (null == mSearchResults) {
                mSearchResults = new ArrayList<>();
            }
            mClearHistoryTv.setVisibility(View.GONE);
            mHistoryTv.setText("没有历史记录");
        }
        mClearHistoryTv.setOnClickListener(this);
        mSearchResultAdapter = new SearchResultAdapter(mSearchResults);
        RecyclerView recyclerView = findViewById(R.id.historyRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mSearchResultAdapter);

        mSearchEt = findViewById(R.id.searchEt);
        mSearchResultTv = findViewById(R.id.searchResultTv);
        mSearchHistoryLl = findViewById(R.id.searchHistoryLl);
        mSearchResultView = findViewById(R.id.searchResultView);
        mSearchResultLl = findViewById(R.id.searchResultLl);
        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (mSearchResultView.getVisibility() == View.GONE) {
                        mSearchResultView.setVisibility(View.VISIBLE);
                    }
                    mSearchResultTv.setText(s.toString());
                    if (deleteIv.getVisibility() == View.GONE) {
                        deleteIv.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mSearchResultView.getVisibility() == View.VISIBLE) {
                        mSearchResultView.setVisibility(View.GONE);
                    }
                    if (deleteIv.getVisibility() == View.VISIBLE) {
                        deleteIv.setVisibility(View.GONE);
                    }
                }
            }
        });
        deleteIv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
        mSearchResultView.setOnClickListener(this);

        MagicIndicator indicator = findViewById(R.id.magicIndicator);
        ViewPager viewPager = findViewById(R.id.viewPager);
        mVipListFragment = VipListFragment.newInstance(false);
        mGoddessDiaryListFragment = GoddessDiaryListFragment.newInstance(false);
        VLFragment[] fragments = new VLFragment[]{mVipListFragment, mGoddessDiaryListFragment};
        final String[] titles = new String[]{getResources().getString(R.string.xin_buy), getResources().getString(R.string.diary)};
        QingXinAdapter adapter = new QingXinAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        CommonNavigator navigator = new CommonNavigator(this);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setTextNormalColor(0xff464a4c);
                titleView.setTextSelectedColor(0xff3bc5e8);
                titleView.setText(titles[index]);
                titleView.setTextSize(18);
                titleView.setOnClickListener(v -> viewPager.setCurrentItem(index));
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator lineIndicator = new LinePagerIndicator(context);
                lineIndicator.setLineHeight(VLUtils.dip2px(2));
                lineIndicator.setLineWidth(VLUtils.dip2px(16));
                lineIndicator.setRoundRadius(VLUtils.dip2px(2));
                lineIndicator.setLineWidth(VLUtils.dip2px(16));
                lineIndicator.setColors(0xff3bc5e8);
                lineIndicator.setYOffset(VLUtils.dip2px(5));
                return lineIndicator;
            }
        });
        indicator.setNavigator(navigator);
        ViewPagerHelper.bind(indicator, viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelTv:
                this.finish();
                break;
            case R.id.clearHistoryTv:
                getSharedPreferences().edit().remove(SEARCH_RESULT).apply();
                mSearchResults.clear();
                mSearchResultAdapter.notifyDataSetChanged();
                mHistoryTv.setText("没有历史记录");
                if (mClearHistoryTv.getVisibility() == View.VISIBLE) {
                    mClearHistoryTv.setVisibility(View.GONE);
                }
                break;
            case R.id.searchResultView:
                if (mSearchHistoryLl.getVisibility() == View.VISIBLE) {
                    mSearchHistoryLl.setVisibility(View.GONE);
                }
                if (mSearchResultView.getVisibility() == View.VISIBLE) {
                    mSearchResultView.setVisibility(View.GONE);
                }
                if (mSearchResultLl.getVisibility() == View.GONE) {
                    mSearchResultLl.setVisibility(View.VISIBLE);
                }
                if (!mSearchResults.contains(mSearchResultTv.getText().toString().trim())) {
                    mSearchResults.add(mSearchResultTv.getText().toString().trim());
                    mSearchResultAdapter.notifyItemInserted(mSearchResults.size() - 1);
                }
                String results = null;
                try {
                    results = MAPPER.writeValueAsString(mSearchResults);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getSharedPreferences().edit().putString(SEARCH_RESULT, results).apply();
                VLScheduler.instance.schedule(500, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        mVipListFragment.setSearch(mSearchResultTv.getText().toString().trim());
                        mGoddessDiaryListFragment.setSearch(mSearchResultTv.getText().toString().trim());
                        mSearchResultTv.setText("");
                    }
                });
                break;
            case R.id.deleteIv:
                mSearchEt.getText().clear();
                break;
            default:
                break;
        }
    }
}
