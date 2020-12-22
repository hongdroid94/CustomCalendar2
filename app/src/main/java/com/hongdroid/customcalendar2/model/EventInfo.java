package com.hongdroid.customcalendar2.model;

public class EventInfo
{
    private int id;
    private String strEventTitle;
    private String strEventContent;
    private String strEventDate;

    public EventInfo() { }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getStrEventTitle()
    {
        return strEventTitle;
    }

    public void setStrEventTitle(String strEventTitle)
    {
        this.strEventTitle = strEventTitle;
    }

    public String getStrEventContent()
    {
        return strEventContent;
    }

    public void setStrEventContent(String strEventContent)
    {
        this.strEventContent = strEventContent;
    }

    public String getStrEventDate()
    {
        return strEventDate;
    }

    public void setStrEventDate(String strEventDate)
    {
        this.strEventDate = strEventDate;
    }
}
