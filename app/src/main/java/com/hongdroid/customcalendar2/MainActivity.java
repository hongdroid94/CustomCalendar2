package com.hongdroid.customcalendar2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hongdroid.customcalendar2.adapter.CustomAdapter;
import com.hongdroid.customcalendar2.util.BaseCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 달력을 표시하는 메인 화면
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomAdapter mAdapter;
    private RecyclerView mRv_schedule;
    private TextView
            mTv_prev,
            mTv_next,
            mTv_current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInit();
    }

    private void setInit() {
        mTv_prev = findViewById(R.id.tv_prev_month);
        mTv_next = findViewById(R.id.tv_next_month);
        mTv_current = findViewById(R.id.tv_current_month);
        mRv_schedule = findViewById(R.id.rv_schedule);

        mRv_schedule.setLayoutManager(new GridLayoutManager(this, BaseCalendar.DAYS_OF_WEEK));               // 그리드 레이아웃 매니져 적용 (엑셀 표처럼 네모 칸 형태 구조)
        mRv_schedule.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));   // 가로 방향으로 Divider (구분선) 을 두어 나눈다.
        mRv_schedule.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));     // 세로 방향으로 Divider (구분선) 을 두어 나눈다.

        mTv_prev.setOnClickListener(this);
        mTv_next.setOnClickListener(this);

        mAdapter = new CustomAdapter(this);
        mRv_schedule.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.tv_prev_month: {
                mAdapter.changeToPrevMonth();
                break; }
            case R.id.tv_next_month: {
                mAdapter.changeToNextMonth();
                break; }
        }
    }

    /**
     * 다음달 or 이전달 변동에 의한 현재달 최신화
     * @param _calendar
     */
    public void refreshCurrentMonth(Calendar _calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM", Locale.KOREAN);
        mTv_current.setText(formatter.format(_calendar.getTime()));
    }
}