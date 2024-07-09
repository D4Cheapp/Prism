package com.prism.messenger.component;

import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class WebSocketInspector extends HttpSessionHandshakeInterceptor {

  private final ProfileServiceImpl profileService;

  public WebSocketInspector(ProfileServiceImpl profileService) {
    this.profileService = profileService;
  }

  @Override
  public boolean beforeHandshake(ServerHttpRequest request, @NotNull ServerHttpResponse response,
      @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes)
      throws Exception {
    String email = Objects.requireNonNull(request.getPrincipal()).getName();
    profileService.setOnlineConnectedStatus(email);
    return super.beforeHandshake(request, response, wsHandler, attributes);
  }

  @EventListener
  public void onDisconnect(SessionDisconnectEvent session) {
    String email = Objects.requireNonNull(session.getUser()).getName();
    profileService.setOnlineDisconnectedStatus(email);
  }
}
