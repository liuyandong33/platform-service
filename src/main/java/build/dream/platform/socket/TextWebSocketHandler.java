package build.dream.platform.socket;

import build.dream.platform.utils.WebSocketUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class TextWebSocketHandler extends org.springframework.web.socket.handler.TextWebSocketHandler {
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Map<String, Object> attributes = session.getAttributes();
        HttpSession httpSession = (HttpSession) attributes.get("httpSession");
        String sessionId = httpSession.getId();
        WebSocketUtils.removeWebSocketSession("1", "1", "1", sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        HttpSession httpSession = (HttpSession) attributes.get("httpSession");
        String sessionId = httpSession.getId();
        WebSocketUtils.addWebSocketSession("1", "1", "1", sessionId, session);
    }
}
