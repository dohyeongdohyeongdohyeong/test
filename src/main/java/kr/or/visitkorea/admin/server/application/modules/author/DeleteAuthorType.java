package kr.or.visitkorea.admin.server.application.modules.author;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="DELETE_AUTHOR_TYPE")
public class DeleteAuthorType extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("typeId"))
			params.put("typeId", this.parameterObject.getString("typeId"));
		
		sqlSession.delete("kr.or.visitkorea.system.AuthorMapper.deleteAuthorType", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
