package m2u.eyelink.aibot.component;

import java.util.List;

import org.springframework.stereotype.Component;

import m2u.eyelink.aibot.domain.Keyboard;
import m2u.eyelink.aibot.domain.Message;
import m2u.eyelink.aibot.domain.MessageIn;
import m2u.eyelink.aibot.domain.MessageOut;

@Component
public class KakaoRespGenerator {

	public Keyboard createKakaoKeyboard(String type, List<String> buttons) {
		if (buttons != null && buttons.size() > 0 ){
			return new Keyboard(type, buttons);
		}
		return new Keyboard(type);
	}

	public MessageOut createKakaoMessage(MessageIn messageIn) {
		
		MessageOut result = new MessageOut();
		
		Message message = new Message();
		message.setText("Test message response.");
		result.setMessage(message);
		
		return result;
	}

}
