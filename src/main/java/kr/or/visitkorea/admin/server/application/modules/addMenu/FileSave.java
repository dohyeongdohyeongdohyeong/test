package kr.or.visitkorea.admin.server.application.modules.addMenu;

import java.io.File;
import java.io.FileOutputStream;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="FILE_SAVE")
public class FileSave extends AbstractModule {
	
	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		if (Call.getProperty("menu.xml.path") == null) {
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "시스템 설정 문제로인해 저장할 수 없습니다.");
			return;
		}
		JSONArray menuList = parameterObject.getJSONArray("modifiedMenu");
		Document menuDoc = Call.getPermissionDocument();

		Document doc = new Document();
		Element root = new Element("menu");
		doc.setRootElement(root);
		
		try (FileOutputStream fos = new FileOutputStream(new File(Call.getProperty("menu.xml.path")))) {
			for (int i = 0; i < menuList.length(); i++) {
				JSONObject menuObj = menuList.getJSONObject(i);
				appendMenu(menuObj, root);
			}
			
			Call.setPermissionDoc(doc);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setIndent("            "));
			outputter.output(doc, fos);
			resultHeaderObject.put("process", "success");
			
		} catch (Exception e) {
			Call.setPermissionDoc(menuDoc);
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", "변경된 내용을 적용하는데 실패하였습니다.");
		}
	}

	//	Menu 추가
	public Element appendMenu(JSONObject obj, Element parent) {
		Element menu = new Element("menu")
				.setAttribute("caption", obj.getString("caption"));
		if (!obj.isNull("app"))
			menu.setAttribute("app", obj.getString("app"));
		if (!obj.isNull("icon"))
			menu.setAttribute("icon", obj.getString("icon"));

		if (!obj.isNull("permission")) {
			Object permissionCheckObj = obj.get("permission");
			if (permissionCheckObj instanceof JSONArray) {
				JSONArray permissionList = (JSONArray) permissionCheckObj;
				for (int j = 0; j < permissionList.length(); j++) {
					JSONObject permisObj = permissionList.getJSONObject(j);
					
					Element permission = appendPermission(permisObj, menu);
				}
			} else if (permissionCheckObj instanceof JSONObject) {
				JSONObject permisnObj = (JSONObject) permissionCheckObj;
				Element permission = appendPermission(permisnObj, menu);
			}
		}

		if (!obj.isNull("bounds")) {
			Element bounds = appendBounds(obj, menu);
		}
		
		if (!obj.isNull("parameters")) {
			JSONObject parametersObj = obj.getJSONObject("parameters");
			Element parameters = new Element("parameters");
			menu.addContent(parameters);
			Object valueCheckObj = parametersObj.get("value");
			if (valueCheckObj instanceof JSONArray) {
				JSONArray valueList = (JSONArray) valueCheckObj;
				for (int j = 0; j < valueList.length(); j++) {
					JSONObject paramObj = valueList.getJSONObject(j);
					
					Element value = appendParameters(paramObj, parameters);
				}
				
			} else if (valueCheckObj instanceof JSONObject) {
				JSONObject valueObj = (JSONObject) valueCheckObj;
				Element value = appendParameters(valueObj, parameters);
			}
		}

		if (!obj.isNull("menu")) {
			Object childMenus = obj.get("menu");
			
			if (childMenus instanceof JSONArray) {
				JSONArray childMenuList = (JSONArray) childMenus;
				
				for (int j = 0; j < childMenuList.length(); j++) {
					JSONObject childMenuObj = childMenuList.getJSONObject(j);
					Element childMenu = appendMenu(childMenuObj, menu);
				}
				
			} else if (childMenus instanceof JSONObject) {
				Element childMenu = appendMenu((JSONObject) childMenus, menu);
			}
		}
		
		parent.addContent(menu);
		
		return menu;
	}
	
	//	Permission 추가
	public Element appendPermission(JSONObject obj, Element parent) {
		Element permission = new Element("permission");
		if (!obj.isNull("id"))
			permission.setAttribute("id", obj.getString("id"));
		permission.setAttribute("caption", obj.getString("caption"))
				  .setAttribute("uuid", obj.getString("uuid"))
				  .setText(Boolean.toString(obj.getBoolean("content")));

		parent.addContent(permission);

		return permission;
	}

	//	Bounds 추가
	public Element appendBounds(JSONObject obj, Element parent) {
		JSONObject boundsObj = (JSONObject) obj.get("bounds");
		Element bounds = new Element("bounds")
				.setAttribute("width", Integer.toString(boundsObj.getInt("width")))
				.setAttribute("height", Integer.toString(boundsObj.getInt("height")));
		parent.addContent(bounds);

		return bounds;
	}
	
	//	Parameter 추가
	public Element appendParameters(JSONObject obj, Element parent) {
		Element value = new Element("value")
				.setAttribute("id", obj.getString("id"))
				.setAttribute("type", obj.getString("type"));
		
		if (obj.getString("type").equals("MAP")) {
			Object mapParam = obj.get("value");
			if (mapParam instanceof JSONArray) {
				JSONArray mapParamList = (JSONArray) mapParam;
				
				for (int k = 0; k < mapParamList.length(); k++) {
					JSONObject mapParamObj = mapParamList.getJSONObject(k);
					
					Element mapValue = new Element("value")
							.setAttribute("id", mapParamObj.getString("id"))
							.setAttribute("type", mapParamObj.getString("type"));
					mapValue.setContent(new CDATA(mapParamObj.getString("content")));
					value.addContent(mapValue);
				}
			} else if (mapParam instanceof JSONObject) {
				JSONObject mapParamList = (JSONObject) mapParam;
				Element mapValue = new Element("value")
						.setAttribute("id", mapParamList.getString("id"))
						.setAttribute("type", mapParamList.getString("type"));
				mapValue.setContent(new CDATA(mapParamList.getString("content")));
				value.addContent(mapValue);
			}
		} else if (obj.getString("type").equals("BOOLEAN")) {
			value.setText(Boolean.toString(obj.getBoolean("content")));
		} else if (obj.getString("type").equals("STRING")) {
			value.setText(obj.getString("content"));
		}
		parent.addContent(value);
		
		return value;
	}
	
	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
	}
}
