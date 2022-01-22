package jdh.test.api.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jdh.test.api.staticval.JSONReturnValue;
import jdh.test.api.util.encrypt.AES256;
import jdh.test.api.util.encrypt.CipherUtil;
import jdh.test.api.util.encrypt.KeyComponent;
import jdh.test.api.util.etc.JsonUtil;

/**
 * @author 장대혁
 * @title api 요청접수 관련 request wrapper
 * @date 2022-01-22
 * @description api 요청접수 시 request 내부 데이터 filter를 위한 wrapper 클래스
 *              1. 개인키로 AES256 키 복호화
 *              2. AES256 키로 인증 토큰 복호화 및 토큰 일치여부 체크
 *              3. 토큰이 일치하는 경우 요청 input data를 AES256 키로 복호화
 */
public class ApiRequestWrapper extends HttpServletRequestWrapper {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String API_TOKEN = "api-test-token";
	
	private Map<String, String> customHeaders;
	private byte[] bodyData;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public ApiRequestWrapper(HttpServletRequest request) throws IOException, GeneralSecurityException {
		super(request);
		this.customHeaders = new HashMap<String, String>();
		String body = cleanXSS(getBody(request));
		
		JsonParser parser = new JsonParser();
		Object obj = null;
		JsonObject jsonObj = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> reqMap = null;
		Map<String, Object> inputMap = null;
		
		// request data json 타입으로 변환
		obj = parser.parse(body);
		jsonObj = (JsonObject) obj;
		// key, token, data
		reqMap = mapper.readValue(jsonObj.toString(), Map.class);
		
		// 1. 개인키로 AES256 키 복호화
		logger.info("[ApiRequestWrapper] 1. 개인키로 AES256 키 복호화");
		String aesKey = CipherUtil.decryptRSA(reqMap.get("key").toString(), KeyComponent.getPrivateKey());
		logger.info("[ApiRequestWrapper] aesKey :: {}", aesKey);
		// AES256 키 헤더 저장
		putHeader("aesKey", aesKey);
		
		// 2. AES키로 토큰 복호화 및 토큰 일치여부 체크
		logger.info("[ApiRequestWrapper] 2. AES키로 토큰 복호화 및 토큰 일치여부 체크");
		String token = AES256.decrypt(aesKey, reqMap.get("token").toString());
		logger.info("[ApiRequestWrapper] token :: {}", token);
		if(!token.equals(API_TOKEN)) {
			reqMap = new HashMap<String, Object>();
			reqMap.put(JSONReturnValue.KEY_RESULT, JSONReturnValue.RESULT_FAIL);
			reqMap.put(JSONReturnValue.KEY_MSG, "토큰 불일치");
			JSONObject data = JsonUtil.getJsonStringFromMap(reqMap);
			bodyData = data.toJSONString().getBytes("utf-8");
			logger.error("[ApiRequestWrapper] 토큰 불일치");
			
			putHeader("tokenFl", "fail");
		}
		
		// 3. 토큰이 일치하는 경우 input data 복호화
		else {
			logger.info("[ApiRequestWrapper] 3. 토큰이 일치하는 경우 input data 복호화");
			String inputData = AES256.decrypt(aesKey, reqMap.get("data").toString());
			logger.info("[ApiRequestWrapper] inputData :: {}", inputData);
			mapper = new ObjectMapper();
			inputMap = mapper.readValue(inputData, Map.class);
			Gson gson = new Gson();
			JsonObject inputObject = gson.toJsonTree(inputMap).getAsJsonObject();
			bodyData = inputObject.toString().getBytes("utf-8");
		}
		
	}

	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(bodyData);
		return new ServletInputStreamImpl(bis);
	}

	class ServletInputStreamImpl extends ServletInputStream {
		private InputStream is;

		public ServletInputStreamImpl(InputStream bis) {
			is = bis;
		}

		public int read() throws IOException {
			return is.read();
		}

		public int read(byte[] b) throws IOException {
			return is.read(b);
		}

		@Override
		public boolean isFinished() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isReady() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			// TODO Auto-generated method stub
			
		}

	}

	private String getBody(HttpServletRequest request) throws IOException {
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
				char[] charBuffer = new char[128];
				int bytesRead = -1;

				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}

		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();

		return body;
	}

	private String cleanXSS(String value) {
		value = value.replaceAll("(?i)script", "");
		value = value.replaceAll("(?i)expression", "no_expression");
		value = value.replaceAll("(?i)iframe", "no_iframe");
		value = value.replaceAll("(?i)object", "no_object");
		value = value.replaceAll("(?i)embed", "no_embed");
		value = value.replaceAll("(?i)document.cookie", "");
		value = value.replaceAll("(?i)onabort", "no_onabort");
		value = value.replaceAll("(?i)onactivate", "no_onactivate");
		value = value.replaceAll("(?i)onafterprint", "no_onafterprint");
		value = value.replaceAll("(?i)onafterupdate", "no_onafterupdate");
		value = value.replaceAll("(?i)onbeforeactivate", "no_onbeforeactivate");
		value = value.replaceAll("(?i)onbeforecopy", "no_onbeforecopy");
		value = value.replaceAll("(?i)onbeforecut", "no_onbeforecut");
		value = value.replaceAll("(?i)onbeforedeactivate", "no_onbeforedeactivate");
		value = value.replaceAll("(?i)onbeforeeditfocus", "no_onbeforeeditfocus");
		value = value.replaceAll("(?i)onbeforepaste", "no_onbeforepaste");
		value = value.replaceAll("(?i)onbeforeprint", "no_onbeforeprint");
		value = value.replaceAll("(?i)onbeforeunload", "no_onbeforeunload");
		value = value.replaceAll("(?i)onbeforeupdate", "no_onbeforeupdate");
		value = value.replaceAll("(?i)onblur", "no_onblur");
		value = value.replaceAll("(?i)onbounce", "no_onbounce");
		value = value.replaceAll("(?i)oncellchange", "no_oncellchange");
		value = value.replaceAll("(?i)onchange", "no_onchange");
		value = value.replaceAll("(?i)onclick", "no_onclick");
		value = value.replaceAll("(?i)oncontextmenu", "no_oncontextmenu");
		value = value.replaceAll("(?i)oncontrolselect", "no_oncontrolselect");
		value = value.replaceAll("(?i)oncopy", "no_oncopy");
		value = value.replaceAll("(?i)oncut", "no_oncut");
		value = value.replaceAll("(?i)ondataavailable", "no_ondataavailable");
		value = value.replaceAll("(?i)ondatasetchanged", "no_ondatasetchanged");
		value = value.replaceAll("(?i)ondatasetcomplete", "no_ondatasetcomplete");
		value = value.replaceAll("(?i)ondblclick", "no_ondblclick");
		value = value.replaceAll("(?i)ondeactivate", "no_ondeactivate");
		value = value.replaceAll("(?i)ondrag", "no_ondrag");
		value = value.replaceAll("(?i)ondragend", "no_ondragend");
		value = value.replaceAll("(?i)ondragenter", "no_ondragenter");
		value = value.replaceAll("(?i)ondragleave", "no_ondragleave");
		value = value.replaceAll("(?i)ondragover", "no_ondragover");
		value = value.replaceAll("(?i)ondragstart", "no_ondragstart");
		value = value.replaceAll("(?i)ondrop", "no_ondrop");
		value = value.replaceAll("(?i)onerror", "no_onerror");
		value = value.replaceAll("(?i)onerrorupdate", "no_onerrorupdate");
		value = value.replaceAll("(?i)onfilterchange", "no_onfilterchange");
		value = value.replaceAll("(?i)onfinish", "no_onfinish");
		value = value.replaceAll("(?i)onfocus", "no_onfocus");
		value = value.replaceAll("(?i)onfocusin", "no_onfocusin");
		value = value.replaceAll("(?i)onfocusout", "no_onfocusout");
		value = value.replaceAll("(?i)onhelp", "no_onhelp");
		value = value.replaceAll("(?i)onkeydown", "no_onkeydown");
		value = value.replaceAll("(?i)onkeypress", "no_onkeypress");
		value = value.replaceAll("(?i)onkeyup", "no_onkeyup");
		value = value.replaceAll("(?i)onlayoutcomplete", "no_onlayoutcomplete");
		value = value.replaceAll("(?i)onload", "no_onload");
		value = value.replaceAll("(?i)onlosecapture", "no_onlosecapture");
		value = value.replaceAll("(?i)onmousedown", "no_onmousedown");
		value = value.replaceAll("(?i)onmouseenter", "no_onmouseenter");
		value = value.replaceAll("(?i)onmouseleave", "no_onmouseleave");
		value = value.replaceAll("(?i)onmousemove", "no_onmousemove");
		value = value.replaceAll("(?i)onmouseout", "no_onmouseout");
		value = value.replaceAll("(?i)onmouseover", "no_onmouseover");
		value = value.replaceAll("(?i)onmouseup", "no_onmouseup");
		value = value.replaceAll("(?i)onmousewheel", "no_onmousewheel");
		value = value.replaceAll("(?i)onmove", "no_onmove");
		value = value.replaceAll("(?i)onmoveend", "no_onmoveend");
		value = value.replaceAll("(?i)onmovestart", "no_onmovestart");
		value = value.replaceAll("(?i)onpaste", "no_onpaste");
		value = value.replaceAll("(?i)onpropertychange", "no_onpropertychange");
		value = value.replaceAll("(?i)onreadystatechange", "no_onreadystatechange");
		value = value.replaceAll("(?i)onreset", "no_onreset");
		value = value.replaceAll("(?i)onresize", "no_onresize");
		value = value.replaceAll("(?i)onresizeend", "no_onresizeend");
		value = value.replaceAll("(?i)onresizestart", "no_onresizestart");
		value = value.replaceAll("(?i)onrowenter", "no_onrowenter");
		value = value.replaceAll("(?i)onrowexit", "no_onrowexit");
		value = value.replaceAll("(?i)onrowsdelete", "no_onrowsdelete");
		value = value.replaceAll("(?i)onrowsinserted", "no_onrowsinserted");
		value = value.replaceAll("(?i)onscroll", "no_onscroll");
		value = value.replaceAll("(?i)onselect", "no_onselect");
		value = value.replaceAll("(?i)onselectionchange", "no_onselectionchange");
		value = value.replaceAll("(?i)onselectstart", "no_onselectstart");
		value = value.replaceAll("(?i)onstart", "no_onstart");
		value = value.replaceAll("(?i)onstop", "no_onstop");
		value = value.replaceAll("(?i)onsubmit", "no_onsubmit");
		value = value.replaceAll("(?i)onunload", "no_onunload");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("[\\\"\\\'][\\s]*vbscript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("\\<script\\>", "").replaceAll("\\<\\/script\\>", "");
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");

		return value;
	}
	
	public void putHeader(String name, String value){
		this.customHeaders.put(name, value);
	}
	
	public String getHeader(String name) {
		String headerValue = customHeaders.get(name);
		
		if (headerValue != null){
			return headerValue;
		}
		return ((HttpServletRequest) getRequest()).getHeader(name);
	}
}
