package com.tudou.calendarpager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.tudou.library.model.CalendarDay;
import com.test.tudou.library.util.DayUtils;
import com.tudou.calendarpager.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tudou on 15-4-30.
 */
public class SimpleFragment extends Fragment {

  @BindView(R.id.text) TextView mText;

  private CalendarDay mCalendarDay;
  private Unbinder unbinder;

  public static SimpleFragment newInstance(CalendarDay calendarDay) {
    SimpleFragment simpleFragment = new SimpleFragment();
    simpleFragment.mCalendarDay = calendarDay;
    return simpleFragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_simple, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    mText.setText("This is at: " + DayUtils.formatEnglishTime(mCalendarDay.getTime()));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }
}
