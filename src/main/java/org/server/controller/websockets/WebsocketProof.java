package org.server.controller.websockets;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.server.config.shared.Websocket;

import java.util.logging.Logger;

@Websocket
@ServerEndpoint("/web") // Esto me da el endpoint "ws://localhost:8080/web"
public class WebsocketProof {
    private static final Logger LOGGER = Logger.getLogger(WebsocketProof.class.getName());

    static {
        LOGGER.info("In WebsocketProof");
    }

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Mensaje recibido: " + message);
        try {
            session.getBasicRemote().sendText("Eco: " + message);
        } catch (Exception e) {
            LOGGER.info("Error sending recibido: " + e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Conexión cerrada: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error en sesión " + session.getId() + ": " + throwable.getMessage());
    }
}
