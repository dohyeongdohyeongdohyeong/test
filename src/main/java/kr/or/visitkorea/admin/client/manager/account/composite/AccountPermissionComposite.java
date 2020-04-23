package kr.or.visitkorea.admin.client.manager.account.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountApplication;
import kr.or.visitkorea.admin.client.manager.account.AccountListItem;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.ContentLinkButton;

public class AccountPermissionComposite extends AbstractContentComposite {

	private MaterialTree tree;
	private AccountListItem accountListItem;
	private MaterialColumn col1;
	private MaterialWidget col2;
	private MaterialLink parentProp;
	private MaterialPanel panel;
	private MaterialLink account;
	private List<String> permissionList = new ArrayList<String>();
	private MaterialContentTreeItem selectedItem;
	private ContentLinkButton clb1;
	private ContentLinkButton clb2;
	private ContentLinkButton clb3;
	private ContentLinkButton clb4;
	private MaterialPanel permTemplate;
	private MaterialRow row;
	private MaterialColumn col11; 

	public AccountPermissionComposite() {
		super();
	}

	@Override
	public void init() {
		this.setWidth("100%");
		this.setHeight("426px");
		setupContent();
		initLayout();
	}
	
	public void setAccountListItem(AccountListItem item) {
		parentProp.setText("선택 항목");
		this.accountListItem = item;
		setupContent();
		permissionList.clear();
		panel.clear();
	}
	
	private AccountPermissionComposite getComposite() {
		return this;
	}

	private void initLayout() {
		
		if (row == null) {
			
			row = new MaterialRow();
			
			col1 = new MaterialColumn();
			col1.setGrid("s5");
			
			col2 = new MaterialColumn();
			col2.setGrid("s7");
		
			panel = new MaterialPanel();
			panel.setOverflow(Overflow.AUTO);
			panel.setBackgroundColor(Color.WHITE);
			panel.setHeight("266px");
			panel.setPaddingTop(25);
			panel.setPaddingLeft(10);
			panel.setPaddingRight(10);
			panel.setBorder("1px solid rgb(224, 224, 224)");
			
			
			parentProp = new MaterialLink("선택 항목");
			parentProp.setFontSize("0.9em");
			parentProp.setTextAlign(TextAlign.LEFT);
			parentProp.setWidth("100%");
			parentProp.setHeight("30px");
			parentProp.setTextColor(Color.RED);
			parentProp.setFontWeight(FontWeight.BOLD);
			
			MaterialPanel titlePanel = new MaterialPanel();
			titlePanel.setBackgroundColor(Color.WHITE);
			titlePanel.add(parentProp);
			titlePanel.setWidth("100%");
			titlePanel.setPadding(5);
			titlePanel.setBorderLeft("1px solid rgb(224, 224, 224)");
			titlePanel.setBorderRight("1px solid rgb(224, 224, 224)");
			titlePanel.setBorderTop("1px solid rgb(224, 224, 224)");
	
			col2.add(titlePanel);
			col2.add(panel);
	
			row.add(col1);
			row.add(col2);
			
			this.add(row);
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setupContent() {
		
		if (account == null) {
			account = new MaterialLink("사용자명");
			account.setWidth("100%");
			account.setFontSize("1.5em");
			this.add(account);
		}
		
		if (this.accountListItem != null) {

			MaterialLoader.loading(true, getComposite());
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("PERMISSION_SELECT"));
			parameterJSON.put("usrId", new JSONString(this.accountListItem.getAccountId()));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject permissionJSON = (JSONObject)JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONArray permissionArray = permissionJSON.get("body").isObject().get("result").isArray();
					int size = permissionArray.size();
					for (int i=0; i<size; i++) {
						JSONObject  tmpObject = permissionArray.get(i).isObject();
						permissionList.add(tmpObject.get("PERMISSION_ID").isString().stringValue());
					}
					
					MaterialLoader.loading(false, getComposite());
				}
			});
			
