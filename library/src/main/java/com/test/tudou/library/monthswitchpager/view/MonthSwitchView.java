package com.test.tudou.library.monthswitchpager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.test.tudou.library.R;
import com.test.tudou.library.R2;
import com.test.tudou.library.model.CalendarDay;
import com.test.tudou.library.monthswitchpager.adapter.MonthViewAdapter;
import com.test.tudou.library.util.DayUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tudou on 15-5-18.
 */
public class MonthSwitchView extends LinearLayout implements MonthView.OnDayClickListener {

  @BindView(R2.id.text2)
  MonthSwitchTextView mSwitchText;
  @BindView(R2.id.content)
  MonthRecyclerView mRecyclerView;

  private MonthView.OnDayClickListener mOnDayClickListener;

  private MonthViewAdapter mMonthAdapter;
  private Unbinder unbinder;

  public MonthSwitchView(Context context) {
    this(context, null);
  }

  public MonthSwitchView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MonthSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    initialize(context, attrs, defStyleAttr);
  }

  private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
    LayoutInflater.from(context).inflate(R.layout.view_month_switch_container, this);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();

    unbinder = ButterKnife.bind(this);

    mMonthAdapter = new MonthViewAdapter(getContext(), this);
    mSwitchText.setMonthRecyclerView(mRecyclerView);
    mRecyclerView.setMonthSwitchTextView(mSwitchText);
    mRecyclerView.setAdapter(mMonthAdapter);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    unbinder.unbind();
  }

  public void setData(CalendarDay startDay, CalendarDay endDay) {
    mMonthAdapter.setData(startDay, endDay, null);
    mSwitchText.setDay(startDay, endDay);
  }

  public void setSelectDay(CalendarDay calendarDay) {
    mRecyclerView.scrollToPosition(DayUtils.calculateMonthPosition(mMonthAdapter.getStartDay(), calendarDay));
    mMonthAdapter.setSelectDay(calendarDay);
  }

  public void setOnDayClickListener(MonthView.OnDayClickListener onDayClickListener) {
    mOnDayClickListener = onDayClickListener;
  }

  @Override public void onDayClick(CalendarDay calendarDay) {
    mOnDayClickListener.onDayClick(calendarDay);
  }
}
