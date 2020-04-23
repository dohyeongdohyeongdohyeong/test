package kr.or.visitkorea.admin.server.application.modules.article;

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
@BusinessMapping(id="INSERT_ARTICLE_CONTENT_MULTI_ROW")
public class InsertArticleContentMultiRow extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("atmId", parameterObject.getString("atmId"));
		JSONArray jsonObject = parameterObject.getJSONArray("ROWS");
		sqlSession.delete("kr.or.visitkorea.system.ArticleMapper.deleteArticleContentWithAtmId", paramterMap);
		
		try {
			for (int i=0; i<jsonObject.length(); i++) {
				
				if (jsonObject.get(i) instanceof JSONObject) {
					
					HashMap<String, Object> buildResultMap = getParameterMap((JSONObject) jsonObject.get(i));
					sqlSession.insert( "kr.or.visitkorea.system.ArticleMapper.articleContentRowInsert" ,  buildResultMap );
					
					Object travelInfo = buildResultMap.get("TRAVEL_INFO");
					insertTravelInfo(travelInfo, buildResultMap);
					
					Object couponInfo = buildResultMap.get("COUPON_INFO");
					insertCouponInfo(couponInfo, buildResultMap,true);
					
					
	
				} else if (jsonObject.get(i) instanceof JSONArray){
					
					JSONArray subObjectArray = jsonObject.getJSONArray(i);
					
					for (int r=0; r<subObjectArray.length(); r++) {
						
						JSONObject subObject = subObjectArray.getJSONObject(r);
						HashMap<String, Object> buildResultMap = getParameterMap(subObject);
						sqlSession.insert( "kr.or.visitkorea.system.ArticleMapper.articleContentRowInsert" ,  buildResultMap );
						
						Object travelInfo = buildResultMap.get("TRAVEL_INFO");
						insertTravelInfo(travelInfo, buildResultMap);
						
						Object couponInfo = buildResultMap.get("COUPON_INFO");
						insertCouponInfo(couponInfo, buildResultMap,true);
						
						
					}
				}
			}
			
			JSONObject resultObj = new JSONObject();
			resultObj.put("procResult", "result");
			
			String convertJSONString = resultObj.toString();
			
			if (convertJSONString.equals("null")) {
				resultHeaderObject.put("process", "fail");
				resultHeaderObject.put("ment", "");
			}else{
				resultBodyObject.put("result", resultObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			sqlSession.rollback();
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "");
		}
		
		
		
	}

	private void insertTravelInfo(Object travelInfo, HashMap<String, Object> buildResultMap) {
		if (travelInfo != null) {
			if (travelInfo instanceof JSONObject) {
				JSONObject obj = (JSONObject) buildResultMap.get("TRAVEL_INFO");
				HashMap<String, Object> travelParams = getTravelParameter(obj);
				travelParams.put("CP_IDX", 0);
				
				//	STATUS :: -1. 삭제한 항목, 0. 기존에 존재했던 항목, 1. 신규로 생성된 항목
				if ((int) travelParams.get("STATUS") == -1)
					sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.deleteArticleTravelInfo", travelParams);
				else if ((int) travelParams.get("STATUS") == 1)
					sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleTravelInfo", travelParams);
				else
					sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateArticleTravelInfo", travelParams);
				
			} else if (travelInfo instanceof JSONArray) {
				JSONArray travelArr = (JSONArray) buildResultMap.get("TRAVEL_INFO");
				for (int j = 0; j < travelArr.length(); j++) {
					JSONObject obj = travelArr.getJSONObject(j);
					HashMap<String, Object> travelParams = getTravelParameter(obj);
					travelParams.put("IDX", j);
					
					if ((int) travelParams.get("STATUS") == -1)
						sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.deleteArticleTravelInfo", travelParams);
					else if ((int) travelParams.get("STATUS") == 1)
						sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleTravelInfo", travelParams);
					else
						sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateArticleTravelInfo", travelParams);
				}
			}
		}
	}
	
	private void insertCouponInfo(Object Couponinfo, HashMap<String, Object> buildResultMap,boolean type) {
		if (Couponinfo != null) {
			if (Couponinfo instanceof JSONObject) {
				JSONObject obj = null;
				if(type) obj = (JSONObject) buildResultMap.get("COUPON_INFO");
				else obj = (JSONObject) buildResultMap.get("COUPON_TRAVEL_INFO");
				HashMap<String, Object> CouponParams = getCouponParameter(obj);
				CouponParams.put("CP_IDX", 0);
				
				//	STATUS :: -1. 삭제한 항목, 0. 기존에 존재했던 항목, 1. 신규로 생성된 항목
				if ((int) CouponParams.get("STATUS") == -1)
					sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.deleteArticleCouponInfo", CouponParams);
				else if ((int) CouponParams.get("STATUS") == 1) {
					sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleCouponInfo", CouponParams);
					sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.importCouponWithACI", CouponParams);
					if(CouponParams.containsKey("imgPath")) {
						sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", CouponParams);
					}
				}
				else {
					HashMap<String, Object> resultMap = 
							sqlSession.selectOne("kr.or.visitkorea.system.ArticleMapper.selectArticleCouponImage", CouponParams);
					
					sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateArticleCouponInfo", CouponParams);
					String img_ID = resultMap.get("CP_IMG_ID").toString();
					
					if(img_ID == null || img_ID.equals("null")) {
						 if(CouponParams.containsKey("imgPath"))
							 sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", CouponParams);
					} else {
						if(CouponParams.containsKey("imgPath")) {
							CouponParams.put("imgId", img_ID);
							 sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathAndImgId", CouponParams);
						} else {
							 CouponParams.put("imgId", img_ID);
							 sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", CouponParams);
						 }
					}
				}
				
			} else if (Couponinfo instanceof JSONArray) {
				JSONArray travelArr = null;
				if(type) travelArr = (JSONArray) buildResultMap.get("COUPON_INFO");
				else travelArr = (JSONArray) buildResultMap.get("COUPON_TRAVEL_INFO");
				
				for (int j = 0; j < travelArr.length(); j++) {
					JSONObject obj = travelArr.getJSONObject(j);
					HashMap<String, Object> CouponParams = getCouponParameter(obj);
					CouponParams.put("CP_IDX", j);
					
					if ((int) CouponParams.get("STATUS") == -1)
						sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.deleteArticleCouponInfo", CouponParams);
					else if ((int) CouponParams.get("STATUS") == 1) {
						sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.insertArticleCouponInfo", CouponParams);
						sqlSession.insert("kr.or.visitkorea.system.ArticleMapper.importCouponWithACI", CouponParams);
						if(CouponParams.containsKey("imgPath")) {
							sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", CouponParams);
						} 
					}
					else {
						
						HashMap<String, Object> resultMap = 
								sqlSession.selectOne("kr.or.visitkorea.system.ArticleMapper.selectArticleCouponImage", CouponParams);
						
						sqlSession.update("kr.or.visitkorea.system.ArticleMapper.updateArticleCouponInfo", CouponParams);

						JSONObject resultObj = new JSONObject(resultMap);
						String img_ID = "";
						if(resultObj.has("CP_IMG_ID"))
						img_ID = resultObj.get("CP_IMG_ID").toString();
						
						if(img_ID == null || img_ID.equals("null") || img_ID.equals("")) {
							 if(CouponParams.containsKey("imgPath")) {
								 
								 sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", CouponParams);
							 }
						} else {
							if(CouponParams.containsKey("imgPath")) {
								CouponParams.put("imgId", img_ID);
								 sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathAndImgId", CouponParams);
							} else {
								 CouponParams.put("imgId", img_ID);
								 System.out.println(CouponParams.get("imgId"));
								 System.out.println(CouponParams.get("imgPath"));
								 sqlSession.delete("kr.or.visitkorea.system.ImageMapper.deleteWithImgId", CouponParams);
							 }
						}
					}
				}
			}
		}
	}
	
	private HashMap<String, Object> getParameterMap(JSONObject jsonObject) {
		HashMap<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("ACI_ID", jsonObject.getString("ACI_ID"));
		paramterMap.put("ATM_ID", jsonObject.getString("ATM_ID"));
		paramterMap.put("CONTENT_ORDER", jsonObject.getInt("CONTENT_ORDER"));
		paramterMap.put("ARTICLE_ORDER", jsonObject.getInt("ARTICLE_ORDER"));
		paramterMap.put("ARTICLE_SUB_ORDER", jsonObject.getInt("ARTICLE_SUB_ORDER"));
		paramterMap.put("ARTICLE_TYPE", jsonObject.getInt("ARTICLE_TYPE"));
		
		if (jsonObject.has("IS_VERTICAL"))
			paramterMap.put("IS_VERTICAL", jsonObject.getInt("IS_VERTICAL"));
		if (jsonObject.has("IS_BOX"))
			paramterMap.put("IS_BOX", jsonObject.getInt("IS_BOX"));
		if (jsonObject.has("ARTICLE_TITLE"))
			paramterMap.put("ARTICLE_TITLE", jsonObject.getString("ARTICLE_TITLE"));
		if (jsonObject.has("ARTICLE_BODY"))
			paramterMap.put("ARTICLE_BODY", jsonObject.getString("ARTICLE_BODY"));
		if (jsonObject.has("TRAVEL_INFO")) {
			if (jsonObject.get("TRAVEL_INFO") instanceof JSONArray)
				paramterMap.put("TRAVEL_INFO", jsonObject.getJSONArray("TRAVEL_INFO"));
			else
				paramterMap.put("TRAVEL_INFO", jsonObject.getJSONObject("TRAVEL_INFO"));
		}
		if (jsonObject.has("COUPON_INFO")) {
			if (jsonObject.get("COUPON_INFO") instanceof JSONArray)
				paramterMap.put("COUPON_INFO", jsonObject.getJSONArray("COUPON_INFO"));
			else
				paramterMap.put("COUPON_INFO", jsonObject.getJSONObject("COUPON_INFO"));
		}
		if (jsonObject.has("COUPON_TRAVEL_INFO")) {
			if (jsonObject.get("COUPON_TRAVEL_INFO") instanceof JSONArray)
				paramterMap.put("COUPON_TRAVEL_INFO", jsonObject.getJSONArray("COUPON_TRAVEL_INFO"));
			else
				paramterMap.put("COUPON_TRAVEL_INFO", jsonObject.getJSONObject("COUPON_TRAVEL_INFO"));
		}
		
		if (jsonObject.has("IMG_ID")) paramterMap.put("IMG_ID", jsonObject.getString("IMG_ID"));
		if (jsonObject.has("IS_CAPTION")) paramterMap.put("IS_CAPTION", jsonObject.getNumber("IS_CAPTION"));
		if (jsonObject.has("IMAGE_CAPTION")) paramterMap.put("IMAGE_CAPTION", jsonObject.getString("IMAGE_CAPTION"));
		if (jsonObject.has("IMAGE_DESCRIPTION")) paramterMap.put("imgDescription", jsonObject.getString("IMAGE_DESCRIPTION"));
		
		return paramterMap;
	}

	private HashMap<String, Object> getTravelParameter(JSONObject jsonObject) {
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		if (jsonObject.has("TRI_ID"))
			parameterMap.put("TRI_ID", jsonObject.getString("TRI_ID"));
		if (jsonObject.has("ACI_ID"))
			parameterMap.put("ACI_ID", jsonObject.getString("ACI_ID"));
		if (jsonObject.has("COT_ID"))
			parameterMap.put("COT_ID", jsonObject.getString("COT_ID"));
		if (jsonObject.has("TITLE"))
			parameterMap.put("TITLE", jsonObject.getString("TITLE"));
		if (jsonObject.has("ADDRESS"))
			parameterMap.put("ADDRESS", jsonObject.getString("ADDRESS"));
		if (jsonObject.has("HOMEPAGE"))
			parameterMap.put("HOMEPAGE", jsonObject.getString("HOMEPAGE"));
		if (jsonObject.has("TEL"))
			parameterMap.put("TEL", jsonObject.getString("TEL"));
		if (jsonObject.has("STATUS"))
			parameterMap.put("STATUS", jsonObject.getInt("STATUS"));
		if (jsonObject.has("ETC"))
			parameterMap.put("ETC", jsonObject.getString("ETC"));
		return parameterMap;
	}
	
	private HashMap<String, Object> getCouponParameter(JSONObject jsonObject) {
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		if (jsonObject.has("CP_ID"))
			parameterMap.put("CP_ID", jsonObject.getString("CP_ID"));
		if (jsonObject.has("ACI_ID"))
			parameterMap.put("ACI_ID", jsonObject.getString("ACI_ID"));
		if (jsonObject.has("CP_GB"))
			parameterMap.put("CP_GB", jsonObject.getString("CP_GB"));
		if (jsonObject.has("CP_USE_FL"))
			parameterMap.put("CP_USE_FL", jsonObject.getString("CP_USE_FL"));
		if (jsonObject.has("CP_TITLE"))
			parameterMap.put("CP_TITLE", jsonObject.getString("CP_TITLE"));
		if (jsonObject.has("CP_DESC"))
			parameterMap.put("CP_DESC", jsonObject.getString("CP_DESC"));
		if (jsonObject.has("AREA_CODE"))
			parameterMap.put("AREA_CODE", jsonObject.getInt("AREA_CODE"));
		if (jsonObject.has("SIGUGUN_CODE"))
			parameterMap.put("SIGUGUN_CODE", jsonObject.getInt("SIGUGUN_CODE"));
		if (jsonObject.has("CP_START_DATE"))
			parameterMap.put("CP_START_DATE", jsonObject.getString("CP_START_DATE"));
		else
			parameterMap.put("CP_START_DATE", null);
		if (jsonObject.has("CP_END_DATE"))
			parameterMap.put("CP_END_DATE", jsonObject.getString("CP_END_DATE"));
		else
			parameterMap.put("CP_END_DATE", null);
		if (jsonObject.has("CP_RSV_START_DATE"))
			parameterMap.put("CP_RSV_START_DATE", jsonObject.getString("CP_RSV_START_DATE"));
		else
			parameterMap.put("CP_RSV_START_DATE", null);
		if (jsonObject.has("CP_RSV_END_DATE"))
			parameterMap.put("CP_RSV_END_DATE", jsonObject.getString("CP_RSV_END_DATE"));
		else
			parameterMap.put("CP_RSV_END_DATE", null);
			
		if (jsonObject.has("CP_COT_CATEGORY"))
			parameterMap.put("CP_COT_CATEGORY", jsonObject.getInt("CP_COT_CATEGORY"));
		if (jsonObject.has("CP_IMG_ID")) {
			parameterMap.put("imgId", jsonObject.getString("CP_IMG_ID"));
			parameterMap.put("NewimgId", jsonObject.getString("CP_IMG_ID"));
		} else {
			parameterMap.put("imgId", null);
		}
		if (jsonObject.has("CP_IMG_PATH"))
			parameterMap.put("imgPath", jsonObject.getString("CP_IMG_PATH"));
		if (jsonObject.has("CP_CONTENT"))
			parameterMap.put("CP_CONTENT", jsonObject.getString("CP_CONTENT"));
		if (jsonObject.has("CP_DEL_FL")) {
			parameterMap.put("CP_DEL_FL", jsonObject.getString("CP_DEL_FL"));
		} else{
			parameterMap.put("CP_DEL_FL", "N");
		}
		if (jsonObject.has("CP_IDX"))
			parameterMap.put("CP_IDX", jsonObject.getInt("CP_IDX"));
		if (jsonObject.has("STATUS"))
			parameterMap.put("STATUS", jsonObject.getInt("STATUS"));
		parameterMap.put("imgDescription", "");
		return parameterMap;
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
