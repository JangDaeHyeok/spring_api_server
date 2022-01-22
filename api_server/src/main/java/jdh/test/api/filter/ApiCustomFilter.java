package jdh.test.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * @author 장대혁
 * @title api 요청접수 시 실행되는 filter
 * @date 2022-01-22
 * @description api 요청접수 시 요청 파라미터 복호화 관련 ApiRequestWrapper 와 반환 데이터 암호화 관련 ApiResponseWrapper 실행
 *              /api/** URL 요청 시에만 실행
 */
@WebFilter(urlPatterns = "/api/*")
public class ApiCustomFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// s :: Request Filter 실행 :: //
		logger.info("=====[ApiCustomFilter] BEFORE Filter 실행=====");
		
		ApiRequestWrapper apiRequestWrapper = null;
		try {
			apiRequestWrapper = new ApiRequestWrapper((HttpServletRequest) request);
		} catch (Exception e) {
			logger.error("[ApiCustomFilter] request wrapper 설정 중 오류 발생 : " + e.getMessage());
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류 발생");
		}
		
		// response data wrapping 객체 생성
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper((HttpServletResponse) response);
		
		// request filter에서 token 불일치가 나온 경우
		if(apiRequestWrapper.getHeader("tokenFl") != null && apiRequestWrapper.getHeader("tokenFl").toString().equals("fail")) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "token 불일치");
			return;
		}
		
		// request header에서 AES256 키 조회
		String aesKey = apiRequestWrapper.getHeader("aesKey");
		
		logger.info("=====[ApiCustomFilter] BEFORE Filter 종료=====");
		// e :: Request Filter 실행 :: //
		
		chain.doFilter(apiRequestWrapper, responseCacheWrapperObject);
		
		// s :: Response Filter 실행 :: //
		logger.info("=====[ApiCustomFilter] AFTER Filter 실행=====");
		
		byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
		String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());
		try {
			response.getOutputStream().write(new ApiResponseWrapper(responseCacheWrapperObject, responseStr, aesKey).getDataStream());
			response.getOutputStream().close();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("[ApiCustomFilter] response wrapper 설정 중 오류 발생 : " + e.getMessage());
			return;
		}
		
		logger.info("=====[ApiCustomFilter] AFTER Filter 종료=====");
		// e :: Response Filter 실행 :: //
	}

	public void destroy() {
		this.filterConfig = null;
	}

}
