package com.hongdroid.customcalendar2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hongdroid.customcalendar2.model.EventInfo;

import java.util.ArrayList;

/**
 * 이벤트 데이터를 관리하는 데이터베이스
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hongdroid.db";

    public DBHelper(@Nullable Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // 데이터 베이스가 생성이 될 때 호출
        // 데이터베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS EventList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onCreate(db);
    }

    // SELECT 문 (이벤트 목록을 조회)
    public ArrayList<EventInfo> getSelectEventListDB() {
        ArrayList<EventInfo> eventInfos = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EventList ORDER BY writeDate DESC", null);
        if(cursor.getCount() != 0) {
            // 조회 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                EventInfo eventInfo = new EventInfo();
                eventInfo.setId(id);
                eventInfo.setStrEventTitle(title);
                eventInfo.setStrEventContent(content);
                eventInfo.setStrEventDate(writeDate);
                eventInfos.add(eventInfo);
            }
        }
        cursor.close();

        return eventInfos;
    }

    public ArrayList<EventInfo> getSelectedEventItem(String _writeDate) {
        ArrayList<EventInfo> eventInfos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM EventList WHERE writeDate = '" + _writeDate + "'", null);
        if(cursor.getCount() != 0) {
            // 조회 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                EventInfo eventInfo = new EventInfo();
                eventInfo.setId(id);
                eventInfo.setStrEventTitle(title);
                eventInfo.setStrEventContent(content);
                eventInfo.setStrEventDate(writeDate);
                eventInfos.add(eventInfo);
            }
        }
        cursor.close();
        return eventInfos;
    }

    // INSERT 문 (이벤트 목록을 DB 넣는다.)
    public void setInsertEventDB(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO EventList (title, content, writeDate) VALUES('" + _title + "','" + _content + "' , '" + _writeDate + "');");
    }

    // UPDATE 문 (이벤트 목록을 수정 한다.)
    public void setUpdateEventDB(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE EventList SET title='" + _title + "', content='" + _content + "' , writeDate='" + _writeDate + "' WHERE writeDate='" + _writeDate + "'");
    }

    // DELETE 문 (이벤트 목록을 제거 한다.)
    public void setDeleteEventDB(String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM EventList WHERE writeDate = '" + _writeDate + "'");
    }

}
