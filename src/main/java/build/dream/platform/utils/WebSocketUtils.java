package build.dream.platform.utils;

import org.apache.commons.collections4.MapUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {
    private static final ConcurrentHashMap<String, WebSocketSession> SESSION_ID_WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<String, WebSocketSession>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> TENANT_ID_WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>>();
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>> TENANT_ID_BRANCH_ID_WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<String, ConcurrentHashMap<String, WebSocketSession>>();
    private static final ConcurrentHashMap<String, WebSocketSession> TENANT_ID_BRANCH_ID_USER_ID_WEB_SOCKET_SESSION_MAP = new ConcurrentHashMap<String, WebSocketSession>();

    public static void addWebSocketSession(String tenantId, String branchId, String userId, String sessionId, WebSocketSession webSocketSession) {
        SESSION_ID_WEB_SOCKET_SESSION_MAP.put(sessionId, webSocketSession);
        ConcurrentHashMap<String, WebSocketSession> tenantIdConcurrentHashMap = TENANT_ID_WEB_SOCKET_SESSION_MAP.get(tenantId);
        if (MapUtils.isEmpty(tenantIdConcurrentHashMap)) {
            tenantIdConcurrentHashMap = new ConcurrentHashMap<String, WebSocketSession>();
            TENANT_ID_WEB_SOCKET_SESSION_MAP.put(tenantId, tenantIdConcurrentHashMap);
        }
        tenantIdConcurrentHashMap.put(branchId + "_" + userId, webSocketSession);

        ConcurrentHashMap<String, WebSocketSession> tenantIdBranchIdConcurrentHashMap = TENANT_ID_BRANCH_ID_WEB_SOCKET_SESSION_MAP.get(tenantId + "_" + branchId);
        if (MapUtils.isEmpty(tenantIdBranchIdConcurrentHashMap)) {
            tenantIdBranchIdConcurrentHashMap = new ConcurrentHashMap<String, WebSocketSession>();
            TENANT_ID_BRANCH_ID_WEB_SOCKET_SESSION_MAP.put(tenantId + "_" + branchId, tenantIdBranchIdConcurrentHashMap);
        }
        tenantIdBranchIdConcurrentHashMap.put(userId, webSocketSession);

        TENANT_ID_BRANCH_ID_USER_ID_WEB_SOCKET_SESSION_MAP.put(tenantId + "_" + branchId + "_" + userId, webSocketSession);
    }

    public static void removeWebSocketSession(String tenantId, String branchId, String userId, String sessionId) {
        SESSION_ID_WEB_SOCKET_SESSION_MAP.remove(sessionId);
        TENANT_ID_WEB_SOCKET_SESSION_MAP.get(tenantId).remove(branchId + "_" + userId);
        TENANT_ID_BRANCH_ID_WEB_SOCKET_SESSION_MAP.get(tenantId + "_" + branchId).remove(userId);
        TENANT_ID_BRANCH_ID_USER_ID_WEB_SOCKET_SESSION_MAP.remove(tenantId + "_" + branchId + "_" + userId);
    }

    public static void sendMessage(String sessionId, TextMessage textMessage) throws IOException {
        WebSocketSession webSocketSession = SESSION_ID_WEB_SOCKET_SESSION_MAP.get(sessionId);
        webSocketSession.sendMessage(textMessage);
    }
}
