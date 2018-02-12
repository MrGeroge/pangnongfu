package com.pangnongfu.server.web.endpoint;

/**
 * Author:zhangyu
 * create on 15/9/19.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/client/notification", configurator = SpringConfigurator.class)
public class NotificationEndpoint {
    private static Logger logger= LoggerFactory.getLogger(NotificationEndpoint.class);
    public final static int SEND_CODE_FAVORITE=1;//点赞
    public final static int SEND_CODE_COLLECT=2;//收藏
    public final static int SEND_CODE_COMMENT=3;//评论
    public final static int SEND_CODE_FOLLOW=4;//关注
    private static final Map<Long,NotificationEndpoint> connections=new ConcurrentHashMap<>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        String queryStr=session.getQueryString();

        if(!StringUtils.isEmpty(queryStr)){
            Long userId=Long.parseLong(queryStr);

            logger.info("open connection for userID "+String.valueOf(userId));

            this.session=session;
            connections.put(userId,this);
        }else{
            try {
                session.close();
            } catch (IOException e) {
                //不处理
            }
        }
    }

    @OnClose
    public void onClose() {

    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Notification Error: " + t.toString(), t);
    }

    public static void sendMessage(long userId,String message){
        NotificationEndpoint connection=connections.get(userId);

        if(connection==null){
            logger.info(String.format("no connection for userId %s",String.valueOf(userId)));
            return;
        }

        try {
            synchronized (connection) {
                connection.session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("send message Error: Failed to send message to client " + String.valueOf(userId), e);
            connections.remove(userId);
            try {
                connection.session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }
}
