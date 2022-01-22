package jdh.test.api.controller.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdh.test.api.staticval.JSONReturnValue;
import jdh.test.api.util.encrypt.CipherUtil;
import jdh.test.api.util.encrypt.KeyComponent;

/**
 * @author 장대혁
 * @title public key 반환 api
 * @data 2022-01-22
 * @description public key를 반환하는 api
 */
@RestController
public class MsApiPublicKeyController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "pk", method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin
	public Map<String, Object> msApiPublicKeyGet(@RequestBody Map<String, Object> input, HttpServletRequest req, HttpSession session) throws Exception {
		//Return Json
		Map<String, Object> returnMap = new HashMap<String, Object>();
		logger.info("[msApiPublicKeyGet] public key 발급");
		
		returnMap.put("publicKey", CipherUtil.publicKeyToStr(KeyComponent.getPublicKey()));
		returnMap.put(JSONReturnValue.KEY_RESULT, JSONReturnValue.RESULT_SUCCESS);
		returnMap.put(JSONReturnValue.KEY_MSG, "public key 발급 완료");
		return returnMap;
	}
}
