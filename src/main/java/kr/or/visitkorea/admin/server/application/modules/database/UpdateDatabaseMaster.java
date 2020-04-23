package kr.or.visitkorea.admin.server.application.modules.database;

import java.util.HashMap;

import org.apache.tapestry.components.Insert;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_DATABASE_MASTER")
public class UpdateDatabaseMaster extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		if(this.parameterObject.has("mode")) {
			if(this.parameterObject.get("mode").equals("Base")) {
				
				int DatabaseUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateDatabaseMaster", BaseParam());
				int ContentMasterUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateContentMaster", BaseParam());
				
				if (this.parameterObject.has("startdate") && this.parameterObject.has("enddate")) {
					
					int FestivalDateUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateFestivalDate", BaseParam());
					resultBodyObject.put("resultF", FestivalDateUpdate);
				}
				
				if(DatabaseUpdate > 0 && ContentMasterUpdate > 0) {
					resultHeaderObject.put("process", "success");
					resultBodyObject.put("resultD", DatabaseUpdate);
					resultBodyObject.put("resultC", ContentMasterUpdate);
					
				}
			} else if(this.parameterObject.get("mode").equals("Etc")) {
				
				
				if (this.parameterObject.has("isOfferView")) {
					
				
				
					int DatabaseOffcnt = sqlSession.selectOne("kr.or.visitkorea.system.DatabaseMapper.DatabaseOfferCnt",EtcParam());
					
					if(DatabaseOffcnt == 0) {
						int DatabaseOfferInsert = sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.insertDatabaseOffer", EtcParam());
						resultBodyObject.put("resultoffer", DatabaseOfferInsert);
					} else {
						int DatabaseOfferUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateDatabaseOfferDB", EtcParam());
						resultBodyObject.put("resultoffer", DatabaseOfferUpdate);
					}
				}
				int DatabaseUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateDatabaseMaster", EtcParam());
				int ContentMasterUpdate = sqlSession.update("kr.or.visitkorea.system.DatabaseMapper.UpdateContentMaster", EtcParam());
				
				if(DatabaseUpdate > 0 && ContentMasterUpdate > 0) {
					resultHeaderObject.put("process", "success");
					resultBodyObject.put("resultD", DatabaseUpdate);
					resultBodyObject.put("resultC", ContentMasterUpdate);
				}
			} else if(this.parameterObject.get("mode").equals("Tag")) {
				HashMap<String, Object> params = new HashMap<>();
				if (this.parameterObject.has("cotId"))
					params.put("cotId", this.parameterObject.getString("cotId"));
				
				
				
				if(this.parameterObject.has("AddList")) {
					JSONArray array = this.parameterObject.getJSONArray("AddList");
					for (int i = 0; i < array.length(); i++) {
						
						params.put("TAG_ID", array.getJSONObject(i).get("TAG_ID"));
						params.put("tagId", array.getJSONObject(i).get("TAG_ID"));
						params.put("TAG_NAME", array.getJSONObject(i).get("TAG_NAME"));
						if(array.getJSONObject(i).has("USR_ID")) {
							params.put("USR_ID", array.getJSONObject(i).get("USR_ID"));
							sqlSession.insert("kr.or.visitkorea.system.TagsMapper.insertTag", params);
						}
						sqlSession.insert("kr.or.visitkorea.system.TagsMapper.insertMemberTag", params);
					}
				}
				if(this.parameterObject.has("DeleteList")) {
					JSONArray array = this.parameterObject.getJSONArray("DeleteList");
					for (int i = 0; i < array.length(); i++) {
						params.put("tagId", array.getString(i));
						sqlSession.delete("kr.or.visitkorea.system.TagsMapper.removeMemberTag", params);
					}
				}
				
				if (this.parameterObject.has("MasterTag")) {
					params.put("MasterTag", this.parameterObject.getString("MasterTag"));
					sqlSession.update("kr.or.visitkorea.system.TagsMapper.updateContentTagAllCancel", params);
					sqlSession.update("kr.or.visitkorea.system.TagsMapper.updateContentTag", params);
				}
				
			} else if(this.parameterObject.get("mode").equals("Link")) {
				if(this.parameterObject.has("Links")) {
					JSONArray Links = this.parameterObject.getJSONArray("Links");
					for (int i = 0; i < Links.length(); i++) {
						
						JSONObject link = Links.getJSONObject(i);
						if (link.has("ImageCheck")) {
							
							if(link.getInt("ImageCheck") == 2) {
								sqlSession.insert("kr.or.visitkorea.system.ImageMapper.insertNotCotId", LinkParam(link));
							} else if(link.getInt("ImageCheck") == 3) {
								String BeforeImg =  sqlSession.selectOne("kr.or.visitkorea.system.DatabaseMapper.SelectAdditionalImgId",LinkParam(link));
								link.put("beforeImg", BeforeImg);
								sqlSession.update("kr.or.visitkorea.system.ImageMapper.updateImagePathAndImgId", LinkParam(link));
								link.remove("beforeImg");
							}
						
						}
						sqlSession.insert("kr.or.visitkorea.system.DatabaseMapper.InsertAdditionalInfo", LinkParam(link));
					}
				}
				
				if(this.parameterObject.has("DeleteLinks")) {
					JSONArray Links = this.parameterObject.getJSONArray("DeleteLinks");
					for (int i = 0; i < Links.length(); i++) {
						HashMap<String, Object> params = new HashMap<>();
						params.put("adiId",Links.getString(i));
						params.put("cotId", this.parameterObject.getString("cotId"));
						sqlSession.delete("kr.or.visitkorea.system.DatabaseMapper.DeleteAdditionalInfo", params);
					}
				}
				
			}
			
			
		} else
			resultHeaderObject.put("process", "fail");
	}

	
	private HashMap<String, Object> BaseParam(){
		HashMap<String, Object> params = new HashMap<>();
		
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("title"))
			params.put("title", this.parameterObject.getString("title"));
		if (this.parameterObject.has("bigcategory"))
			params.put("bigcategory", this.parameterObject.getString("bigcategory"));
		if (this.parameterObject.has("midcategory"))
			params.put("midcategory", this.parameterObject.getString("midcategory"));
		if (this.parameterObject.has("smlcategory"))
			params.put("smlcategory", this.parameterObject.getString("smlcategory"));
		if (this.parameterObject.has("startdate"))
			params.put("startdate", this.parameterObject.getString("startdate"));
		if (this.parameterObject.has("enddate"))
			params.put("enddate", this.parameterObject.getString("enddate"));
		if (this.parameterObject.has("bigarea"))
			params.put("bigarea", this.parameterObject.getInt("bigarea"));
		if (this.parameterObject.has("midarea"))
			params.put("midarea", this.parameterObject.getInt("midarea"));
		if (this.parameterObject.has("status"))
			params.put("status", this.parameterObject.getInt("status"));
		if (this.parameterObject.has("authcode"))
			params.put("authcode", this.parameterObject.getString("authcode"));
		
		return params;
	}
	
	private HashMap<String, Object> EtcParam(){
		HashMap<String, Object> params = new HashMap<>();
		
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (this.parameterObject.has("address"))
			params.put("address", this.parameterObject.getString("address"));
		if (this.parameterObject.has("addressDetail"))
			params.put("addressDetail", this.parameterObject.getString("addressDetail"));
		if (this.parameterObject.has("department"))
			params.put("department", this.parameterObject.getString("department"));
		if (this.parameterObject.has("contactInformation"))
			params.put("contactInformation", this.parameterObject.getString("contactInformation"));
		if (this.parameterObject.has("snsId"))
			params.put("snsId", this.parameterObject.getString("snsId"));
		if (this.parameterObject.has("Directoffer"))
			params.put("Directoffer", this.parameterObject.getString("Directoffer"));
		if (this.parameterObject.has("isOfferView"))
			params.put("isOfferView", this.parameterObject.getInt("isOfferView"));
		if (this.parameterObject.has("isOfferNotSNS"))
			params.put("isOfferNotSNS", this.parameterObject.getInt("isOfferNotSNS"));
		
		return params;
	}
	
	private HashMap<String, Object> LinkParam(JSONObject Link){
		HashMap<String, Object> params = new HashMap<>();
		
		if (this.parameterObject.has("cotId"))
			params.put("cotId", this.parameterObject.getString("cotId"));
		if (Link.has("adiId"))
			params.put("adiId", Link.getString("adiId"));
		if (Link.has("linkType"))
			params.put("linkType", Link.getString("linkType"));
		if (Link.has("displayTitle"))
			params.put("displayTitle", Link.getString("displayTitle"));
		if (Link.has("link"))
			params.put("link", Link.getString("link"));
		if (Link.has("imgId"))
			params.put("imgId", Link.getString("imgId"));
		if (Link.has("backgroundColor"))
			params.put("backgroundColor", Link.getString("backgroundColor"));
		if (Link.has("textColor"))
			params.put("textColor", Link.getString("textColor"));
		if (Link.has("LinkDetailType"))
			params.put("LinkDetailType", Link.getString("LinkDetailType"));
		if (Link.has("contentOrder"))
			params.put("contentOrder", Link.getInt("contentOrder"));
		if (Link.has("chkUse"))
			params.put("chkUse", Link.getInt("chkUse"));
		if (Link.has("ImagePath")) {
			params.put("imgPath", Link.getString("ImagePath"));
			params.put("imgDescription", "");
		}
		if (Link.has("beforeImg")) {
			params.put("imgId", Link.getString("beforeImg"));
			params.put("NewimgId", Link.getString("imgId"));
		}
		return params;
	}
	
	
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
