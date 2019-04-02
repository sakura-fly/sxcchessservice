package com.gao.ee;

import javax.jws.Oneway;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ServerEndpoint("/chess")
public class Socket {

    // private static Map<String, People> map = new HashMap<>();
    private static Set<Session> waite = new HashSet<>();
    // private static Set<Session> play = new HashSet<>();
    private static Map<Session, Session> room = new HashMap<>();

    @OnOpen
    public void open(Session session) {
        if (waite.size() > 0) {
            try {
                session.getBasicRemote().sendText("info,匹配到对手");
                Session d = (Session) waite.toArray()[0];
                waite.remove(d);
                // play.add(session);
                // play.add(d);
                room.put(d, session);
                room.put(session, d);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                session.getBasicRemote().sendText("info,暂无空闲，等待匹配");
                waite.add(session);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println("ccc");
    }

    @OnClose
    public void close() {

    }

    @OnMessage
    public void message(String message, Session session) {

        String[] msg = message.split(",");
        if (msg[0].equals("conet")) {
            // if (map.keySet().contains(msg[1])) {
            //     push(session, "err,昵称存在");
            // } else {
            //     push(session, "sw,进入大厅，等待匹配中");
            //     People p = new People();
            //     p.setIsPlay(0);
            //     p.setSession(session);
            //     map.put(msg[1], p);
            // }
            // for (String k : map.keySet()){
            //     System.out.println(map.get(k).getSession() == session);
            // }
        } else {
            push(room.get(session), message);
        }
        System.out.println(message);
        // push(session, message);
    }

    @OnError
    public void onError(Throwable t, Session session) throws Throwable {
        // t.printStackTrace();
        // System.out.println(map.keySet());
        // for (String k : map.keySet()) {
        //     if (map.get(k).getSession() == session) {
        //         map.remove(map.get(k));
        //     }
        // }
        // waite.remove(session);
        Session d = room.remove(session);
        if (d != null) {
            d.getBasicRemote().sendText("err,你的对手掉线");
            room.remove(d);
        } else {
            waite.remove(session);
        }
        System.out.println("WebSocket服务端错误 " + t);

    }

    private void push(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
