package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.constants.Color;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;

public class ContentTreeItem extends MaterialTreeItem {

	protected int slidingValue = 0;
	
	private String editorValue = "";

	private SafeHtml safaHtml;
	
	private DatabaseContentType contentType;
	
	private String[] paramters;

	private List<String> diagResult = new ArrayList<String>();

	private String uniqueId;

	private String title;

	private List<ItemInformation> editorObject = new ArrayList<ItemInformation>();
	
	private Map<String, Object> internalReferences = new HashMap<String, Object>();
	
	public ContentTreeItem(int slidingValue) {
		super();
		this.slidingValue = slidingValue;
	}

	@Override
    protected void onLoad() {
        super.onLoad();
	}
	
	public Map<String, Object> getInternalReferences() {
		return internalReferences;
	}

	public void setInternalReferences(Map<String, Object> internalReferences) {
		this.internalReferences = internalReferences;
	}

	@Override
    public void select() {
		
        SelectionEvent.fire(getTree(), this);
        List<MaterialTreeItem> treeItems = getTreeItems();
        if (!treeItems.isEmpty()) {
            if (this.isHide()) {
                CloseEvent.fire(getTree(), this);
                this.setHide(false);
            } else {
                OpenEvent.fire(getTree(), this);
                this.setHide(true);
            }
        }
		
    }

	public int getSlidingValue() {
		return this.slidingValue;
	}

	
	@Override
	public void setText(String text) {
		super.setText(text);
		this.title = text;
	}

	@Override
	public String getText() {
		return this.title;
	}

	public String getEditorValue() {
		return this.editorValue;
	}
	
	public List<ItemInformation> getEditorObject() {
		return this.editorObject;
	}
	
	public void updateCount() {
		super.setText(this.title + " ( 총 " + this.editorObject.size() + " 개의 정보가 할당됨 )");
	}
	
	public void setEditorObject(List<ItemInformation> informations) {
		this.editorObject = informations;
		if (editorObject != null && !editorObject.isEmpty()) {
			this.setTextColor(Color.BLUE);
		}else {
			this.setTextColor(Color.BLACK);
		}
		
		super.setText(this.title + " ( 총 " + informations.size() + " 개의 정보가 할당됨 )");
	}
	
	public void setEditorTable(String editorValue) {
		this.editorValue = editorValue;
		super.setText(this.title + " ( 총 " + editorValue + " 개의 정보가 할당됨 )");
	}
	

	public void setEditorValue(String editorValue) {
		this.editorValue = editorValue;

		SafeHtmlBuilder b = new SafeHtmlBuilder();
		b.appendHtmlConstant(editorValue);
		
		setEditorSafeValue(b.toSafeHtml());
	
	}

	public void setEditorSafeValue(SafeHtml safeHtml) {
		this.safaHtml = safeHtml;
		if (safaHtml.asString().length() > 0) {
			this.setTextColor(Color.BLUE);
		}else {
			this.setTextColor(Color.BLACK);
		}
		super.setText(this.title + " ( 총 " + safaHtml.asString().length() + " 개의 정보가 할당됨 )");
	}

	public SafeHtml getEditorSafeValue() {
		return this.safaHtml;
	}

	public DatabaseContentType getContentType() {
		return contentType;
	}

	public void setContentType(DatabaseContentType contentType) {
		this.contentType = contentType;
	}

	public String[] getParamters() {
		return paramters;
	}

	public void setParamters(String[] paramters) {
		this.paramters = paramters;
	}

	public Map<String, Object> dialogParameters() {
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("DIV", "2s");
		retMap.put("VALUES", this.paramters);
		retMap.put("RESULT", this.diagResult);
		retMap.put("TARGET", this);
		
		return retMap;
	}

	public void setDialogResult(List<String> resultList) {
		this.diagResult = resultList;
		this.setTextColor(Color.BLUE);
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getUniqueId() {
		return this.uniqueId;
	}

	public void updateOrder(double cOrder, double aOrder) {
		
		DatabaseContentType dct = this.getContentType();
		if (dct.equals(DatabaseContentType.INPUT_IMAGE)) {
			
			
			List<ItemInformation> informations = this.getEditorObject();
			
			int articleSubOrder = 0;
			for(ItemInformation ii : informations) {
				
				getInternalReferences().put("ARTICLE_SUB_ORDER", articleSubOrder);

				ImageInformation iif = (ImageInformation)ii;
				String imgId = iif.getImgId();
				updateImageInvoke(imgId, cOrder, aOrder, articleSubOrder);
				articleSubOrder++;
			}
			
		}else {
			
			String aciId = (String)getInternalReferences().get("ACI_ID");
			updateInvoke(aciId, cOrder, aOrder);
			
		}
	}
	
	private void saveDetailItem() {
		Object contentDetailItemObject = getInternalReferences().get("DETAIL_ITEM");
		if (contentDetailItemObject != null) {
			ContentDetailItem contentDetailItem = (ContentDetailItem)contentDetailItemObject;
			contentDetailItem.saveArticleContentData();
		}
	}

	private void updateImageInvoke(String aciId, double ... cOrder) {
		
		
		if (aciId != null) {
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_ORDER_FOR_ARTICLE_WITH_IMAGE")); 
			parameterJSON.put("imgId", new JSONString(aciId));
			parameterJSON.put("cOrder", new JSONNumber(cOrder[0]));
			parameterJSON.put("aOrder", new JSONNumber(cOrder[1]));
			parameterJSON.put("asOrder", new JSONNumber(cOrder[2]));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			});
		
		}
	}
	
	private void updateInvoke(String aciId, double ... cOrder) {
		
		if (aciId != null) {
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_ORDER_FOR_ARTICLE")); 
			parameterJSON.put("aciId", new JSONString(aciId));
			parameterJSON.put("cOrder", new JSONNumber(cOrder[0]));
			parameterJSON.put("aOrder", new JSONNumber(cOrder[1]));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			});
		
		}
	}
	
}
