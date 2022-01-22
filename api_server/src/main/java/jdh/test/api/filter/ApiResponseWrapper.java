package jdh.test.api.filter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jdh.test.api.util.encrypt.AES256;

/**
 * @author 장대혁
 * @title api 요청 데이터 반환 관련 response wrapper
 * @date 2022-01-22
 * @description api 요청 데이터 반환 시 response 내부 데이터 filter를 위한 wrapper 클래스
 *              1. response data 조회
 *              2. return data를 AES256 키로 암호화
 */
public class ApiResponseWrapper extends HttpServletResponseWrapper{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	ByteArrayOutputStream output;
	FilterServletOutputStream filterOutput;

	@SuppressWarnings("deprecation")
	public ApiResponseWrapper(HttpServletResponse response, String responseStr, String aesKey) throws IOException, NoSuchAlgorithmException, GeneralSecurityException {
		super(response);
		output = new ByteArrayOutputStream();
		DataOutputStream out = null;
		try {
			// 1. response data 조회
			logger.info("[ApiResponseWrapper] 1. response data 조회");
			JsonParser parser = new JsonParser();
			Object obj = parser.parse(responseStr);
			JsonObject responseJson = (JsonObject) obj;
			
			// 2. return data 암호화
			logger.info("[ApiResponseWrapper] 2. return data 암호화");
			logger.info("[ApiResponseWrapper] data :: " + responseJson.get("data").toString());
			logger.info("[ApiResponseWrapper] data 길이 :: " + responseJson.get("data").toString().length());
			String returnData = AES256.encrypt(aesKey, responseJson.get("data").toString());
			responseJson.addProperty("data", returnData);
			logger.info("[ApiResponseWrapper] 암호화 된 returnData :: " + returnData);
			
			out = new DataOutputStream(output);
			out.write(responseJson.toString().getBytes());
			output.flush();
			output.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			out.close();
			output.close();
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (filterOutput == null) {
			filterOutput = new FilterServletOutputStream(output);
		}
		return filterOutput;
	}

	public byte[] getDataStream() {
		return output.toByteArray();
	}

}
