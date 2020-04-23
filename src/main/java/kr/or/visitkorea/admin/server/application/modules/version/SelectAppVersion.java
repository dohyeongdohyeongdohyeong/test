package kr.or.visitkorea.admin.server.application.modules.version;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="SELECT_APP_VERSION")
public class SelectAppVersion extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        String apId;
        try {
            apId = parameterObject.getString("apId");
            if ("".equals(apId)) apId = null;
        } catch (JSONException e) {
            apId = null;
        }

        try {
            List<HashMap<String, Object>> list = sqlSession.selectList("kr.or.visitkorea.system.AppVersionMapper.select", apId);
            JSONArray jsonArray = new JSONArray(list);
            String convertJSONString = jsonArray.toString();
            if (convertJSONString.equals("null")) {
                resultHeaderObject.put("process", "fail");
                resultHeaderObject.put("ment", "");
            } else {
                if (apId != null) {
                    if (list.size() == 0) {
                        resultBodyObject.put("result", new JSONObject("{\"error\":\"not found\"}"));
                    } else if (list.size() == 1) {
                        resultBodyObject.put("result", new JSONObject(jsonArray.getJSONObject(0).toString().toLowerCase()));
                    }
                } else {
                    resultBodyObject.put("result", jsonArray);
                }
            }
        } catch (Exception e) {
            resultHeaderObject.put("process", "fail");
            resultHeaderObject.put("ment", e.getMessage());
            e.printStackTrace();
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
