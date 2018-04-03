package com.qingxin.medical.common;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.qingxin.medical.widget.OnWheelChangedListener;
import com.qingxin.medical.widget.WheelView;
import com.qingxin.medical.widget.adapters.NumericWheelAdapter;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLUtils;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期弹框.
 *
 * @author zhikuo
 */
public class QingXinDatePopuWindow {

    // 非法时间范围
    public final static int ERROR_CODE_ILLEGAL_RANGE = 101;
    private DatePopuWindowType mWindowType;
    private DateFormatType mDateType;
    // 普通日期框当前日期
    private Calendar mDateState;
    // 日期范围, 选中日期状态
    private DateRange mDateRange;
    private String mTitle;
    private VLAsyncHandler<QingXinDatePopuWindow> mCallBack;
    private CommonCustomPopupWindow mContentPopuWindow;

    // 日期范围
    public class DateRange {
        Calendar mStartDateState;
        Calendar mEndDateState;
        TextView mStartDateView;
        TextView mEndDateView;
        TextView mCurrentSelectedView;

        void initView() {
            if (mStartDateView != null && mStartDateState != null) {
                mStartDateView.setText(VLUtils.dateToString(mStartDateState.getTime(), VLUtils.formatDate2));
            }
            if (mEndDateView != null && mEndDateState != null) {
                mEndDateView.setText(VLUtils.dateToString(mEndDateState.getTime(), VLUtils.formatDate2));
            }
        }

        void setCurrentView(TextView currentView) {
            this.mCurrentSelectedView = currentView;
            // 更改背景
            currentView.setBackgroundResource(R.drawable.bg_blue_corners);
            if (currentView == mStartDateView) {
                mEndDateView.setBackgroundResource(R.drawable.bg_gray_corners);
            } else {
                mStartDateView.setBackgroundResource(R.drawable.bg_gray_corners);
            }
        }

        TextView getCurrentView() {
            return mCurrentSelectedView != null ? mCurrentSelectedView : mStartDateView;
        }

        Calendar getCurrentState() {
            return mCurrentSelectedView == mStartDateView ? mStartDateState : mEndDateState;
        }

        public Date getStartDateState() {
            return mStartDateState.getTime();
        }

        public Date getEndDateState() {
            return mEndDateState.getTime();
        }
    }

    /**
     * 日期框中日期格式类型
     */
    public enum DateFormatType {
        Y_M_D,// 年月日
        M_D_H,// 月日小时
    }

    /**
     * 日期框类型
     */
    public enum DatePopuWindowType {
        COMMON,// 普通日期框
        RANGE,// 日期范围
    }

