package com.hongdroid.customcalendar2.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 커스텀 달력의 전반적인 연산을 도와주는 유틸 클래스
 */

public class BaseCalendar {
    public static final int DAYS_OF_WEEK = 7;
    public static final int ROW_OF_CALENDAR = 6;

    public Calendar mCalendar;

    public int prevMonthTailOffset = 0; // 이전달의 후반 날짜
    public int nextMonthHeadOffset = 0; // 다음달의 초반 날짜
    public int currentMonthMaxDate = 0; // 달의 마지막 날짜

    public ArrayList<Integer> dataList;

    public BaseCalendar() {
        this.dataList = new ArrayList<>();
        this.mCalendar = Calendar.getInstance();
        this.mCalendar.setTime(new Date());
    }

    /**
     * Init Calendar
     */
    public Calendar initBaseCalendar() {
        makeMonthDate();
        return mCalendar;
    }

    /**
     * 이전 달로 이동
     * @return
     */
    public Calendar changeToPrevMonth() {
        if(mCalendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) - 1);
            mCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        } else {
            mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) - 1);
        }
        makeMonthDate();

        return mCalendar;
    }

    /**
     * 다음 달로 이동
     * @return
     */
    public Calendar changeToNextMonth() {
        if(mCalendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            mCalendar.set(Calendar.YEAR, mCalendar.get(Calendar.YEAR) + 1);
            mCalendar.set(Calendar.MONTH, 0);
        } else {
            mCalendar.set(Calendar.MONTH, mCalendar.get(Calendar.MONTH) + 1);
        }
        makeMonthDate();

        return mCalendar;
    }

    /**
     * Make Month Date
     */
    public void makeMonthDate() {
        dataList.clear();
        mCalendar.set(Calendar.DATE, 1);
        currentMonthMaxDate = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        prevMonthTailOffset = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        makePrevMonthTail((Calendar) mCalendar.clone());
        makeCurrentMonth(mCalendar);

        nextMonthHeadOffset = ROW_OF_CALENDAR * DAYS_OF_WEEK - (prevMonthTailOffset + currentMonthMaxDate);
        makeNextMonthHead();
    }

    private void makePrevMonthTail(Calendar _calendar) {
        _calendar.set(Calendar.MONTH, _calendar.get(Calendar.MONTH) - 1);
        int maxDate = _calendar.getActualMaximum(Calendar.DATE);
        int maxOffsetDate = maxDate - prevMonthTailOffset;

        for (int i = 1; i <= prevMonthTailOffset; i++) {
            dataList.add(++maxOffsetDate);
        }
    }

    private void makeCurrentMonth(Calendar _calendar) {
        for (int i = 1; i <= _calendar.getActualMaximum(Calendar.DATE); i++) {
            dataList.add(i);
        }
    }

    private void makeNextMonthHead() {
        int date = 1;
        for (int i = 1; i <= nextMonthHeadOffset; i++) {
            dataList.add(date++);
        }
    }
}
