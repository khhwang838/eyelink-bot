package m2u.eyelink.aibot.component;

import java.util.List;

import org.springframework.stereotype.Component;

import m2u.eyelink.aibot.IConstants;
import m2u.eyelink.aibot.domain.Keyboard;
import m2u.eyelink.aibot.domain.Message;
import m2u.eyelink.aibot.domain.MessageOut;

@Component
public class KakaoRespGenerator {

	public Keyboard createKakaoKeyboard(List<String> buttons) {
		if (buttons != null && buttons.size() > 0 ){
			return new Keyboard(IConstants.Type.Keyboard.BUTTONS, buttons);
		}
		return new Keyboard(IConstants.Type.Keyboard.TEXT);
	}

	public MessageOut createKakaoMessage(String response) {
		
		MessageOut result = new MessageOut();
		
		Message message = new Message();
		message.setText(response);
		result.setMessage(message);
		
		return result;
	}

}
