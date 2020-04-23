package kr.or.visitkorea.admin.client.manager.partners.affiliateProposal;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 제휴/제안 신청 Main
 */
public class PartnersAffiliateProposalMain extends AbstractContentPanel {
	// 화면 레이아웃에 사용될 컴포넌트를 상단 멤버변수로 선언
	private MaterialComboBox<Object> type;
	private MaterialComboBox<Object> status;
	private ContentTable table;
	private MaterialTextBox searchName;
	private MaterialIcon searchIcon;
	private MaterialLabel countLabel;
	private int index;
	private int offset;
	private int totalCount;
	
	private static Map<String, Object> detail;
	private boolean permissionSearchEnabled;
	
	public PartnersAffiliateProposalMain(MaterialExtentsWindow materialExtentsWindow, PartnersAffiliateProposalApplication pa) {
		super(materialExtentsWindow);
		
		// 검색 조건 영역 권한 제어
		permissionSearchEnabled = Registry.getPermission("4df4fbcf-4dcd-4490-8c18-ec8701436336");
		// 구분 권한 제어
		type.setEnabled(permissionSearchEnabled);
		// 처리 상태 권한 제어
		status.setEnabled(permissionSearchEnabled);
		// 검색어 입력 권한 제어
		searchName.setEnabled(permissionSearchEnabled);
		// 검색 아이콘  권한 제어
		searchIcon.setEnabled(permissionSearchEnabled);
	}
	
	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		
		buildLayout();
	}
	
	private void buildLayout() {
		MaterialPanel topPanel = new MaterialPanel();
		topPanel.setWidth("100%");
		topPanel.setHeight("80px");
		topPanel.setPadding(10);
		
		// 구분 콤보박스 셋팅
		type = new MaterialComboBox<>();
		type.setLabel("구분");
		type.setLayoutPosition(Position.ABSOLUTE);
		type.setTop(5); type.setLeft(10); type.setWidth("150px");
		type.addItem("전체", -1);
		type.addItem("제휴", 1);
		type.addItem("제안", 2);
		type.setSelectedIndex(0);
		topPanel.add(type);
		
		// 처리 상태 콤보박스 셋팅
		status = new MaterialComboBox<Object>();
		status.setLabel("처리 상태");
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(5); status.setLeft(190); status.setWidth("120px");
		status.addItem("전체", -1);
		status.addItem("접수", 0);
		status.addItem("확인 중", 1);
		status.addItem("답변 완료", 2);
		// 기본 선택된 아이템 지정
		status.setSelectedIndex(0);
		topPanel.add(status);
		
		this.add(topPanel);
		
		MaterialPanel tablePanel = new MaterialPanel();
		tablePanel.setWidth("100%"); tablePanel.setHeight("670px");
		tablePanel.setBackgroundColor(Color.WHITE);
		tablePanel.add(topPanel);
		
		// 검색어 입력 텍스트박스
		searchName = new MaterialTextBox();
		searchName.setLabel("검색어 입력");
		searchName.setLayoutPosition(Position.ABSOLUTE);
		searchName.setTop(5); searchName.setLeft(340); searchName.setWidth("1050px");
		topPanel.add(searchName);
		
		// 테이블 칼럼명 셋팅
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40); table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		table.appendTitle("번호", 70, TextAlign.CENTER);
		table.appendTitle("구분", 150, TextAlign.CENTER);
		table.appendTitle("아이디", 150, TextAlign.CENTER);
		table.appendTitle("제목", 350, TextAlign.CENTER);
		table.appendTitle("등록 일시", 200, TextAlign.CENTER);
		table.appendTitle("처리 일시", 200, TextAlign.CENTER);
		table.appendTitle("처리 상태", 150, TextAlign.CENTER);
		table.appendTitle("댓글 확인", 130, TextAlign.CENTER);
		tablePanel.add(table);
		
		// 검색 아이콘 셋팅
		searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event -> {
			queryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		// 추가 목록 조회 아이콘
		MaterialIcon nextIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextIcon.setTextAlign(TextAlign.CENTER);
		nextIcon.addClickHandler(event -> {
			if ((offset + 20) >= totalCount)
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			else
				queryList(false);
		});
		nextIcon.setEnabled(Registry.getPermission("1c25b9a8-2273-4057-9782-b9c0168a5897"));
		table.getButtomMenu().addIcon(nextIcon, "다음 20개", Style.Float.RIGHT);
		
		// 조회된 목록 건수
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, Style.Float.RIGHT);
		
		this.add(tablePanel);
	}
	
	public void queryList(boolean bstart) {
		// 조회 범위 지정, default: 0~20, step: +20
		if (bstart) {
			offset = 0;
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		String searchNameCheck = searchName.getText();
		String typeCheck = type.getSelectedValue().get(0).toString();
		String statusCheck = status.getSelectedValue().get(0).toString();
		JSONObject paramJson = new JSONObject();
		
		// server code logic 호출 및 business logic에 필요 변수 등록 
		// client <-> server 통신 간에 gwt에서 제공되는 json 타입의 객체 사용
		paramJson.put("cmd", new JSONString("SELECT_PARTNERS_AFFPRO_LIST"));
		if (!"".equals(searchNameCheck))
			paramJson.put("title", new JSONString(searchNameCheck));
		if (!"".equals(typeCheck))
			paramJson.put("type", new JSONString(typeCheck));
		if (!"".equals(statusCheck))
			paramJson.put("status", new JSONString(statusCheck));
		
		paramJson.put("offset", new JSONString(offset + ""));
		
		// 처리 전까지 대기 시간에 로드될 UI start
		table.loading(true);
		
		VisitKoreaBusiness.post("call", paramJson.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultJson = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject headerJson = (JSONObject) resultJson.get("header");
				String procResult = headerJson.get("process").isString().stringValue();
				
				// server 처리에서 성공 여부를 판별
				if (procResult.equals("success")) {
					// server에서 return된 데이터를 가지고 있는 key
					JSONObject bodyObject = (JSONObject) resultJson.get("body");
					JSONArray bodyResultObject = (JSONArray) bodyObject.get("result");
					JSONObject bodyResultCount = (JSONObject) bodyObject.get("resultCount");
					countLabel.setText(bodyResultCount.get("CNT").isNumber().toString() + " 건");
					totalCount = Integer.parseInt(bodyResultCount.get("CNT").isNumber().toString());
					
					// server에서 성공적으로 처리되었으나 조회된 결과가 없을 경우
					int count = bodyResultObject.size();
					if (count == 0)
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					
					for (int i = 0; i < count; i++) {
						JSONObject dataSet = (JSONObject) bodyResultObject.get(i);
						
						// server에서 조회된 데이터를 기준으로 contentTableRow에 데이터 바인딩
						ContentTableRow tableRow = table.addRow(Color.WHITE
								, new int[]{3}
								, ""+(index++)
								, dataSet.get("TYPE").isNumber().toString().equals("1") ? "제휴" : dataSet.get("TYPE").isNumber().toString().equals("2") ? "제안" : "-"
								, dataSet.get("ID")!=null?dataSet.get("ID").isString().stringValue():"-"
								, dataSet.get("TITLE")!=null?dataSet.get("TITLE").isString().stringValue():"-"
								, dataSet.get("CREATE_DATE")!=null? dataSet.get("CREATE_DATE").isString().stringValue() : "-"
								, dataSet.get("UPDATE_DATE") !=null? dataSet.get("UPDATE_DATE").isString().stringValue():"-"
								, dataSet.get("STATUS").isNumber().toString().equals("0") ? "접수" : 
									dataSet.get("STATUS").isNumber().toString().equals("1") ? "확인 중" : 
										dataSet.get("STATUS").isNumber().toString().equals("2") ? "답변 완료" : "-"
								, dataSet.get("REPLY") != null ? dataSet.get("REPLY").isString().stringValue() : "-"
						);
						
						// row에 클릭 이벤트 발생 시 처리될 로직
						tableRow.addClickHandler(event -> {
							detail = new HashMap<String, Object>();
							detail.put("ppsId", dataSet.get("PPS_ID").isString().stringValue());
							detail.put("title", dataSet.get("TITLE").isString().stringValue());
							detail.put("body", dataSet.get("BODY").isString().stringValue());
							detail.put("type", dataSet.get("TYPE").isNumber());
							detail.put("status", dataSet.get("STATUS").isNumber());
							detail.put("id", dataSet.get("ID").isString().stringValue());
							detail.put("row", event.getSource());
							
							ContentTableRow ctr = (ContentTableRow) event.getSource();
							// 클릭 이벤트 발생 시 선택된 칼럼 번호 판별
							if (ctr.getSelectedColumn() == 3)
								// 권한 여부를 판별하여 조건 충족 시 상세 페이지 링크로 이동
								if (Registry.getPermission("f4bb3e00-a3fa-4dbb-aeb4-949e543b75c3"))
									getMaterialExtentsWindow().openDialog(PartnersAffiliateProposalApplication.CONTENT_DETAIL, detail, 1500);
						});
					}
				} else if (procResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				// 처리 전까지 대기 시간에 로드될 UI end
				table.loading(false);
			}
		});
	}
}