package kr.or.visitkorea.admin.server.application.modules.version;

import org.json.JSONException;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="DELETE_APP_VERSION")
public class DeleteAppVersion extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        String apId;
        try {
            apId = parameterObject.getString("apId");
        } catch (JSONException e) {
            failure(resultHeaderObject, "apId is null.");
            return;
        }

        int delete = sqlSession.delete("kr.or.visitkorea.system.AppVersionMapper.delete", apId);
        if (delete == 1) {
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
