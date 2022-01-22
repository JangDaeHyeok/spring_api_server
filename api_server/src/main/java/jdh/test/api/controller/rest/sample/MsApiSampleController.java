package jdh.test.api.controller.rest.sample;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdh.test.api.staticval.JSONReturnValue;
import jdh.test.api.util.etc.ValidateUtil;

/**
 * @author 장대혁
 * @title api sample
 * @data 2022-01-22
 * @description api sample로 RequestBody를 사용하여 파라미터 세팅 및
 *              데이터 반환을 똑같이 수행하면 ApiCustomFilter에서 자동으로 데이터 암복호화 진행
 */
@RestController
public class MsApiSampleController {
	// private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping(value = "/api/msg")
	@ResponseBody
	@CrossOrigin
	public Map<String, Object> msApiTestGet(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpSession session) throws Exception {
		//Return Json
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String returnStr = "안녕하세요.";
		
		// s :: set parameter :: //
		if(input.get("name") != null && !input.get("name").toString().equals("")) {
			returnStr += " " + input.get("name").toString() + "님 반갑습니다.";
		}
		// e :: set parameter :: //
		
		// s :: set return :: //
		returnMap.put(JSONReturnValue.KEY_DATA, returnStr);
		returnMap.put(JSONReturnValue.KEY_RESULT, JSONReturnValue.RESULT_SUCCESS);
		returnMap.put(JSONReturnValue.KEY_MSG, "메세지 조회 성공");
		// e :: set return :: //
		return returnMap;
	}
	
	@PostMapping(value = "/api/test")
	@ResponseBody
	@CrossOrigin
	public Map<String, Object> msApiStrGet(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpSession session) throws Exception {
		//Return Json
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int cnt = 0;
		String returnStr = "";
		
		// s :: set parameter :: //
		if(ValidateUtil.notEmpty(input.get("count"))) {
			cnt = Integer.parseInt(input.get("count").toString());
		}
		// e :: set parameter :: //
		
		for(int i = 1; i <= cnt; i++) {
			returnStr += "A";
		}
		
		// s :: set return :: //
		returnMap.put(JSONReturnValue.KEY_DATA, returnStr);
		returnMap.put(JSONReturnValue.KEY_RESULT, JSONReturnValue.RESULT_SUCCESS);
		returnMap.put(JSONReturnValue.KEY_MSG, "테스트 조회 성공");
		// e :: set return :: //
		return returnMap;
	}
}
