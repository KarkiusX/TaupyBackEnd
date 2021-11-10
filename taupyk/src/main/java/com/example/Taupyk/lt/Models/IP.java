package com.example.Taupyk.lt.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class IP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long UId;

    private String IP;

    private int viewCount;

    private long timeStamp;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public IP(){};

    public String getIP() {
        return IP;
    }

    public long getTimeStampTime()
    {
        Instant instant = Instant.now();
        return instant.getEpochSecond();
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
