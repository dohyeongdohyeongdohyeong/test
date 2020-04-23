package kr.or.visitkorea.admin.client.manager.contents.banner;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.inputmask.MaterialInputMask;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadSimplePanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ModifyBannerDetailDialog extends DialogContent {

	private MaterialLabel dialogTitle, mlfile, mlresultmsg;
//	private MaterialButton mbtab1, mbtab2;
	private MaterialPanel mptab1, mptab2;
	private MaterialInputMask<Object> mIimgdes, mItitle, mIUrl;
	private MaterialComboBox<Object> cbidname;
	private MaterialInput edidname, mIname;
	private ContentTable table;
	private String mkbid, cotid, imgid;
	private String orderby;
	private FileUploadSimplePanel mfile;
	private String savePath;
	
	SelectionPanel selectTab;
	private boolean bCot = false;
	public ModifyBannerDetailDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	private int offset = 0;
	@Override
	protected void onLoad() {
		super.onLoad();
		mkbid = this.getParameters().get("mkbid").toString();
		orderby = this.getParameters().get("orderby").toString();
//		Console.log(this.getParameters().get("cotid").toString());
		if(this.getParameters().get("cotid") == null) {
			mItitle.setText(this.getParameters().get("title").toString());
			mIUrl.setText(this.getParameters().get("url").toString());
			selectTab.setSelectionOnSingleMode("외부 링크");
			setTab(0);
		} else {
			cotid = this.getParameters().get("cotid").toString();
			mIname.setText(this.getParameters().get("title").toString());
			selectTab.setSelectionOnSingleMode("콘텐츠 선택");
			setTab(1);
		}
		mlfile.setText("홍보 이미지 사이즈 : 111*111 - 이미지형식 : png");
		imgid = this.getParameters().get("imgid").toString();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_IMAGE_ID_LIST"));
		parameterJSON.put("imgId", new JSONString(imgid));
		table.loading(true);
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					JSONObject obj = (JSONObject)bodyResultObj.get(0);
					mIimgdes.setText(obj.get("imgdes")!=null?obj.get("imgdes").isString().stringValue():"");
				}
			}
		});
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("콘텐츠 등록");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		mlresultmsg = new MaterialLabel("");
		mlresultmsg.setLayoutPosition(Position.ABSOLUTE);
		mlresultmsg.setFontSize("1.2em");
		mlresultmsg.setFontWeight(FontWeight.BOLD);
		mlresultmsg.setTextColor(Color.GREY_DARKEN_3);
		mlresultmsg.setTop(10);
		mlresultmsg.setLeft(200);
		this.add(mlresultmsg);
		
		buildBody();
	}
	

	private void buildBody() {
		// 탭 부분 구현
		MaterialRow mr1 = new MaterialRow();
		mr1.setLayoutPosition(Position.RELATIVE);
		mr1.setMarginTop(10);
		mr1.setPaddingLeft(15);
		mr1.setPaddingRight(15);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("외부 링크", 0);
		map.put("콘텐츠 선택", 1);
		selectTab = UI.selectionPanelTabType(map);
		selectTab.setFloat(Float.LEFT);
		selectTab.addStatusChangeEvent(e->{
			setTab((Integer)selectTab.getSelectedValue());
		});
		mr1.add(selectTab);
		this.add(mr1);
		
		mptab1 = new MaterialPanel();
		mptab1.setLayoutPosition(Position.RELATIVE);
		mptab1.setMargin(15);
		mptab1.setWidth("100%");
		mItitle = new MaterialInputMask<Object>();
		mItitle.setLayoutPosition(Position.ABSOLUTE);
		mItitle.setLabel("제목");
		mItitle.setTop(0); mItitle.setWidth("680px"); mItitle.setHeight("40px");
		mItitle.setPaddingLeft(10);
		mptab1.add(mItitle);
		
		mIUrl = new MaterialInputMask<Object>();
		mIUrl.setLayoutPosition(Position.ABSOLUTE);
		mIUrl.setLabel("연결 Url");
		mIUrl.setTop(70); mIUrl.setWidth("680px"); mIUrl.setHeight("40px");
		mIUrl.setPaddingLeft(10);
		mptab1.add(mIUrl);
		
		mptab2 = new MaterialPanel();
		mptab2.setLayoutPosition(Position.RELATIVE);
		mptab2.setMargin(15);
		mptab2.setWidth("100%");
		cbidname = new MaterialComboBox<>();
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setWidth("150px");
		cbidname.setTop(0);
		cbidname.addItem("콘텐츠명", "0");
		cbidname.addItem("대표태그", "1");
		mptab2.add(cbidname);
		edidname = new MaterialInput();
		edidname.setBackgroundColor(Color.WHITE);
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setWidth("410px");
		edidname.setTop(15);
		edidname.setLeft(160);
		edidname.setPaddingLeft(10);
		mptab2.add(edidname);
		MaterialButton mIsearch = new MaterialButton("검색");
		mIsearch.setLayoutPosition(Position.ABSOLUTE);
		mIsearch.setTop(20);
		mIsearch.setLeft(590);
		mIsearch.setBackgroundColor(Color.BLUE_DARKEN_3);
		mIsearch.setWidth("95px");
		mIsearch.setHeight("40px");
		mIsearch.addClickHandler(e->{
			mlresultmsg.setText("");
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("SELECT_BANNER_CONTENT_LIST"));
			String idname = edidname.getText();
			if(!"".equals(idname)) {
				if(cbidname.getSelectedIndex()== 0)
					parameterJSON.put("title", new JSONString(idname));
				else parameterJSON.put("tag", new JSONString(idname));
			}
			if(idname == null || idname.trim().equals("")) {
				mlresultmsg.setText("최소한의 검색어를 입력해야 합니다.");
				return ;
			}
			parameterJSON.put("offset", new JSONString(offset+""));
			table.loading(true);
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
//						JSONObject bodyResultcnt = (JSONObject) bodyObj.get("resultCnt");
//						countLabel.setText(bodyResultcnt.get("CNT")+" 건");
//						totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
						
						int usrCnt = bodyResultObj.size();
						if (usrCnt == 0) {
							mlresultmsg.setText("검색된 결과가 없습니다.");
//							MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
						}

						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							ContentTableRow tableRow = table.addRow(Color.WHITE 
									,obj.get("cid")!=null?obj.get("cid").isString().stringValue():"없음"
									,"전국"
									,obj.get("title")!=null?obj.get("title").isString().stringValue():"제목없음"
									,obj.get("tagname")!=null?"#"+obj.get("title").isString().stringValue():"태그없음"
									,"[미리보기]"
									,"[선택]"
									);
							tableRow.put("title", obj.get("title").isString().stringValue());
							tableRow.put("cotid", obj.get("cotid").isString().stringValue());
							tableRow.addClickHandler(e->{
								ContentTableRow ctr = (ContentTableRow)e.getSource();
								if(e.getX() >500 && e.getX() < 570) {//노출 비노출 설정
									String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid="+ctr.get("cotid").toString();
									Registry.openPreview(new MaterialIcon(IconType.WEB), tgrUrl);
								} else if(e.getX() >600 && e.getX() < 650) {
									mIimgdes.setText(ctr.get("title").toString());
									cotid = ctr.get("cotid").toString();
									Console.log(cotid);
								}
							});
						}
					}
					table.loading(false);
				}
			});
		});
		
		mptab2.add(mIsearch);
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.getTopMenu().setVisible(false);
		table.getButtomMenu().setVisible(false);
		table.setHeight(175);
		table.setWidth("685px");
		table.setTop(40);
		table.appendTitle("CID", 100, TextAlign.CENTER);
		table.appendTitle("지역", 80, TextAlign.CENTER);
		table.appendTitle("콘텐츠명", 200, TextAlign.CENTER);
		table.appendTitle("대표태그", 100, TextAlign.CENTER);
		table.appendTitle("미리보기", 100, TextAlign.CENTER);
		table.appendTitle("선택", 100, TextAlign.CENTER);
		mptab2.add(table);
		MaterialLabel mlname = new MaterialLabel("콘텐츠명");
		mlname.setLayoutPosition(Position.ABSOLUTE);
		mlname.setBackgroundColor(Color.GREY_LIGHTEN_2);
		mlname.setBorderRadius("2px");
		mlname.setTop(195);
		mlname.setPadding(10);
		mlname.setWidth("150px");
		mlname.setHeight("40px");
		mlname.setTextAlign(TextAlign.CENTER);
		mptab2.add(mlname);
		mIname= new MaterialInput();
		mIname.setLayoutPosition(Position.ABSOLUTE);
		mIname.setTop(195);
		mIname.setLeft(155);
		mIname.setPaddingLeft(10);
		mIname.setBackgroundColor(Color.WHITE);
		mIname.setWidth("530px");
		mIname.setHeight("40px");
		mptab2.add(mIname);
		
		this.add(mptab1);
		this.add(mptab2);
		
		setTab(0);
		
		mfile = new FileUploadSimplePanel(170, 40, Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		mfile.setLayoutPosition(Position.ABSOLUTE);
		mfile.setBackgroundColor(Color.BLUE_DARKEN_3);
		mfile.setTop(350); mfile.setLeft(15);
		mfile.getUploader().setAcceptedFiles("image/*"); 
		mlfile = new MaterialLabel();
		mlfile.setLayoutPosition(Position.ABSOLUTE);
		mlfile.setTop(350);
		mlfile.setLeft(190);
		mlfile.setWidth("530px");
		mlfile.setText("");
		mfile.getUploader().addSuccessHandler(event->{
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			mlfile.setText((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
			imgid = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			String[] imgMainSplitArr = imgid.split("-");
			savePath = "";
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			imgid = IDUtil.uuid();
//			mfile.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
		});
		this.add(mlfile);
		this.add(mfile);
		
		mIimgdes = new MaterialInputMask<Object>();
		mIimgdes.setLayoutPosition(Position.ABSOLUTE);
		mIimgdes.setLabel("이미지설명");
		mIimgdes.setTop(400); mIimgdes.setLeft(10); mIimgdes.setWidth("680px"); mIimgdes.setHeight("40px");
		mIimgdes.setPaddingLeft(10);
		this.add(mIimgdes);
	}
	private void setTab(int idx) {
		if(idx == 0) {
			mptab1.setVisible(true);
			mptab2.setVisible(false);
			bCot = false;
		} else {
			mptab2.setVisible(true);
			mptab1.setVisible(false);
			bCot = true;
		}
	}
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		
	}
	
	private MaterialPanel buttonAreaPanel;
	protected MaterialButton saveButton;
	protected MaterialButton closeButton;
//	private ClickHandler handler;
	protected void addDefaultButtons() {
		buttonAreaPanel = new MaterialPanel();
		buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
		buttonAreaPanel.setWidth("100%");
		buttonAreaPanel.setPaddingLeft(20);
		buttonAreaPanel.setPaddingRight(20);
		buttonAreaPanel.setLeft(0); 
		buttonAreaPanel.setBottom(15); 
		
		closeButton = new MaterialButton("닫기");
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.setMarginLeft(20);
		closeButton.setId("close");
		closeButton.addClickHandler(event->{
			table.clearRows();
			getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(closeButton);
		
		saveButton = new MaterialButton("저장");
		saveButton.setLayoutPosition(Position.RELATIVE);
		saveButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		saveButton.setId("save");
		saveButton.addClickHandler(event->{
			// insert image
			JSONObject parameterJSON = new JSONObject();
			if(savePath != null) {
				parameterJSON.put("cmd", new JSONString("INSERT_IMAGE_NOT_COTID"));
				parameterJSON.put("imgId", new JSONString(imgid));
				parameterJSON.put("imgPath", new JSONString(savePath));
				parameterJSON.put("imgDesc", new JSONString(mIimgdes.getText().trim()));
			} else {
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_DESCRIPTION"));
				parameterJSON.put("imgId", new JSONString(imgid));
				parameterJSON.put("desc", new JSONString(mIimgdes.getText().trim()));
			}
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
				}
			});

			parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_BANNER_DETAIL"));
			parameterJSON.put("mkbid", new JSONString(mkbid));
			parameterJSON.put("orderby", new JSONString(orderby));
//			parameterJSON.put("title", new JSONString(mIimgdes.getText().trim()));
			parameterJSON.put("title", new JSONString(mItitle.getText().trim()));
			if(imgid != null) {
				parameterJSON.put("imgid", new JSONString(imgid));
			}
			if(!bCot) {
				parameterJSON.put("url", new JSONString(mIUrl.getText().toString().trim()));
			} else {
				parameterJSON.put("cotid", new JSONString(cotid));
			}
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
			});
			
			
			if(handler != null) {
				handler.onClick(event);
			}
			table.clearRows();
			getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(saveButton);
		this.add(buttonAreaPanel); 
	}

	@Override
	public int getHeight() {
		return 520;
	}
}
