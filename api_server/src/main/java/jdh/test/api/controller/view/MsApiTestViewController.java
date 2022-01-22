package jdh.test.api.controller.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

// api 성능 테스트 결과 view controller
@RestController
public class MsApiTestViewController {
	@RequestMapping(value = "/test/view", method = RequestMethod.GET)
	public ModelAndView apiTestView(@RequestParam Map<String, Object> input, HttpServletRequest req) throws Exception {
		ModelAndView mv = new ModelAndView("api");
		return mv;
	}
}
