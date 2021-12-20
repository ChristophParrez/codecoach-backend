package be.codecoach.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

public class SmsSender {
    @Value("${TWILIO_ACCOUNT_SID}")
    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    public static void main(String[] args) {
        /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:+32472491759"),
                        new PhoneNumber("whatsapp:+15855968249"),
                        "Where's Wallace?")
                .create();

        System.out.println(message.getSid());*/
        System.out.println(ACCOUNT_SID);
        System.out.println(AUTH_TOKEN);
    }

}
