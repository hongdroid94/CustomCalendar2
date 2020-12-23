package com.hongdroid.customcalendar2.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongdroid.customcalendar2.database.DBHelper;
import com.hongdroid.customcalendar2.model.EventInfo;
import com.hongdroid.customcalendar2.util.BaseCalendar;
import com.hongdroid.customcalendar2.MainActivity;
import com.hongdroid.customcalendar2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 리사이클러 뷰 어댑터 (그리드 레이아웃 매니져) 사용
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private Context mContext;
    private BaseCalendar mBaseCalendar;
    private DBHelper mDBHelper;
    private Calendar mCurCalendar;
    private boolean mIsAlreadyWrited = false;    // 이전에 게시글을 작성한적 있는지 검사하는 불리언


    private ArrayList<EventInfo> mEventInfos;

    public CustomAdapter(Context _context) {
        this.mContext = _context;
        this.mDBHelper = new DBHelper(_context);
        this.mBaseCalendar = new BaseCalendar();
        this.mEventInfos = mDBHelper.getSelectEventListDB();
        mCurCalendar = mBaseCalendar.initBaseCalendar();
        refreshView(mCurCalendar);
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

        // SQLite Database 의 DB를 비교하여 날짜별 이벤트 유무에 따라 Dot Image를 VISIBLE 처리 해준다.
        if(mEventInfos.size() != 0) {
            for (int i = 0; i < mEventInfos.size(); i++)
            {
                if(mEventInfos.get(i).getStrEventDate().equals(getCombineDate(position))) {
                    holder.iv_event.setVisibility(View.VISIBLE);
                    break;
                } else {
                    holder.iv_event.setVisibility(View.INVISIBLE);
                }
            }
        }

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
        protected ImageView iv_event;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            iv_event = itemView.findViewById(R.id.iv_event);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); // 현재 클릭 한 position ( 위치 ) 참고로 position 은 첫번째 배열 아이템이 0 으로 시작된다.
                    String strCombineDate = getCombineDate(curPos);
                    ArrayList<EventInfo> item = mDBHelper.getSelectedEventItem(strCombineDate);
                    Toast.makeText(mContext, strCombineDate, Toast.LENGTH_SHORT).show();
                    setEditEventDialog(strCombineDate, item);
                }
            });
        }
    }

    private String getCombineDate(int _position)
    {
        int iYear = mCurCalendar.get(Calendar.YEAR);        // 클릭 년(연)
        int iMonth = mCurCalendar.get(Calendar.MONTH);      // 클릭 월
        int iDay = mBaseCalendar.dataList.get(_position);   // 클릭 일
        iMonth += 1;                                        // Calendar.MONTH는 실제 월의 -1만큼 모자라기때문에 1을 더해줘야 한다.

        if( _position < mBaseCalendar.prevMonthTailOffset) {
            // 이전 달 꼬리 부분
            if(iMonth == 1) {
                iYear -= 1 ;
                iMonth = 12;
            }
            else
                iMonth -=1 ;
        }

        if( _position >= mBaseCalendar.prevMonthTailOffset + mBaseCalendar.currentMonthMaxDate ) {
            // 다음 달 꼬리 부분
            if(iMonth == 12) {
                iYear += 1;
                iMonth = 1;
            } else {
                iMonth += 1;
            }
        }

        return iYear + " 년 " + iMonth + " 월 " + iDay + " 일 ";
    }

    private void setEditEventDialog(String _combineDate, ArrayList<EventInfo> _item)
    {
        mIsAlreadyWrited = false;
        // 팝업 창 띄우기
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_edit);
        EditText et_title = dialog.findViewById(R.id.et_title);
        EditText et_content = dialog.findViewById(R.id.et_content);

        String title = "";
        String content = "";
        if(_item.size() != 0) {
            mIsAlreadyWrited = true;
            title = _item.get(0).getStrEventTitle();
            content = _item.get(0).getStrEventContent();
            et_title.setText(title);
            et_content.setText(content);
        }

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(et_title.getText().length() == 0 || et_content.getText().length() == 0) {
                    Toast.makeText(mContext, "입력필드가 비어있습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();

                EventInfo item = new EventInfo();
                item.setStrEventTitle(title);
                item.setStrEventContent(content);
                item.setStrEventDate(_combineDate);

                if( !mIsAlreadyWrited ) {
                    mDBHelper.setInsertEventDB(title, content, _combineDate); // Insert Database
                    mEventInfos.add(item);
                    Toast.makeText(mContext, "이벤트 목록에 추가 되었습니다 !", Toast.LENGTH_SHORT).show();
                } else {
                    mDBHelper.setUpdateEventDB(title, content, _combineDate); // Update Database
                    for (int i = 0; i < mEventInfos.size(); i++)
                    {
                        if(mEventInfos.get(i).getStrEventDate().equals(_combineDate))
                            mEventInfos.set(i, item);
                    }
                    Toast.makeText(mContext, "이벤트 목록이 수정 되었습니다 !", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
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
