package kr.or.visitkorea.admin.client.manager.member;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersDetailDialog extends DialogContent {

	private MaterialLabel dialogTitle, mlresultmsg;
	private MaterialTextBox inputid, inputpass, inputpass2, inputname, inputmagname;
	SelectionPanel selisuse;
	private String snsid;
	private MaterialButton mbduchk;
	private boolean bInsert = true, bchk=false , clickchk = true;
	public PartnersDetailDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		snsid = this.getParameters().get("snsid")!=null?this.getParameters().get("snsid").toString():null;
		if(snsid == null) {
			dialogTitle.setText("Partners 계정 추가");
			bInsert = true;
			inputid.setReadOnly(false);
			mbduchk.setVisible(true);
			mbduchk.setText("중복체크");
			inputid.setText("");
			inputname.setText("");
			inputmagname.setText("");
			inputpass.setText("");
			inputpass2.setText("");
			selisuse.setSelectionOnSingleMode("사용");
		} else {
			dialogTitle.setText("Partners 계정 수정");
			bInsert = false;
			inputid.setReadOnly(true);
			mbduchk.setVisible(false);
//			Console.log(this.getParameters().get("isuse").toString());
			int use = Integer.parseInt(this.getParameters().get("isuse").toString());
			if(use == 1)
				selisuse.setSelectionOnSingleMode("사용");
			else selisuse.setSelectionOnSingleMode("사용중지");
			inputpass.setText("");
			inputpass2.setText("");
			inputid.setText(this.getParameters().get("id").toString());
			inputname.setText(this.getParameters().get("name").toString());
			inputmagname.setText(this.getParameters().get("magname").toString());
		}
		bchk = false;
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("Partners 계정 정보");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		mlresultmsg = new MaterialLabel("");
		mlresultmsg.setLayoutPosition(Position.ABSOLUTE);
		mlresultmsg.setFontSize("1.1em");
		mlresultmsg.setFontWeight(FontWeight.BOLD);
		mlresultmsg.setTextColor(Color.GREY_DARKEN_3);
		mlresultmsg.setTop(10);
		mlresultmsg.setLeft(230);
		this.add(mlresultmsg);
		
		buildBody();
	}
	

	private void buildBody() {
		MaterialRow mr1 = new MaterialRow();
		mr1.setLayoutPosition(Position.RELATIVE);
		mr1.setMarginTop(30);
		mr1.setPaddingLeft(30);
		mr1.setPaddingRight(30);
		MaterialLabel lb01 = new MaterialLabel("사용 여부");
		lb01.setLayoutPosition(Position.RELATIVE);
		lb01.setFloat(Float.LEFT);
		lb01.setWidth("130px");
		mr1.add(lb01);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("사용", 1);
		map.put("사용중지", 0);
		selisuse = new SelectionPanel();
		selisuse.setFloat(Float.LEFT);
		selisuse.setValues(map);
		selisuse.setSelectionOnSingleMode("사용");
		mr1.add(selisuse);
		this.add(mr1);
		
		MaterialRow mr2 = new MaterialRow();
		mr2.setLayoutPosition(Position.RELATIVE);
		mr2.setMarginTop(10);
		mr2.setPaddingLeft(30);
		mr2.setPaddingRight(30);
		
		inputid = new MaterialTextBox();
		inputid.setLabel("아이디");
		inputid.setFloat(Float.LEFT);
		inputid.setWidth("450px");inputid.setHeight("35px");
		mr2.add(inputid);
		inputid.addKeyDownHandler(ee->{
			mbduchk.setBackgroundColor(Color.BLUE);
			mbduchk.setText("중복체크");
			bchk = false;
		});
		
		mbduchk = new MaterialButton("중복체크");
		mbduchk.setFloat(Float.LEFT);
		mbduchk.setMarginLeft(20);
		mbduchk.setTop(5);
		mbduchk.setBackgroundColor(Color.BLUE);
		mr2.add(mbduchk);
		mbduchk.addClickHandler(ee->{
			if(bchk) return;
			if(inputid.getText().trim().equals("")) {
				Registry.showMsg(mlresultmsg, "사용할 아이디를 입력해주세요.", 3000);
				return;
			}
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("SELECT_CHKID_PARTNERS"));
			parameterJSON.put("id", new JSONString(inputid.getText().trim()));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
//						JSONArray bodyResultObj = (JSONArray) ((JSONObject) resultObj.get("body")).get("result");
						JSONObject bodyResultcnt = (JSONObject) ((JSONObject) resultObj.get("body")).get("resultCnt");
						if(bodyResultcnt.get("CNT").isNumber().doubleValue()>0) {
							Registry.showMsg(mlresultmsg, "이미 사용중인 아이디 입니다. 다른 이름을 정해주세요.", 3000);
							mbduchk.setBackgroundColor(Color.RED_DARKEN_1);
							mbduchk.setText("중복");
							bchk = false;
						} else {
							mbduchk.setBackgroundColor(Color.GREEN);
							mbduchk.setText("사용가능");
							bchk = true;
						}
					} else {}
				}
			});
		});
		this.add(mr2);
		
		
		MaterialRow mr3 = new MaterialRow();
		mr3.setLayoutPosition(Position.RELATIVE);
		mr3.setPaddingLeft(30);	mr3.setPaddingRight(30);
		this.add(mr3);
		inputpass = new MaterialTextBox();
		inputpass.setLabel("비밀번호");
		inputpass.setFloat(Float.LEFT);
		inputpass.setWidth("350px");inputpass.setHeight("35px");
		inputpass.setType(InputType.PASSWORD);
		mr3.add(inputpass);
		inputpass2 = new MaterialTextBox();
		inputpass2.setLabel("비밀번호확인");
		inputpass2.setFloat(Float.LEFT);
		inputpass2.setWidth("350px");	inputpass2.setHeight("35px");
		inputpass2.setMarginLeft(10);
		inputpass2.setType(InputType.PASSWORD);
		mr3.add(inputpass2);
		
		MaterialRow mr4 = new MaterialRow();
		mr4.setLayoutPosition(Position.RELATIVE);
		mr4.setPaddingLeft(30);
		mr4.setPaddingRight(30);
		this.add(mr4);
		
		MaterialRow mr5 = new MaterialRow();
		mr5.setLayoutPosition(Position.RELATIVE);
		mr5.setPaddingLeft(30);
		mr5.setPaddingRight(30);
		this.add(mr5);
		
		inputname = new MaterialTextBox();
		inputname.setLabel("기관명");
		inputname.setFloat(Float.LEFT);
		inputname.setWidth("550px");inputname.setHeight("35px");
		mr5.add(inputname);
		
		MaterialRow mr6 = new MaterialRow();
		mr6.setLayoutPosition(Position.RELATIVE);
		mr6.setPaddingLeft(30);
		mr6.setPaddingRight(30);
		
		inputmagname = new MaterialTextBox();
		inputmagname.setLabel("담당자");
		inputmagname.setFloat(Float.LEFT);
		inputmagname.setWidth("550px");inputmagname.setHeight("35px");
		mr6.add(inputmagname);
		this.add(mr6);
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
		buttonAreaPanel.setBottom(20); 
		
		closeButton = new MaterialButton("닫기");
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.setMarginLeft(20);
		closeButton.setId("close");
		closeButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(closeButton);
		
		saveButton = new MaterialButton("저장");
		saveButton.setLayoutPosition(Position.RELATIVE);
		saveButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		saveButton.setId("save");
		saveButton.addClickHandler(event->{
			if(bInsert && !bchk) {
				Registry.showMsg(mlresultmsg, "아이디 중복체크를 해주세요", 3000);
				return;
			}
			if(!inputpass.getText().equals(inputpass2.getText())) {
				Registry.showMsg(mlresultmsg, "비밀번호 확인을 다시 해주세요.", 3000);
				return;
			}
			JSONObject parameterJSON = new JSONObject();
			if(!inputpass.getText().trim().equals("")) {
				try {
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update(inputpass.getText().trim().getBytes()); 
					byte byteData[] = md.digest();
					StringBuffer sb = new StringBuffer(); 
					for(int i = 0 ; i < byteData.length ; i++){
						sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
					}
//					Console.log(sb.toString());
					parameterJSON.put("pass", new JSONString(sb.toString()));
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
			if(bInsert) {
				parameterJSON.put("cmd", new JSONString("INSERT_PARTNERS"));
				parameterJSON.put("snsid", new JSONString(IDUtil.uuid()));
				parameterJSON.put("id", new JSONString(inputid.getText().trim()));
//				parameterJSON.put("pass", new JSONString(inputpass.getText().trim()));
			} else {
				parameterJSON.put("cmd", new JSONString("UPDATE_PARTNERS"));
				parameterJSON.put("snsid", new JSONString(snsid));
//				if(!inputpass.getText().trim().equals("")) {
//					try {
//						MessageDigest md = MessageDigest.getInstance("SHA-256");
//						md.update(inputpass.getText().trim().getBytes()); 
//						byte byteData[] = md.digest();
//						StringBuffer sb = new StringBuffer(); 
//						for(int i = 0 ; i < byteData.length ; i++){
//							sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
//						}
////						Console.log(sb.toString());
//						parameterJSON.put("pass", new JSONString(sb.toString()));
//					} catch (NoSuchAlgorithmException e) {
//						e.printStackTrace();
//					}
//				}
			}
			
			parameterJSON.put("isuse", new JSONString(""+(int)selisuse.getSelectedValue()));
			
			parameterJSON.put("name", new JSONString(inputname.getText().trim()));
			parameterJSON.put("magname", new JSONString(inputmagname.getText().trim()));
			if(clickchk == true) {
				clickchk = false;
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
							getMaterialExtentsWindow().closeDialog();
						} else {
							MaterialToast.fireToast("저장 실패 !", 3000);
						}
						clickchk = true;
					}
				});
			} else {
				return;
			}
			if(handler != null) {
				handler.onClick(event);
			}
			
		});
		buttonAreaPanel.add(saveButton);
		this.add(buttonAreaPanel); 
	}

	@Override
	public int getHeight() {
		return 540;
	}
}
