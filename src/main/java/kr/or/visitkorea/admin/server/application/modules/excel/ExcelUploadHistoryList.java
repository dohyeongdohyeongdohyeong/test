package kr.or.visitkorea.admin.server.application.modules.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="EXCEL_UPLOAD_HIST_LIST")
public class ExcelUploadHistoryList extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        final int offset = parameterObject.getInt("offset");

        String startDate;
        try {
            startDate = parameterObject.getString("startDate");
        } catch (Exception e) {
            startDate = null;
        }

        if (startDate != null && startDate.isEmpty()) startDate = null;

        String endDate;
        try {
            endDate = parameterObject.getString("endDate");
        } catch (Exception e) {
            endDate = null;
        }

        if (endDate != null && endDate.isEmpty()) endDate = null;

        HashMap<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("offset", offset);
        parameterMap.put("startDate", startDate);
        parameterMap.put("endDate", endDate);

        List<Map<String, String>> list = sqlSession.selectList(
                "kr.or.visitkorea.system.ExcelUploadMapper.selectHistory",
                parameterMap);
        if (list == null) {
            resultHeaderObject.put("process", "fail");
            resultHeaderObject.put("ment", "");
        } else {
            resultBodyObject.put("result", new JSONArray(list));
        }
    }

    @Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        System.out.println("request object :: " + parameterObject);
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
        System.out.println("result object :: " + resultBodyObject);
    }
}
