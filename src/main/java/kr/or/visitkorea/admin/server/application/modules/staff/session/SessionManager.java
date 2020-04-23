package kr.or.visitkorea.admin.server.application.modules.staff.session;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class SessionManager implements ApplicationContextAware, 
									HttpSessionAttributeListener, HttpSessionListener {

	private static SessionManager sessionManager;
	private static ConcurrentHashMap<String, Object> sessionMap = new ConcurrentHashMap<>();
	
	static {
		sessionManager = new SessionManager();
	}
	
	public static SessionManager getInstance() {
		return sessionManager;
	}
	
	public boolean isContains(String id) {
		return sessionMap.get(id) != null;
	}
	
	public void sessionCreate(String id, HttpSession session) {
		sessionMap.put(id, session);
	}
	
	public void sessionDestroy(String id) {
		if (isContains(id)) {
			sessionMap.remove(id);
		}
	}

	public ConcurrentHashMap<String, Object> getSessionMap() {
		return SessionManager.sessionMap;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		WebApplicationContext context = (WebApplicationContext) applicationContext;
		context.getServletContext().addListener(this);
	}
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		//	sessionCreated는 로그인 여부와 관계없이 Root 도메인으로 접속 시 생성됨으로 사용하지 않음
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		String usrId = (String) se.getSession().getAttribute("usrId");
		this.sessionDestroy(usrId);
	}	

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		if (event.getName().equals("usrId")) {
			System.out.println(" >> getName :: " + event.getName());
			System.out.println(" >> getValue :: " + event.getValue());
			this.sessionCreate((String) event.getValue(), event.getSession());
		}
		System.out.println("attributeAdded");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		if (event.getName().equals("usrId")) {
			System.out.println(" >> getName :: " + event.getName());
			System.out.println(" >> getValue :: " + event.getValue());
			this.sessionDestroy((String) event.getValue());
		}
		System.out.println("attributeRemoved");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		
	}
}
