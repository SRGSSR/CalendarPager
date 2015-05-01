package com.tudou.calendarpager.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.tudou.calendarpager.R;
import com.tudou.calendarpager.model.CalendarDay;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by tudou on 15-4-30.
 */
public class WeekView extends View {
  private final static String TAG = "WeekView";
  private final static int DAY_IN_WEEK = 7;

  private CalendarDay mFirstShowDay;
  private CalendarDay mStartDay;
  private CalendarDay mEndDay;
  private int mWeekPostion;
  private int mDaysPosition;

  private Paint mPaintNormal;
  private Paint mPaintSelect;
  private int mSelectDay;

  private float acceleration = 0.5f;
  private float headMoveOffset = 0.6f;
  private float footMoveOffset = 1- headMoveOffset;
  private float radiusMax;
  private float radiusMin;
  private float radiusOffset;

  private int indicatorColorId;
  private int indicatorColorsId;

  private SpringView mSpringView;

  private ArrayList<CalendarDay> mWeekCalendarDays;

  public WeekView(Context context) {
    this(context, null);
  }

  public WeekView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public WeekView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(attrs);
    initPaint();
    initData();
  }

  private void initData() {
    mWeekCalendarDays = new ArrayList<>(DAY_IN_WEEK);
  }

  private void initAttrs(AttributeSet attrs){
    radiusMax = getResources().getDimension(R.dimen.si_default_radius_max);
    radiusMin = getResources().getDimension(R.dimen.si_default_radius_min);

    indicatorColorId = R.color.si_default_indicator_bg;

    radiusOffset = radiusMax - radiusMin;
  }


  private void initPaint() {
    mPaintNormal = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintNormal.setColor(getResources().getColor(android.R.color.darker_gray));
    mPaintNormal.setTextSize(26f);

    mPaintSelect = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintSelect.setColor(getResources().getColor(android.R.color.holo_blue_dark));


    initSpringView();
  }

  protected void onDraw(Canvas canvas) {

    drawSpringView(canvas);

    if (mWeekCalendarDays.size() < 7) {
      super.onDraw(canvas);
      return;
    }

    for (int i = 0; i < DAY_IN_WEEK; i++) {
      String content = String.valueOf(mWeekCalendarDays.get(i).day);
      Paint.FontMetrics fontMetrics = mPaintNormal.getFontMetrics();
      //计算文字高度
      float fontHeight = fontMetrics.bottom - fontMetrics.top;
      //计算文字baseline
      float textWidth = mPaintNormal.measureText(content);
      float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);

      float textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
      canvas.drawText(content, getResources().getDimension(R.dimen.activity_horizontal_margin) +  parentWidth / 7 * i + parentWidth / 7 / 2 - textWidth / 2, textBaseY, mPaintNormal);

    }
  }

  private void drawSpringView(Canvas canvas) {
    if (mWeekPostion  != mDaysPosition / 7) return;
    mSpringView.getHeadPoint().setY(getHeight() / 2);
    mSpringView.getFootPoint().setY(getHeight() / 2);
    mSpringView.makePath();
    canvas.drawPath(mSpringView.path, mSpringView.paint);
    canvas.drawCircle(mSpringView.headPoint.getX(), mSpringView.headPoint.getY(),
        mSpringView.headPoint.getRadius(), mSpringView.paint);
    canvas.drawCircle(mSpringView.footPoint.getX(), mSpringView.footPoint.getY(),
        mSpringView.footPoint.getRadius(), mSpringView.paint);
  }

  public void onViewPageScroll(int position, float positionOffset, int positionOffsetPixels) {
    mDaysPosition = position;
    Log.e(TAG, "position: "
        + position
        + "      positionOffset: "
        + positionOffset
        + "     positionOffsetPicxels: "
        + positionOffsetPixels);

    if (position % 7 < 7) {
      // radius
      float radiusOffsetHead = 0.5f;
      if(positionOffset < radiusOffsetHead){
        mSpringView.getHeadPoint().setRadius(radiusMin);
      }else{
        mSpringView.getHeadPoint().setRadius(((positionOffset-radiusOffsetHead)/(1-radiusOffsetHead) * radiusOffset + radiusMin));
      }
      float radiusOffsetFoot = 0.5f;
      if(positionOffset < radiusOffsetFoot){
        mSpringView.getFootPoint().setRadius((1-positionOffset/radiusOffsetFoot) * radiusOffset + radiusMin);
      }else{
        mSpringView.getFootPoint().setRadius(radiusMin);
      }

      // x
      float headX = 1f;
      if (positionOffset < headMoveOffset){
        float positionOffsetTemp = positionOffset / headMoveOffset;
        headX = (float) ((Math.atan(positionOffsetTemp*acceleration*2 - acceleration ) + (Math.atan(acceleration))) / (2 * (Math.atan(acceleration))));
      }
      mSpringView.getHeadPoint().setX(getDayX(position) - headX * getPositionDistance(position));
      float footX = 0f;
      if (positionOffset > footMoveOffset){
        float positionOffsetTemp = (positionOffset- footMoveOffset) / (1- footMoveOffset);
        footX = (float) ((Math.atan(positionOffsetTemp*acceleration*2 - acceleration ) + (Math.atan(acceleration))) / (2 * (Math.atan(acceleration))));
      }
      mSpringView.getFootPoint().setX(getDayX(position) - footX * getPositionDistance(position));

      // reset radius
      if(positionOffset == 0){
        mSpringView.getHeadPoint().setRadius(radiusMax);
        mSpringView.getFootPoint().setRadius(radiusMax);
      }
    } else {
      mSpringView.getHeadPoint().setX(getDayX(position));
      mSpringView.getFootPoint().setX(getDayX(position));
      mSpringView.getHeadPoint().setRadius(radiusMax);
      mSpringView.getFootPoint().setRadius(radiusMax);
    }

    if (position % 7 == 6 && positionOffset > 0.8) {
      mSpringView.paint.setAlpha(0);
    } else {
      mSpringView.paint.setAlpha(255);
    }

    invalidate();
  }

  private float getPositionDistance(int position) {
    float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
    return -parentWidth / 7;
  }

  private float getDayX(int position) {
    float parentWidth = getWidth() - 2 * getResources().getDimension(R.dimen.activity_horizontal_margin);
    return getResources().getDimension(R.dimen.activity_horizontal_margin) +  parentWidth / 7 * (position % 7) + parentWidth / 7 / 2;
  }

  public void setSelectDay(int selectDay) {
    mSelectDay = selectDay;
  }

  private void initSpringView() {
    addPointView();
  }

  private void addPointView() {
    mSpringView = new SpringView();
    mSpringView.setIndicatorColor(getResources().getColor(indicatorColorId));
  }

  public void setDays(CalendarDay firstShowDay, CalendarDay startDay, CalendarDay endDay) {
    mFirstShowDay = firstShowDay;
    mStartDay = startDay;
    mEndDay = endDay;
  }

  public void setPosition(int position) {
    mWeekPostion = position;
    createWeekCalendardays();
    invalidate();
  }

  private void createWeekCalendardays() {
    mWeekCalendarDays.clear();
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(mFirstShowDay.getTime());
    calendar.roll(Calendar.DAY_OF_YEAR, mWeekPostion * 7);
    for (int i = 0; i < DAY_IN_WEEK; i++) {
      mWeekCalendarDays.add(new CalendarDay(calendar));
      calendar.roll(Calendar.DAY_OF_YEAR, 1);
    }
  }
}