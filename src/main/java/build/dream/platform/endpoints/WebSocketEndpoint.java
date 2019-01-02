/*
package build.dream.platform.endpoints;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/webSocket")
@Component
public class WebSocketEndpoint {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketEndpoint> webSocketEndpoints = new CopyOnWriteArraySet<WebSocketEndpoint>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    */
/**
     * 连接建立成功调用的方法
     *//*

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketEndpoints.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage(UUID.randomUUID().toString());
        } catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    */
/**
     * 连接关闭调用的方法
     *//*

    @OnClose
    public void onClose() {
        webSocketEndpoints.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    */
/**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     *//*

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        //群发消息
        for (WebSocketEndpoint webSocketEndpoint : webSocketEndpoints) {
            try {
                webSocketEndpoint.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(String message) {
        for (WebSocketEndpoint webSocketEndpoint : webSocketEndpoints) {
            try {
                webSocketEndpoint.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketEndpoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketEndpoint.onlineCount--;
    }
}
*/
