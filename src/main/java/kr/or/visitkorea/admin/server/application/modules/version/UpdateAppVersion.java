package kr.or.visitkorea.admin.server.application.modules.version;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_APP_VERSION")
public class UpdateAppVersion extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        HashMap<String, String> version = new HashMap<>();
        try {
            version.put("apId", parameterObject.getString("apId"));
            version.put("ver", parameterObject.getString("ver"));
            version.put("min_ver", parameterObject.getString("min_ver"));
            version.put("url", parameterObject.getString("url"));
        } catch (JSONException e) {
        }
        int update = sqlSession.update("kr.or.visitkorea.system.AppVersionMapper.update", version);
        if (update == 1) {
            resultBodyObject.put("result", true);
        } else {
            failure(resultHeaderObject, "Occurred an error in the middle of insert operation.");
        }
    }

    private void failure(JSONObject result, String message) {
        result.put("process", "fail");
        result.put("ment", message);
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
