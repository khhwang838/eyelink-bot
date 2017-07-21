package m2u.eyelink.aibot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import m2u.eyelink.aibot.component.KakaoRespGenerator;
import m2u.eyelink.aibot.domain.Friend;
import m2u.eyelink.aibot.domain.Keyboard;
import m2u.eyelink.aibot.domain.MessageIn;
import m2u.eyelink.aibot.domain.MessageOut;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private static final String programDURL = "http://m2u-parstream.eastus.cloudapp.azure.com:9507/ProgramD/GetBotResponse?input=";
	
	@Autowired
	private KakaoRespGenerator kakaoRespGenerator;
	
	/**
	 * 사용자가 채팅방에 최초로 들어오거나 재진입했을 때 호출되는 서비스
	 * <p>이용자가 최초로 채팅방에 들어올 때 기본으로 키보드 영역에 표시될 자동응답 명령어의 목록을 호출하는 API입니다.
	 * 챗팅방을 지우고 다시 재 진입시에도 호출됩니다. 다만 카카오 서버에서도 1분 동안 캐시가 저장되기 때문에 유저가 채팅방을 지우고 들어오는 행동을 반복하더라도 개발사 서버를 1분에 한번씩 호출하게 됩니다.
	 * <p>유저가 자동응답으로 메시지를 주고 받았을 경우는 마지막 메시지에 담겨있던 자동응답 명령어 목록이 표시됩니다. 다만 메시지에 저장된 자동응답 명령어는 10분간 유효합니다. 10분이 지난 다음에는 다시 Keyboard api를 호출하여 자동응답 목록을 초기화하게 됩니다
	 * @return
	 */
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	@ResponseBody
	public Keyboard getKeyboard() {
		
		logger.info("Getting keyboard...");
		
		String type = "text";
		
		Keyboard result = kakaoRespGenerator.createKakaoKeyboard(type, null);
		
		return result;
	}
	
	/**
	 * 사용자가 플러스 친구 대화방에서 메시지를 입력했을 때 호출되는 서비스
	 * @param messageIn
	 * @return
	 */
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	@ResponseBody
	public MessageOut getMessage(@RequestBody MessageIn messageIn) {
		
		logger.info("messageIn : {}", messageIn);
		
		
		// TODO : Program D API 호출하기
		// URL : http://m2u-parstream.eastus.cloudapp.azure.com:9507/ProgramD/GetBotResponse?input={msg}
		
		// !! 텍스트 타입이라고 가정
		String response = getResponse(messageIn.getContent());
		
		logger.info("response : {}", response);
		
		MessageOut result = kakaoRespGenerator.createKakaoMessage(response);
		
		return result;
	}
	
	private String getResponse(String content) {
		
		StringBuffer sb = null;
		 try {

			URL url = new URL(programDURL + content);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			
			logger.info("Output from Server .... \n");
			
			sb = new StringBuffer();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			logger.info(">>>>> {}", sb.toString());
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 사용자가 플러스 친구를 추가했을 때 호출되는 서비스
	 * @param friend
	 */
	@RequestMapping(value = "/friend", method = RequestMethod.POST)
	@ResponseBody
	public void friendAdded(@RequestBody Friend friend) {
		
		logger.info("You have got a new friend. user_key : {}", friend.getUser_key());
		
	}
	
	/**
	 * 사용자가 플러스 친구를 차단했을 때 호출 되는 서비스
	 * @param user_key
	 */
	@RequestMapping(value = "/friend/{user_key}", method = RequestMethod.DELETE)
	@ResponseBody
	public void friendBlockedMe(@PathVariable("user_key")String user_key) {
		
		logger.info("A friend blocked you. user_key : {}", user_key);
		
	}
	
	/**
	 * 사용자가 대화방에서 나갔을 때 호출되는 서비스
	 * @param user_key
	 */
	@RequestMapping(value = "/chat_room/{user_key}", method = RequestMethod.DELETE)
	@ResponseBody
	public void friendLeftChatRoom(@PathVariable("user_key")String user_key) {
		
		logger.info("A friend left the chat room. user_key : {}", user_key);
		
	}
}
