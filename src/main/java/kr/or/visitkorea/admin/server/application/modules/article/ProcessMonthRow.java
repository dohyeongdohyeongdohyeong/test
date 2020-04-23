package kr.or.visitkorea.admin.server.application.modules.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="PROCESS_MONTH_ROW")
public class ProcessMonthRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("tbl", parameterObject.getString("tbl"));
		parameterMap.put("colTitle", parameterObject.getString("colTitle"));
		parameterMap.put("cotId", parameterObject.getString("cotId"));
		
		System.out.println("parameterObject.getString(\"value\") :: " + parameterObject.getString("value"));
		
		String[] valueSpiltArray = parameterObject.getString("value").split("\\|");
		
		// delete month content
		String atmId = sqlSession.selectOne("kr.or.visitkorea.system.ArticleMapper.selcetAtmIdFromCotId", parameterMap);
		int deleteInt = sqlSession.delete("kr.or.visitkorea.system.ArticleMapper.deleteWithCotId", parameterMap);

		List<Map<String, String>> list =  new ArrayList<Map<String, String>>();
		
		for (int i=0; i<valueSpiltArray.length; i++) {
			String monthStr = valueSpiltArray[i];
			if (monthStr.length() == 1) monthStr = "0"+monthStr;
			Map<String, String> member00 = new HashMap<String, String>();
			member00.put("atmId", atmId);
			member00.put("month", monthStr);
			list.add(member00);
			System.out.println("kr.or.visitkorea.system.ArticleMapper.insertWithCotId.memberMap  :: " + atmId + " , " + valueSpiltArray[i]);
		}
		
		parameterMap.put("list", list);
		
		int insertInt = sqlSession.delete("kr.or.visitkorea.system.ArticleMapper.insertWithCotId", parameterMap);
		
		JSONObject resultObj = new JSONObject();
		resultObj.put("insertStat", insertInt);
		
		String convertJSONString = resultObj.toString();

		if (convertJSONString.equals("null")) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}else{
			resultBodyObject.put("result", resultObj);
		}
		
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
