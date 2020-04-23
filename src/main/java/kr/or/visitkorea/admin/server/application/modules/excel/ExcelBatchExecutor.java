package kr.or.visitkorea.admin.server.application.modules.excel;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="EXEC_EXCEL_BATCH")
public class ExcelBatchExecutor extends AbstractModule {

    @Override
    public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    }

    @Override
    protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    }

    @Override
    protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
    }
}
