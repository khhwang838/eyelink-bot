package m2u.eyelink.aibot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import m2u.eyelink.aibot.component.KakaoRespGenerator;
import m2u.eyelink.aibot.domain.Keyboard;
import m2u.eyelink.aibot.domain.MessageIn;
import m2u.eyelink.aibot.domain.MessageOut;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private KakaoRespGenerator kakaoRespGenerator;
	
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	@ResponseBody
	public Keyboard getKeyboard() {
		
		logger.info("Getting keyboard...");
		
		String type = "text";
		
		Keyboard result = kakaoRespGenerator.createKakaoKeyboard(type, null);
		
		return result;
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	@ResponseBody
	public MessageOut getMessage(@RequestBody MessageIn messageIn) {
		
		logger.info("messageIn : {}", messageIn);
		
		MessageOut result = kakaoRespGenerator.createKakaoMessage(messageIn);
		
		return result;
	}
}
