package kr.or.visitkorea.admin.server.application.modules.files;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */ 
@BusinessMapping(id="SELECT_XLS_COUNT")
public class SelectXlsCount extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		
		String SelectType = parameterObject.get("select_type").toString();
		
		int Cnt = 0;
		if(SelectType.equals("DatabaseList")) {
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();

			if(parameterObject.has("mode"))
				paramterMap.put("mode", parameterObject.get("mode").toString());
			if(parameterObject.has("keyword"))
				paramterMap.put("keyword", parameterObject.get("keyword").toString());
			if(parameterObject.has("contentPart"))
				paramterMap.put("contentPart", parameterObject.get("contentPart").toString());
			if(parameterObject.has("StatusSearch"))
				paramterMap.put("StatusSearch", parameterObject.get("StatusSearch").toString());
			if(parameterObject.has("startInput"))
				paramterMap.put("startInput", parameterObject.get("startInput").toString());
			if(parameterObject.has("endInput"))
				paramterMap.put("endInput", parameterObject.get("endInput").toString());
			if(parameterObject.has("OTD_ID"))
				paramterMap.put("OTD_ID", parameterObject.get("OTD_ID").toString());
			if(parameterObject.has("dateType"))
				paramterMap.put("dateType", parameterObject.get("dateType").toString());
			if(parameterObject.has("areacode"))
				paramterMap.put("areacode", parameterObject.get("areacode").toString());
			if(parameterObject.has("sigunguname"))
				paramterMap.put("sigunguname", parameterObject.get("sigunguname").toString());
			
			
			Cnt = sqlSession.selectOne( 
					"kr.or.visitkorea.system.DatabaseMapper.SelectCountExcelDownload" , 
					paramterMap );
			
		} else if(SelectType.equals("RecommandList")) {
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
	    	paramterMap.put("mode", parameterObject.getInt("mode"));
			if(parameterObject.has("dateType"))
				paramterMap.put("dateType", parameterObject.getInt("dateType"));
			if(parameterObject.has("startInput"))
				paramterMap.put("startInput", parameterObject.getString("startInput"));
			if(parameterObject.has("endInput"))
				paramterMap.put("endInput", parameterObject.getString("endInput"));
			if(parameterObject.has("areaCode"))
				paramterMap.put("areaCode", parameterObject.getInt("areaCode"));
			if(parameterObject.has("sigunguname"))
				paramterMap.put("sigunguname", parameterObject.getInt("sigunguname"));
			if(parameterObject.has("CategoryCode"))
				paramterMap.put("CategoryCode", parameterObject.getInt("CategoryCode"));
			if(parameterObject.has("keyword"))
				paramterMap.put("keyword", parameterObject.getString("keyword"));
			if(parameterObject.has("OTD_ID"))
				paramterMap.put("OTD_ID", parameterObject.getString("OTD_ID"));
			if(parameterObject.has("StatusSearch"))
				paramterMap.put("StatusSearch", parameterObject.getInt("StatusSearch"));
			if(parameterObject.has("division"))
				paramterMap.put("Division", parameterObject.get("division").toString());
			Cnt = sqlSession.selectOne( 
					"kr.or.visitkorea.system.ArticleMapper.ArticleCountExcelDownload" , 
					paramterMap );
			
		} else if(SelectType.equals("PartnersContent")) {
			
			HashMap<String, Object> paramterMap = new HashMap<String, Object>();
	    	paramterMap.put("mode", parameterObject.getInt("mode"));
			if(parameterObject.has("dateType"))
				paramterMap.put("dateType", parameterObject.getInt("dateType"));
			if(parameterObject.has("startInput"))
				paramterMap.put("startInput", parameterObject.getString("startInput"));
			if(parameterObject.has("endInput"))
				paramterMap.put("endInput", parameterObject.getString("endInput"));
			if(parameterObject.has("CategoryCode"))
				paramterMap.put("CategoryCode", parameterObject.getInt("CategoryCode"));
			if(parameterObject.has("keyword"))
				paramterMap.put("keyword", parameterObject.getString("keyword"));
			if(parameterObject.has("StatusSearch"))
				paramterMap.put("StatusSearch", parameterObject.getInt("StatusSearch"));
			if(parameterObject.has("Is_Use"))
				paramterMap.put("Is_Use", parameterObject.getInt("Is_Use"));
			if(parameterObject.has("Apply_type"))
				paramterMap.put("Apply_type", parameterObject.getInt("Apply_type"));
			
			Cnt = sqlSession.selectOne( 
					"kr.or.visitkorea.system.PartnersMapper.PartnersContentExcelCount" , 
					paramterMap );
			
		}
		

			resultHeaderObject.put("result", Cnt);
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
