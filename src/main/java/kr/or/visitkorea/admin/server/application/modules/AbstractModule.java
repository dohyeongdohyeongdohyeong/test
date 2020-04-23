package kr.or.visitkorea.admin.server.application.modules;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONObject;

import com.google.gson.Gson;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.staff.session.SessionManager;

public abstract class AbstractModule implements Module {

	protected JSONObject parameterObject;
	protected JSONObject resultObject;
	private HttpServletRequest request;
	private HttpServletResponse response;
	protected SqlSession sqlSession;
	protected SessionManager sessionManager;

	{
		sessionManager = new SessionManager();
	}

	public static SqlSession openSqlSession() {
        return Call.getSqlSessionFactory().openSession(true);
    }

	public void init() {
	}
	
	@Override
	public void setParameter(JSONObject param) {
		this.parameterObject = param;
	}
	
	public JSONObject invoke() {

		resultObject = new JSONObject();
		resultObject.put("return.type", "application/json");

		JSONObject resultHeaderObject = new JSONObject();
		resultHeaderObject.put("process", "success");

		JSONObject resultBodyObject = new JSONObject();

		sqlSession = openSqlSession();

		try {
			beforeRun(resultHeaderObject, resultBodyObject); 
			run(resultHeaderObject, resultBodyObject);
			afterRun(resultHeaderObject, resultBodyObject);
			sqlSession.commit();
		} catch(Exception e){
			e.printStackTrace();
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
		
		resultObject.put("header", resultHeaderObject);
		resultObject.put("body", resultBodyObject);
		
		return resultObject;
	}

	protected JSONObject invokeModule(JSONObject commandObject) {
		try {
			
			return __invokeModule(commandObject);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	protected String getJsonString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj); //convert 
	}

	protected HttpServletRequest getRequest() {
		return this.request;
	}
	
	protected HttpServletResponse getResponse() {
		return this.response;
	}
	
	private JSONObject __invokeModule(JSONObject obj) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		String commandId = obj.getString("cmd");
		AbstractModule tgrModule = (AbstractModule) getClass(commandId).newInstance();
		if (tgrModule == null) return null;
		tgrModule.setParameter(obj);
		return tgrModule.invoke();
		
	}

	private Class<Module> getClass(String commandId) throws ClassNotFoundException {

		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile(String.format("//business[@id='%s']", commandId), Filters.element());
		Element el = (Element) xp.evaluateFirst(Call.getBusinessDocument());
		
		String targetClassValue = el.getValue();
		if (targetClassValue != null) {
			return (Class<Module>) Class.forName(targetClassValue);
		}else {
			return null;
		}
	}
	abstract protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject); 
	
	abstract protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject);

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public String getLoginStaffIp(HttpServletRequest request) {
        String ip = null;
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() ==  0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        return ip;
	}
	
}