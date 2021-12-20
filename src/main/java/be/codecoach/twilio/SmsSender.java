package be.codecoach.twilio;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("application-twilio.properties")
public class SmsSender {



    @Value("${TWILIO_ACCOUNT_SID}")
    public String ACCOUNT_SID;
    @Value("${TWILIO_AUTH_TOKEN}")
    public String AUTH_TOKEN;

    public void sendMessage() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        /*Message message = Message.creator(
                        new PhoneNumber("+32472491759"),
                        new PhoneNumber("+15855968249"),
                        "Where's Wallace?")
                .create();
        */
        System.out.println(ACCOUNT_SID);
        System.out.println(AUTH_TOKEN);
    }

}
