package com.hongdroid.customcalendar2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdroid.customcalendar2.util.BaseCalendar;
import com.hongdroid.customcalendar2.MainActivity;
import com.hongdroid.customcalendar2.R;

import java.util.Calendar;

/**
 * 리사이클러 뷰 어댑터 (그리드 레이아웃 매니져) 사용
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private Context mContext;
    private BaseCalendar mBaseCalendar;

    public CustomAdapter(Context _context) {
        this.mContext = _context;
        this.mBaseCalendar = new BaseCalendar();
        refreshView(this.mBaseCalendar.initBaseCalendar());
    }

    @NonNull
    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(mContext).inflate(R.layout.list_item_schedule,parent, false);
        return new CustomViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        if(position % BaseCalendar.DAYS_OF_WEEK == 0)
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.COLOR_CALENDAR_RED));          // 일요일은 빨간색을 표시한다.
        else if(position % BaseCalendar.DAYS_OF_WEEK == 6)
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.COLOR_CALENDAR_BLUE));         // 토요일은 빨간색을 표시한다.
        else
            holder.tv_date.setTextColor(mContext.getResources().getColor(R.color.COLOR_CALENDAR_DARK_GRAY));    // 나머지 날짜는 검회색을 표시한다.

        if( position < mBaseCalendar.prevMonthTailOffset ||
                position >= mBaseCalendar.prevMonthTailOffset + mBaseCalendar.currentMonthMaxDate )             // 이전 달의 끝 부분이나 다음 달의 초반 부분의 영역은 투명도를 주어 UI 상으로 구분시켜 줌
            holder.tv_date.setAlpha(0.3f);
        else
            holder.tv_date.setAlpha(1f);

        holder.tv_date.setText(String.valueOf(mBaseCalendar.dataList.get(position)));
    }

    public void changeToPrevMonth() {
        refreshView(mBaseCalendar.changeToPrevMonth());
    }

    public void changeToNextMonth() {
        refreshView(mBaseCalendar.changeToNextMonth());
    }

    @Override
    public int getItemCount() {
        return BaseCalendar.ROW_OF_CALENDAR * BaseCalendar.DAYS_OF_WEEK; // 한 주에 7일 * 6열의 그리드로 구성되어야 한다.
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tv_date;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar curCalendar = mBaseCalendar.initBaseCalendar();
                    int curPos = getAdapterPosition();                                                            // 현재 클릭 한 position ( 위치 ) 참고로 position 은 첫번째 배열 아이템이 0 으로 시작된다.
                    int clickYear = curCalendar.get(Calendar.YEAR);                                               // 클릭 년(연)
                    int clickMonth = curCalendar.get(Calendar.MONTH);                                             // 클릭 월
                    int clickDay = mBaseCalendar.dataList.get(curPos);                                            // 클릭 일
                    String combineDate = clickYear + " 년 " + (clickMonth + 1) + " 월 " + clickDay + " 일 ";       // 년 월 일 합친 String
                    Toast.makeText(mContext, combineDate, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Refresh Adapter
     * @param _calendar
     */
    private void refreshView(Calendar _calendar) {
        notifyDataSetChanged();
        ((MainActivity)mContext).refreshCurrentMonth(_calendar);
    }


}
