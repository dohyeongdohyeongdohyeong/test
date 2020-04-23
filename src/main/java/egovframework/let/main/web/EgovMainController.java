package egovframework.let.main.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 템플릿 메인 페이지 컨트롤러 클래스(Sample 소스)
 * @author 실행환경 개발팀 JJY
 * @since 2011.08.31
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.08.31  JJY            최초 생성
 *
 * </pre>
 */
@Controller
public class EgovMainController {

	/**
	 * 템플릿 메인 페이지 조회
	 * @return 메인페이지 정보 Map [key : 항목명]
	 *
	 * @param request
	 * @param model
	 * @exception Exception Exception
	 */
	@RequestMapping(value = "/mainPage.do")
	public String getMgtMainPage(HttpServletRequest request, ModelMap model)
	  throws Exception{
		return "main/EgovMainView";
	}
	@RequestMapping(value = "/login.do")
	public String getLoginPage(HttpServletRequest request, ModelMap model)
	  throws Exception{
		return "main/login";
	}
	
	@RequestMapping(value = "/info_page.do")
	public String getLoginHtml(HttpServletRequest request, ModelMap model)
	  throws Exception{
		return "main/info_page";
	}
	
	
}