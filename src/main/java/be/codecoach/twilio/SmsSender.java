package be.codecoach.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    public String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    public void sendMessage(String telephoneNumber, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new PhoneNumber(telephoneNumber),
                        new PhoneNumber("+15855968249"),
                        message)
                .create();
    }

}
