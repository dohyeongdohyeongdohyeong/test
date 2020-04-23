package kr.or.visitkorea.admin.server.application.modules.fcm;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_FCM_MESSAGES")
public class SelectFcmMessages extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        HashMap<String, Object> parameter = new HashMap<>();
        if (this.parameterObject.has("keyword"))
            parameter.put("keyword", parameterObject.getString("keyword"));
        if (this.parameterObject.has("osType"))
            parameter.put("osType", parameterObject.getString("osType"));
        if (this.parameterObject.has("offset"))
            parameter.put("offset", parameterObject.getInt("offset"));
        if (this.parameterObject.has("limit"))
            parameter.put("limit", parameterObject.getInt("limit"));

        List<HashMap<String, Object>> resultMap =
        		sqlSession.selectList("kr.or.visitkorea.system.FcmMessageMapper.select", parameter);
        int totalCount = sqlSession.selectOne("kr.or.visitkorea.system.FcmMessageMapper.totalCount");
        
        JSONArray resultArr = new JSONArray(resultMap);
        
        if (resultArr.toString().equals("null")) {
        	resultHeaderObject.put("process", "fail");
        	resultHeaderObject.put("ment", "");
        } else {
            resultBodyObject.put("result", resultArr);
            resultBodyObject.put("resultCnt", totalCount);
        }
    }

    @Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    	
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        System.out.println(">>> resultHeaderObject : " + resultHeaderObject.toString());
        System.out.println(">>> resultBodyObject : " + resultBodyObject.toString());
    }
}