			account.setText(this.accountListItem.getName());
		}
		
		if (permTemplate == null) {
			
			permTemplate = new MaterialPanel();
			permTemplate.setMarginTop(20);
			permTemplate.setMarginBottom(20);
			permTemplate.setTextAlign(TextAlign.CENTER);

			clb1 = new ContentLinkButton("전체 선택 (시스템 포함)");
			clb1.setFontSize("0.9em");
			clb1.setMarginRight(10);
			
			clb2 = new ContentLinkButton("전체 선택 (시스템 제외)");
			clb2.setFontSize("0.9em");
			clb2.setMarginRight(10);
			
			clb3 = new ContentLinkButton("전체 해제 (시스템 포함)");
			clb3.setFontSize("0.9em");
			clb3.setMarginRight(10);
			
			clb4 = new ContentLinkButton("전체 해제 (시스템 제외)");
			clb4.setFontSize("0.9em");
			
			permTemplate.add(clb1);
			permTemplate.add(clb3);
			permTemplate.add(clb2);
			permTemplate.add(clb4);

			clb1.addClickHandler(event->{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("TARGET", tree);
				paramMap.put("ACCOUNT_ITEM", accountListItem);
				this.accountListItem.getMaterialExtentsWindow().openDialog(
					AccountApplication.SELECT_ALL_PERMISSION_FOR_ADMIN, 
					paramMap, 500);
			});

			clb2.addClickHandler(event->{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("TARGET", tree);
				paramMap.put("ACCOUNT_ITEM", accountListItem);
				this.accountListItem.getMaterialExtentsWindow().openDialog(
					AccountApplication.SELECT_ALL_PERMISSION_FOR_USER, 
					paramMap, 500);
			});

			clb3.addClickHandler(event->{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("TARGET", tree);
				paramMap.put("ACCOUNT_ITEM", accountListItem);
				this.accountListItem.getMaterialExtentsWindow().openDialog(
					AccountApplication.DESELECT_ALL_PERMISSION_FOR_ADMIN, 
					paramMap, 500);
			});

			clb4.addClickHandler(event->{
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("TARGET", tree);
				paramMap.put("ACCOUNT_ITEM", accountListItem);
				this.accountListItem.getMaterialExtentsWindow().openDialog(
					AccountApplication.DESELECT_ALL_PERMISSION_FOR_USER, 
					paramMap, 500);
			});
			
			this.add(permTemplate);
			
			initLayout();
		}
		
		if (this.accountListItem == null) {
			clb1.setEnabled(false);
			clb2.setEnabled(false);
			clb3.setEnabled(false);
			clb4.setEnabled(false);
		}else {
			clb1.setEnabled(true);
			clb2.setEnabled(true);
			clb3.setEnabled(true);
			clb4.setEnabled(true);
		}
		
		if (tree == null) {
			
			MaterialPanel panel1 = new MaterialPanel();
			col1.add(panel1);
			panel1.setOverflow(Overflow.AUTO);
			panel1.setWidth("100%");
			panel1.setHeight("300px");
			panel1.setBorder("1px solid rgb(224, 224, 224)");
			panel1.setBackgroundColor(Color.WHITE);
		
			tree = new MaterialTree();
			tree.setTextAlign(TextAlign.LEFT);
			tree.setFontSize("1.0em");
			
			buildPermmitionTree();
	
			tree.addSelectionHandler(event->{
				if (this.accountListItem != null) {
					if (selectedItem != null) selectedItem.setTextColor(Color.BLACK);
	
					selectedItem = (MaterialContentTreeItem) event.getSelectedItem();
					selectedItem.setTextColor(Color.RED);
					List<JSONObject> permList = (List<JSONObject>) selectedItem.get("permission");
					parentProp.setText(selectedItem.getText());
					panel.clear();
					for (JSONObject obj : permList) {
						addPermission(obj);
					}
				}
			});
	
			panel1.add(tree);
			col1.add(panel1);
			
		}else {
			
		}
	}

	private void addPermission(JSONObject jobj) {
		
		if (this.accountListItem != null) {
			
			MaterialRow detailRow = new MaterialRow();
			
			col11 = new MaterialColumn();
			col11.setGrid("s12");
	
			String permissionId = jobj.get("uuid").isString().stringValue();
			jobj.put("content", JSONBoolean.getInstance(permissionList.contains(permissionId)));
			
			MaterialCheckBox checkBox = new MaterialCheckBox();
			NodeList<Element> labelElement = checkBox.getElement().getElementsByTagName("label");
			labelElement.getItem(0).getStyle().setColor("#000");
			checkBox.setText(jobj.get("caption").isString().stringValue());
			checkBox.setValue(jobj.get("content").isBoolean().booleanValue());
			col11.setTextColor(Color.BLUE_DARKEN_2);
			col11.add(checkBox);
			col11.setFontSize("1.0em");
			col11.setTextAlign(TextAlign.LEFT);
			
			checkBox.addValueChangeHandler(event->{
				MaterialCheckBox chkBox = (MaterialCheckBox)event.getSource();
				boolean checkValue = chkBox.getValue().booleanValue();
				jobj.put("content", JSONBoolean.getInstance(checkValue));
				
				JSONObject permissionTableObj = (JSONObject) Registry.get(Registry.PERMISSION_CAPTION);
				JSONObject permissionObj = permissionTableObj.get(permissionId).isObject();

				JSONObject parameterJSON = new JSONObject();
				String usrId = accountListItem.getAccountId();
				String caption = permissionObj.get("caption").isString().stringValue();
				String parentCaption = permissionObj.get("parentCaption").isString().stringValue();
				
				parameterJSON.put("usrId", new JSONString(usrId));
				parameterJSON.put("permissionId", new JSONString(permissionId));
				parameterJSON.put("editUsrId", new JSONString(Registry.getUserId()));
				parameterJSON.put("caption", new JSONString(caption));
				parameterJSON.put("parentCaption", new JSONString(parentCaption));
				
				if (checkValue) {
					parameterJSON.put("permission", new JSONNumber(1));
					parameterJSON.put("cmd", new JSONString("PERMISSION_INSERT"));
					permissionList.add(permissionId);
				} else {
					parameterJSON.put("permission", new JSONNumber(0));
					parameterJSON.put("cmd", new JSONString("PERMISSION_DELETE")); 
					permissionList.remove(permissionId);
				}
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {}
				});
			});
			detailRow.add(col11);
			panel.add(detailRow);
		}

	}
	
	private void buildPermmitionTree() {
		
		JSONObject permObj = (JSONObject) Registry.get("PERMISSION_TEMPLATE");
		if (permObj != null && permObj.get("menu") != null) {
				
			JSONObject rootNode = permObj.get("menu").isObject();
			JSONArray mainNodes = rootNode.get("menu").isArray();
			
			
			int sizeMainNodes = mainNodes.size();
			for (int i=0; i<sizeMainNodes; i++) {
				
				JSONObject jObj = mainNodes.get(i).isObject();
				buildMenu(tree, jObj);
				
			}
		}
		
	}

	private void buildMenu(MaterialWidget widget, JSONObject jObj) {
		
		MaterialContentTreeItem mainNode = new MaterialContentTreeItem( jObj.get("caption").isString().stringValue(), IconType.PERM_IDENTITY);

		buildMenu(jObj, mainNode);
		buildPermission(jObj, mainNode);
		
		widget.add( mainNode );
	}

	private void buildMenu(JSONObject jObj, MaterialContentTreeItem mainNode) {
		if (jObj.get("menu") != null) {
			JSONValue permission = jObj.get("menu");
			if (permission instanceof JSONObject) {
				JSONObject permissionObject = permission.isObject();
				if (permissionObject != null) {
					MaterialContentTreeItem permNode = new MaterialContentTreeItem( permissionObject.get("caption").isString().stringValue(), IconType.PERM_IDENTITY);
					if (permNode.size() != 0)
						mainNode.add(permNode);
					buildMenu(mainNode, permissionObject);
				}
			} else if (permission instanceof JSONArray) {
				JSONArray permissionArray = permission.isArray();
				if (permissionArray != null) {
					int sizePermNodes = permissionArray.size();
					for (int p=0; p<sizePermNodes; p++) {
						JSONObject pObject = permissionArray.get(p).isObject();
						buildMenu(mainNode, pObject);
					}
				}
			}
		}
	}
	
	private void buildPermission(JSONObject jObj, MaterialContentTreeItem mainNode) {
		
		JSONObject permissionObject = jObj.get("permission").isObject();
		if (permissionObject != null) {
			List<JSONObject> permList = new ArrayList<JSONObject>();
			permList.add(permissionObject);
			mainNode.put("permission", permList);
		}
		
		JSONArray permissionArray = jObj.get("permission").isArray();
		if (permissionArray != null) {
			
			int length = permissionArray.size();
			List<JSONObject> permList = new ArrayList<JSONObject>();
			for (int i=0; i<length; i++) {
				permList.add(permissionArray.get(i).isObject());
			}
			mainNode.put("permission", permList);

		}
		
	}


	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}

}
