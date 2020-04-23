package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;

import java.util.Collections;
import java.util.LinkedList;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.UserImageDetailItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UserImageDetailDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialPanel contentArea;
	public LinkedList<UserImageDetailItem> imageList;	
	private int itemSize;
	
	public UserImageDetailDialog(MaterialExtentsWindow tgrWindow, MemberActivityMain host) {
		super(tgrWindow);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	public int getHeight() {
		return 660;
	}

	public void buildContent() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel();
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		buildDetailTab();
	}
	
	public void buildDetailTab() {
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		row.setPadding(8);
		
		MaterialIcon saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setFloat(Float.RIGHT);
		saveIcon.setFontSize("25px");
		saveIcon.addClickHandler(event -> {
			saveIdxItems();
		});
		row.add(saveIcon);
		this.add(row);
		
		contentArea = new MaterialPanel();
		contentArea.setWidth("100%");
		contentArea.setHeight("500px");
		contentArea.setPadding(15);
		contentArea.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.add(contentArea);
	}
	
	public void saveIdxItems() {
		JSONArray imgListArray = new JSONArray();
		this.imageList.forEach(item -> {
			JSONObject obj = new JSONObject();
			obj.put("idx", new JSONNumber(item.getIdx()));
			obj.put("umgId", new JSONString(item.getUmgId()));
			obj.put("imgId", new JSONString(item.getImgId()));
			obj.put("visiable", new JSONNumber(item.getVisiable()));
			imgListArray.set(imgListArray.size(), obj);
		});
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_USER_IMAGE_IDX"));
		parameterJSON.put("imgList", imgListArray);
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (parma1, param2, param3) -> {
			alert("저장 완료", 400, 300, new String[] {
					"변경사항이 성공적으로 저장되었습니다."
			});
		});
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		contentArea.clear();
		if (this.getParameters().containsKey("title"))
			dialogTitle.setText("사용자 사진관리 :: " + this.getParameters().get("title"));
		if (this.getParameters().containsKey("cotId"))
			fetch(this.getParameters().get("cotId").toString());
	}
	
	public void orderUp(int index) {
		UserImageDetailItem originItem = this.imageList.get(index - 1);
		UserImageDetailItem item = this.imageList.get(index);

		Collections.swap(this.imageList, index, index - 1);
		originItem.setIdx(originItem.getIdx() + 1);
		item.setIdx(item.getIdx() - 1);
		reorder();
	}

	public void orderDown(int index) {
		UserImageDetailItem originItem = this.imageList.get(index + 1);
		UserImageDetailItem item = this.imageList.get(index);

		Collections.swap(this.imageList, index, index + 1);
		originItem.setIdx(originItem.getIdx() - 1);
		item.setIdx(item.getIdx() + 1);
		reorder();
	}
	
	public void reorder() {
		contentArea.clear();
		this.imageList.forEach(item -> {
			contentArea.add(item);
		});
	}

	public void fetch(String cotId) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_USER_IMAGE_WITH_COTID"));
		parameterJSON.put("cotId", new JSONString(cotId));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyObj = resultObj.get("body").isObject().get("result").isArray();
				
				itemSize = bodyObj.size();

				this.imageList = new LinkedList<>();
				
				for (int i = 0; i < itemSize; i++) {
					JSONObject obj = bodyObj.get(i).isObject();
					UserImageDetailItem item = new UserImageDetailItem(obj, this);
					contentArea.add(item);
					this.imageList.add(item);
				}
			} else {
				alert("오류 발생", 400, 300, new String[] {
						"이미지 목록을 불러오는중 오류가 발생했습니다.",
						"관리자에게 문의해주세요."
				});
			}
		});
	}

	public int getItemSize() {
		return itemSize;
	}
}
