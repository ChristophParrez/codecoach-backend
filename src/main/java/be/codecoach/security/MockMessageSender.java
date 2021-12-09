package be.codecoach.security;

import be.codecoach.security.authentication.user.event.Event;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"test", "development", "production"})
@Component
public class MockMessageSender implements MessageSender {

    @Override
    public void handle(Event event) {
        //Send your messages here...
    }
}
