package com.gyb.bookring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
        * WebSocketServer
        *
        * @author lihao
        */
@ServerEndpoint("/server/webrtc/{userId}")
@Component
public class WebSocketServer {

    //private RedisTemplate redisTemplate =new RedisTemplate();

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketServer>> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";
    /**
     * 接收sessionId
     */
    private String sessionId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        this.sessionId = session.getId();
        if (webSocketMap.containsKey(userId)) {
            ConcurrentHashMap<String,WebSocketServer> userSocketServers = webSocketMap.get(userId);
            if (userSocketServers.containsKey(sessionId)){
                userSocketServers.remove(sessionId);
            }else{
                addOnlineCount();
            }
            userSocketServers.put(sessionId,this);
        } else {
            ConcurrentHashMap<String,WebSocketServer> userSocketServers = new ConcurrentHashMap<>();
            userSocketServers.put(sessionId, this);
            webSocketMap.put(userId,userSocketServers);
            //在线数加1
            addOnlineCount();
        }

        System.out.printf("\n用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            ConcurrentHashMap<String,WebSocketServer> userSocketServers = webSocketMap.get(userId);
            if(userSocketServers.containsKey(sessionId)){
                userSocketServers.remove(sessionId);
                subOnlineCount();
            }
            if(userSocketServers.isEmpty()){
                webSocketMap.remove(userId);
            }
        }
        System.out.printf("\n用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.printf("\n用户消息:" + userId + ",报文:" + message);
//        sendMessage("{type:\'reply\',message:\'replay for message "+message+"\'}");
        JSONObject messageObj = JSON.parseObject(message);
        System.out.println(messageObj.getString("type"));
        if("signal".equals(messageObj.getString("type"))){
            String socket_to = messageObj.getString("socket_to");
            ConcurrentHashMap<String,WebSocketServer> userSocketServers = webSocketMap.get(userId);
            if(userSocketServers.size()>1){
                messageObj.put("socket_to",sessionId);
                if("-1".equals(socket_to)){
                    userSocketServers.forEach(((brotherSessionId, webSocketServer) -> {
                    if(!sessionId.equals(brotherSessionId)){
                        try {
                            webSocketServer.sendMessage(messageObj.toJSONString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
                }else{
                userSocketServers.get(socket_to).sendMessage(messageObj.toJSONString());
                }
            }
        }else if("sockets".equals(messageObj.getString("type"))){
            JSONObject sockets = new JSONObject();
            sockets.put("all",webSocketMap.get(userId).keys());
            sockets.put("current",sessionId);
            sockets.put("type","sockets");
            sendMessage(sockets.toJSONString());
        }
        //可以群发消息
        //消息保存到数据库、redis
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.printf("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        System.out.printf("发送消息到:" + userId + "，报文:" + message);
        System.out.printf("用户" + userId + ",不在线！");

    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }
}
