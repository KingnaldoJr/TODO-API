package dev.rmjr.todo.event;

import dev.rmjr.todo.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@Builder
public class OnUserRegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String appUrl;

    public OnUserRegistrationCompleteEvent(User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }
}