    /**
     * 年月日范围框
     *
     * @param startDate 日期框起始时间默认值
     * @param endDate   日期框结束时间默认值
     * @param callBack  选中后回调
     * @param context   开启界面的上下文
     */
    public QingXinDatePopuWindow(Date startDate, Date endDate, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        if (startDate == null) startDate = new Date();
        if (endDate == null) endDate = new Date();
        if (startDate.getTime() > endDate.getTime()) {
            callBack.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, ERROR_CODE_ILLEGAL_RANGE, "非法时间范围");
            return;
        }
        this.mDateType = DateFormatType.Y_M_D;
        this.mWindowType = DatePopuWindowType.RANGE;
        DateRange range = new DateRange();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        range.mStartDateState = startCalendar;
        range.mEndDateState = endCalendar;
        this.mDateRange = range;
        init0("", startCalendar.get(Calendar.YEAR) + 10, startCalendar.get(Calendar.YEAR) - 10, callBack, context);
    }

    /**
     * 年月日框
     *
     * @param defaultDate 日期框打开默认时间
     * @param title       显示标题
     * @param callBack    选中后回调
     * @param context     开启界面的上下文
     */
    public QingXinDatePopuWindow(final Date defaultDate, final String title, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        this(DateFormatType.Y_M_D, defaultDate, title, callBack, context);
    }

    /**
     * 创建日期框
     *
     * @param type        日起框类型
     * @param defaultDate 日期框打开默认时间
     * @param title       显示标题
     * @param callBack    选中后回调
     * @param context     开启界面的上下文
     */
    public QingXinDatePopuWindow(DateFormatType type, Date defaultDate, String title, VLAsyncHandler<QingXinDatePopuWindow> callBack, Context context) {
        if (type == null) type = DateFormatType.Y_M_D;
        this.mWindowType = DatePopuWindowType.COMMON;
        this.mDateType = type;
        VLDebug.Assert(defaultDate != null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);
        int yearOfDate = calendar.get(Calendar.YEAR);
        this.mDateState = calendar;
        if (DateFormatType.Y_M_D.equals(type)) {
            // 默认最大最小年通过起始日期计算
            init0(title, yearOfDate + 50, yearOfDate - 130, callBack, context);
        } else if (DateFormatType.M_D_H.equals(type)) {
            init1(title, callBack, context);
        }
    }

    /**
     * 年月日框
     *
     * @param yearOfDate 从1开始
     * @param mothOfYear 和系统的Calendar保持同一 从0开始
     * @param dayOfMoth  从1开始
     * @param title      显示标题
     * @param maxYear    最大年
     * @param minYear    最小年
     * @param callBack   选中后回调
     * @param context    开启界面的上下文
     */
    public QingXinDatePopuWindow(int yearOfDate, final int mothOfYear, final int dayOfMoth, final String title, final int maxYear, final int minYear, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearOfDate);
        calendar.set(Calendar.MONTH, mothOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMoth);
        this.mDateState = calendar;
        init0(title, maxYear, minYear, callBack, context);
    }

    // 月/日/小时框
    public QingXinDatePopuWindow(int mothOfYear, int dayOfMoth, int hourOfDay, String title, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, mothOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMoth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        this.mDateState = calendar;
        init1(title, callBack, context);
    }

    /**
     * 年月日框
     *
     * @param title    显示标题
     * @param maxYear  最大年
     * @param minYear  最小年
     * @param callBack 选中后回调
     * @param context  开启界面的上下文
     */
    private void init0(final String title, final int maxYear, final int minYear, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        Calendar currentState = getCurrentState();
        if (null == currentState) return;
        int yearOfDate = currentState.get(Calendar.YEAR);
        int mothOfYear = currentState.get(Calendar.MONTH);
        int dayOfMoth = currentState.get(Calendar.DAY_OF_MONTH);

        mTitle = title;
        mCallBack = callBack;
        mContentPopuWindow = new CommonCustomPopupWindow(context,
                R.layout.group_select_date_popupwindow, R.id.select_date_layout,
                R.style.PopupAnimation, new ColorDrawable(0x80000000), createOnClickListener());
        //判断是否是魅族手机，如果是则显示隐藏的空白布局
        if (TextUtils.equals("Meizu", android.os.Build.BRAND)) {
            mContentPopuWindow.mMenuView.findViewById(R.id.selectDateEmpty).setVisibility(View.VISIBLE);
        }

        initTitleBar4Date();

        final WheelView wheelYear = mContentPopuWindow.mMenuView.findViewById(R.id.wheelYear);
        final WheelView wheelMonth = mContentPopuWindow.mMenuView.findViewById(R.id.wheelMonth);
        final WheelView wheelDay = mContentPopuWindow.mMenuView.findViewById(R.id.wheelDay);

        // TODO
        OnWheelChangedListener listener = (wheel, oldValue, newValue) -> {
            int yearOfDate1 = minYear + wheelYear.getCurrentItem();
            int mothOfYear1 = wheelMonth.getCurrentItem();
            int dayOfMoth1 = wheelDay.getCurrentItem() + 1;
            Calendar currentState1 = getCurrentState();
            currentState1.set(Calendar.YEAR, yearOfDate1);
            currentState1.set(Calendar.MONTH, mothOfYear1);
            currentState1.set(Calendar.DAY_OF_MONTH, dayOfMoth1);
            updateDays(wheelYear, wheelMonth, wheelDay, context);
        };

        DateNumericAdapter monthAdapter = new DateNumericAdapter(context, 1, 12, mothOfYear);
        monthAdapter.setTextType("月");
        wheelMonth.setViewAdapter(monthAdapter);
        wheelMonth.setCurrentItem(mothOfYear);
        wheelMonth.addChangingListener(listener);
        // year
        DateNumericAdapter yearAdapter = new DateNumericAdapter(context, minYear, maxYear, yearOfDate);
        yearAdapter.setTextType("年");
        wheelYear.setViewAdapter(yearAdapter);
        wheelYear.setCurrentItem(yearOfDate - minYear);
        wheelYear.addChangingListener(listener);
        updateDays(wheelYear, wheelMonth, wheelDay, context);
        wheelDay.setCurrentItem(dayOfMoth - 1);
        wheelDay.addChangingListener(listener);
    }

    private void init1(String title, final VLAsyncHandler<QingXinDatePopuWindow> callBack, final Context context) {
        Calendar currentState = getCurrentState();
        if (null == currentState) return;
        final int mothOfYear = currentState.get(Calendar.MONTH);
        final int dayOfMoth = currentState.get(Calendar.DAY_OF_MONTH) + 1;
        final int hourOfDay = currentState.get(Calendar.HOUR_OF_DAY) + 1;
        mTitle = title;
        mCallBack = callBack;
        mContentPopuWindow = new CommonCustomPopupWindow(context,
                R.layout.group_select_time_popupwindow, R.id.select_date_layout,
                R.style.PopupAnimation, new ColorDrawable(0x80000000), createOnClickListener());
        TextView titleTxt = mContentPopuWindow.mMenuView.findViewById(R.id.wheelDateChoose);
        titleTxt.setText(mTitle);
        if (TextUtils.equals("Meizu", android.os.Build.BRAND)) {
            mContentPopuWindow.mMenuView.findViewById(R.id.selectDateEmpty).setVisibility(View.VISIBLE);
        }
        //initTitleBar4Date();

        Calendar calendar = Calendar.getInstance();
        WheelView mWheelMonth = mContentPopuWindow.mMenuView.findViewById(R.id.wheelYear);
        DateNumericAdapter mMonthAdapter = new DateNumericAdapter(context, 1, 12, mothOfYear);
        mWheelMonth.setViewAdapter(mMonthAdapter);
        mMonthAdapter.setTextType("月");
        final WheelView mWheelDay = mContentPopuWindow.mMenuView.findViewById(R.id.wheelMonth);
        mWheelMonth.setCurrentItem(mothOfYear);
        mWheelMonth.addChangingListener((wheel, oldValue, newValue) -> {
            Calendar calendar12 = Calendar.getInstance();
            if (newValue > mothOfYear) {
                calendar12.add(Calendar.YEAR, 1);
            }
            calendar12.set(Calendar.MONTH, newValue);
            int maxDays = calendar12.getActualMaximum(Calendar.DAY_OF_MONTH);
            DateNumericAdapter mWheelDayAdapter = new DateNumericAdapter(context, 1, maxDays, newValue);
            mWheelDay.setViewAdapter(mWheelDayAdapter);
            mWheelDay.setCurrentItem(calendar12.get(Calendar.DAY_OF_MONTH) - 1);
            mWheelDayAdapter.setTextType("日");

            Calendar currentState12 = getCurrentState();
            if (null != currentState12)
                currentState12.setTime(calendar12.getTime());
        });

        mWheelDay.addChangingListener((wheel, oldValue, newValue) -> {
            Calendar calendar1 = getCurrentState();
            if (null != calendar1)
                calendar1.set(Calendar.DAY_OF_MONTH, newValue + 1);
        });
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DateNumericAdapter mWheelDayAdapter = new DateNumericAdapter(context, 1, maxDays, dayOfMoth);
        mWheelDay.setViewAdapter(mWheelDayAdapter);
        mWheelDay.setCurrentItem(dayOfMoth - 1);
        mWheelDayAdapter.setTextType("日");

        WheelView mWheelHour = mContentPopuWindow.mMenuView.findViewById(R.id.wheelDay);
        DateNumericAdapter mWheelHourAdapter = new DateNumericAdapter(context, 1, 24, 5);
        mWheelHour.setViewAdapter(mWheelHourAdapter);
        mWheelHourAdapter.setTextType("点");
        mWheelHour.addChangingListener((wheel, oldValue, newValue) -> {
            Calendar currentState1 = getCurrentState();
            currentState1.set(Calendar.HOUR_OF_DAY, newValue + 1);
        });
        mWheelHour.setCurrentItem(hourOfDay - 1);
    }


    /**
     * 初始化日期选择框Title栏
     */
    private void initTitleBar4Date() {
        // 范围title框
        LinearLayout rangeTitleBar = mContentPopuWindow.mMenuView.findViewById(R.id.date_range_title_bar);
        // 普通文本title框
        LinearLayout commonTitleBar = mContentPopuWindow.mMenuView.findViewById(R.id.date_select_title_bar);
        // 如果是日期范围开启范围选择title
        if (DatePopuWindowType.RANGE.equals(mWindowType)) {
            commonTitleBar.setVisibility(View.GONE);
            rangeTitleBar.setVisibility(View.VISIBLE);
            // 起始时间编辑框
            mDateRange.mStartDateView = rangeTitleBar.findViewById(R.id.minTimeEt);
            mDateRange.mEndDateView = rangeTitleBar.findViewById(R.id.maxTimeEt);

            // 当选中日期文本后修改背景
            View.OnClickListener listener = v -> {
                // 设置当前选择框
                mDateRange.setCurrentView((TextView) v);
            };
            // 默认选中起始日期框
            mDateRange.setCurrentView(mDateRange.mStartDateView);
            mDateRange.mStartDateView.setOnClickListener(listener);
            mDateRange.mEndDateView.setOnClickListener(listener);

            mDateRange.initView();
        } else {
            rangeTitleBar.setVisibility(View.GONE);
            commonTitleBar.setVisibility(View.VISIBLE);

            TextView titleTxt = mContentPopuWindow.mMenuView.findViewById(R.id.wheelDateChoose);
            titleTxt.setText(mTitle == null ? "Choose Date" : mTitle);
        }
    }


    private View.OnClickListener createOnClickListener() {
        return v -> {
            switch (v.getId()) {
                case R.id.wheelDateCancel:
                case R.id.timeRangeCancelTv:
                    mContentPopuWindow.dismiss();
                    if (null != mCallBack) {
                        mCallBack.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResCanceled, null);
                    }
                    break;
                case R.id.wheelDateOk:
                case R.id.timeRangeSureTv:
                    // 日期范围时截止时间需要大于起始时间
                    long dayMilli = 3600L * 1000 * 24;
                    if (mDateRange != null && mDateRange.mEndDateState.getTimeInMillis() / dayMilli < mDateRange.mStartDateState.getTimeInMillis() / dayMilli && mCallBack != null) {
                        mContentPopuWindow.dismiss();
                        mCallBack.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, ERROR_CODE_ILLEGAL_RANGE, "非法时间范围");
                    } else if (null != mCallBack) {
                        mContentPopuWindow.dismiss();
                        mCallBack.handlerSuccess(QingXinDatePopuWindow.this);
                    }
                    break;
                case R.id.wheelDateChoose:
                    break;
                default:
                    break;
            }
        };
    }

    private class DateNumericAdapter extends NumericWheelAdapter {
        DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            setTextSize(16);
            setTextColor(0xFF555555);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public CharSequence getItemText(int index) {
            return super.getItemText(index);
        }
    }

    // 获取当前日期对应的状态对象
    private Calendar getCurrentState() {
        if (DatePopuWindowType.COMMON.equals(this.mWindowType)) {
            return this.mDateState;
        }
        // 范围
        if (DatePopuWindowType.RANGE.equals(this.mWindowType)) {
            return mDateRange.getCurrentState();
        }
        return null;
    }

    private void updateDays(WheelView wheelYear, WheelView month, WheelView day, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + wheelYear.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DateNumericAdapter dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType("日");
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);

        // 更新日期框title
        if (mDateRange != null) {
            mDateRange.getCurrentView().setText(VLUtils.dateToString(mDateRange.getCurrentState().getTime(), VLUtils.formatDate2));
        }
    }

    /**
     * 显示窗口
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        mContentPopuWindow.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示窗口
     */
    public void show(View parent) {
        mContentPopuWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 获取当前窗口选中的时间
     */
    public Date getSelectedDate() {
        return null == getCurrentState() ? null : getCurrentState().getTime();
    }

    /**
     * 获取当前窗口选中的起止时间范围
     */
    public DateRange getSelectedDateRnage() {
        return mDateRange;
    }

    public PopupWindow getPopupWindow() {
        return mContentPopuWindow;
    }

}
