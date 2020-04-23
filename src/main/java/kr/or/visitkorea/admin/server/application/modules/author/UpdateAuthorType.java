package kr.or.visitkorea.admin.server.application.modules.author;

import java.util.HashMap;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="UPDATE_AUTHOR_TYPE")
public class UpdateAuthorType extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		String mode = null;
		HashMap<String, Object> params = new HashMap<String, Object>();
		if (this.parameterObject.has("mode"))
			mode = this.parameterObject.getString("mode");
		if (this.parameterObject.has("typeId"))
			params.put("typeId", this.parameterObject.getString("typeId"));
		if (this.parameterObject.has("typeName"))
			params.put("typeName", this.parameterObject.getString("typeName"));
		if (this.parameterObject.has("keyword"))
			params.put("keyword", this.parameterObject.getString("keyword"));
		
		if (mode.equals("ADD"))
			sqlSession.insert("kr.or.visitkorea.system.AuthorMapper.insertAuthorType", params);
		else if (mode.equals("MODIFY"))
			sqlSession.insert("kr.or.visitkorea.system.AuthorMapper.updateAuthorType", params);
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
	}

}
