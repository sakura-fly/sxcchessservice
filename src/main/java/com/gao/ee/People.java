package com.gao.ee;

import javax.websocket.Session;

public class People {

    private Session session;
    private int isPlay;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }
}
