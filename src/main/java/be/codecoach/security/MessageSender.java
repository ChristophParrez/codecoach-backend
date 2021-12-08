package be.codecoach.security;

import be.codecoach.security.authentication.user.event.Event;

public interface MessageSender {
    void handle(Event event);
}
