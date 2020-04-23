package kr.or.visitkorea.admin.client.manager.event.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.event.EventApplication;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.dialogs.InsertOXQuizDialog;
import kr.or.visitkorea.admin.client.manager.event.model.EventOXQuiz;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventComponentOXQuiz extends AbstractEventComponent {
	private EventContentsTree host;
	private EventProcess selectedProcess = null;
	private MaterialIcon sizeIcon;
	private String subEvtId;
	private MaterialExtentsWindow window;
	private int sizeType;
	private MaterialLabel WinValue;
	private MaterialLink addIcon;
	private MaterialLink IndexUpIcon;
	private MaterialLink IndexDownIcon;
	private MaterialLink RemoveIcon;
	private List<EventOXQuiz> OxList;
	private List<EventOXQuiz> OxDeleteList;
	private SearchBodyWidget QuizTable;
	private int WinValueC;
	
	public EventComponentOXQuiz(EventComponentType componentType, AbstractContentPanel host, MaterialExtentsWindow window) {
		super(componentType, host);
		this.window = window;
		this.host = (EventContentsTree) host;
	}

	@Override
	protected void init() {
		this.OxList = new ArrayList<>();
		this.OxDeleteList = new ArrayList<>();
		
		this.titleLabel.setText("OX퀴즈");
		this.sizeIcon = this.addIcon(this.titleRow, IconType.CROP_7_5, Float.RIGHT);
		this.sizeIcon.setVisible(false);
		this.sizeIcon.setTooltip("와이드 형");
		this.sizeIcon.addClickHandler(e -> {
			if (this.sizeType == 1) {
				this.sizeIcon.setTextColor(Color.BLACK);
				this.titleLabel.setText("OX퀴즈 - " + this.selectedProcess.getTitle());
			} else {
				this.sizeIcon.setTextColor(Color.RED);
				this.titleLabel.setText("OX퀴즈 - "+ this.selectedProcess.getTitle() +"(와이드형)");
			}
			
			Console.log(this.sizeType+"사이즈");
			this.sizeType = this.sizeType == 1 ? 0 : 1;
			this.getComponentObj().setSizeType(sizeType);
		});
		
		MaterialRow row = new MaterialRow();
		MaterialLabel WinLabel = new MaterialLabel("당첨조건 정답 수  : ");
		WinLabel.setGrid("s3");
		WinLabel.setFontSize("20px");
		WinLabel.setFontWeight(FontWeight.BOLD);
		
		WinValue = new MaterialLabel("");
		WinValue.setGrid("s2");
		WinValue.setFontSize("20px");
		WinValue.setFontWeight(FontWeight.BOLD);
		WinValue.setTextAlign(TextAlign.LEFT);
		
		row.add(WinLabel);
		row.add(WinValue);
		
		row.setMarginTop(10);
		row.setMarginBottom(0);
		this.add(row);
		
		
		row = new MaterialRow();
		row.setMargin(0);
		row.setWidth("1045px");
		row.setPadding(10);
		row.setDisplay(Display.FLEX);
		row.setFlexAlignItems(FlexAlignItems.CENTER);
		
		QuizTable = new SearchBodyWidget();
		QuizTable.setWindow(this.window);
		QuizTable.setLayoutPosition(Position.RELATIVE);
		QuizTable.setWidth("100%");
		QuizTable.setHeight("350px");
		QuizTable.setHeaderTitleVisible(true);
		QuizTable.setHeaderTitle(new String[] {"번호", "문제", "정답", "힌트여부", "힌트"});
		QuizTable.setHeaderTitleWidths(new String[] {"130px", "300px", "130px", "130px", "333px"});
		QuizTable.render();
		
		addIcon = new MaterialLink(IconType.ADD);
		addIcon.setEnabled(false);
		addIcon.addClickHandler(e ->{
			 
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("subEvtId",this.subEvtId);
			this.window.addDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, new InsertOXQuizDialog(this.window,this));
			window.openDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, params, 700);
			
		});
		RemoveIcon = new MaterialLink(IconType.REMOVE);
		RemoveIcon.setEnabled(false);
		RemoveIcon.addClickHandler(event->{
			TagListRow selectedRow = (TagListRow) QuizTable.getSelectedRow();
			int index = this.QuizTable.getListBody().getWidgetIndex(selectedRow);
			
			OxDeleteList.add(OxList.get(index));
			OxList.remove(index);
			Refresh(this.isEditMode);
			
		});
		IndexUpIcon = new MaterialLink(IconType.ARROW_UPWARD);
		IndexUpIcon.setEnabled(false);
		IndexUpIcon.addClickHandler(event -> {
			if (this.QuizTable.getSelectedRow() == null) return;
			TagListRow selectedRow = (TagListRow) this.QuizTable.getSelectedRow();
			int index = this.QuizTable.getListBody().getWidgetIndex(selectedRow);
			
			if (index == 0)
				return;
			
			Collections.swap(OxList, index, index - 1);
			Refresh(this.isEditMode);
		});
		IndexDownIcon = new MaterialLink(IconType.ARROW_DOWNWARD);
		IndexDownIcon.setEnabled(false);
		IndexDownIcon.addClickHandler(event -> {
			if (this.QuizTable.getSelectedRow() == null) return;
			TagListRow selectedRow = (TagListRow) this.QuizTable.getSelectedRow();
			int index = this.QuizTable.getListBody().getWidgetIndex(selectedRow);
			
			if (index == this.QuizTable.getRows().size() - 1)
				return;
			Collections.swap(OxList, index, index + 1);
			Refresh(this.isEditMode);
		});
		QuizTable.addLink(addIcon,Float.LEFT);
		QuizTable.addLink(RemoveIcon,Float.LEFT);
		QuizTable.addLink(IndexUpIcon,Float.RIGHT);
		QuizTable.addLink(IndexDownIcon,Float.RIGHT);
		this.add(row);
		
		QuizTable.setVisible(true);
		
		row.add(QuizTable);
		row.setVisible(true);
		
	}

	@Override
	protected void modifyClickAction() {
		this.sizeIcon.setVisible(true);
		addIcon.setEnabled(true);
		RemoveIcon.setEnabled(true);
		IndexUpIcon.setEnabled(true);
		IndexDownIcon.setEnabled(true);
		
	}

	@Override
	protected void saveClickAction() {
		if(this.WinValueC > OxList.size()) {
			host.getMaterialExtentsWindow().alert("설정된 문제가 정답 수 보다 적어 저장할 수 없습니다.",500);
			return;
		}
		
		this.sizeIcon.setVisible(false);
		this.selectedProcess.setOXQuizList(OxList);
		this.selectedProcess.setOXQuizDeleteList(OxDeleteList);
		addIcon.setEnabled(false);
		RemoveIcon.setEnabled(false);
		IndexUpIcon.setEnabled(false);
		IndexDownIcon.setEnabled(false);
		
	}

	
	@Override
	public void setupContents() {
		this.subEvtId = this.getComponentObj().getSubEvtId();
		this.sizeType = this.getComponentObj().getSizeType();
		this.host.getContentProcess().getProcessList().forEach(item -> {
			if (item.getSubEvtId().equals(this.subEvtId)) {
				this.selectedProcess = item;
				if (this.sizeType == 0) {
					this.sizeIcon.setTextColor(Color.BLACK);
					this.titleLabel.setText("OX퀴즈 - " + this.selectedProcess.getTitle());
				} else {
					this.sizeIcon.setTextColor(Color.RED);
					this.titleLabel.setText("OX퀴즈 - "+ this.selectedProcess.getTitle() +"(와이드형)");
				}
			}
		});
		OxList = this.selectedProcess.getOXQuizList();
		WinValueC = this.selectedProcess.getWinCondValue();
		WinValue.setText(WinValueC+" 회 이상");
		Refresh(this.isEditMode);
	}
	
	
	@Override
	public void refresh() {
		Refresh(this.isEditMode);
	}
	
	
	public EventProcess getSelectedProcess() {
		return selectedProcess;
	}

	public void setSelectedProcess(EventProcess selectedProcess) {
		this.selectedProcess = selectedProcess;
	}

	public List<EventOXQuiz> getOXList(){
		return OxList;
	}
	
	public void addOXList(EventOXQuiz OX) {
		OxList.add(OX);
		List<VisitKoreaListCell> listCell = new ArrayList<VisitKoreaListCell>();
		listCell.add(new TagListLabelCell(OxList.size()+""
				, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(OX.isQuestiontype() ? OX.getQuestion() : OX.getQuestionimgAlt()
				, Float.LEFT, "300px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(OX.isAnswer() ? "O" : "X" 
				, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(OX.isHintYn() ? "있음" : "없음"
				, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(OX.getHintType() == 0 ? OX.getHintbody() :  OX.getHintType() == 1 ? OX.getHintbody() :OX.getHintimgAlt() 
				, Float.LEFT, "322px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		
		
		TagListRow tagRow = new TagListRow(listCell);
		tagRow.setHeight("40px");
		tagRow.setTagName(IDUtil.uuid());
		tagRow.put("INDEX", OxList.size()-1);
		tagRow.addDoubleClickHandler(event -> {
			if(!this.isEditMode)
				return;
			HashMap<String, Object> params = new HashMap<>();
			params.put("OX",OX);
			params.put("subEvtId",this.subEvtId);
			params.put("index",tagRow.get("INDEX"));
			this.window.addDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, new InsertOXQuizDialog(this.window,this));
			window.openDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, params, 700);
			
		});
		
		QuizTable.addRowForLastIndex(tagRow);
		
		
		
	}
	
	public void Refresh(boolean isEnable) {
		
		if(this.selectedProcess == null) {
			this.subEvtId = this.getComponentObj().getSubEvtId();
			this.sizeType = this.getComponentObj().getSizeType();
			this.host.getContentProcess().getProcessList().forEach(item -> {
				if (item.getSubEvtId().equals(this.subEvtId)) {
					this.selectedProcess = item;
					if (this.sizeType == 0) {
						this.sizeIcon.setTextColor(Color.BLACK);
						this.titleLabel.setText("OX퀴즈 - " + this.selectedProcess.getTitle());
					} else {
						this.sizeIcon.setTextColor(Color.RED);
						this.titleLabel.setText("OX퀴즈 - "+ this.selectedProcess.getTitle() +"(와이드형)");
					}
				}
			});
		}
		WinValueC = this.selectedProcess.getWinCondValue();
		WinValue.setText(WinValueC+" 회 이상");
		QuizTable.removeAll();
		
		for (int i = 0; i < OxList.size(); i++) {
			
			
			List<VisitKoreaListCell> listCell = new ArrayList<VisitKoreaListCell>();
			listCell.add(new TagListLabelCell(i+1+""
					, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
			listCell.add(new TagListLabelCell(OxList.get(i).isQuestiontype() ? OxList.get(i).getQuestion() : OxList.get(i).getQuestionimgAlt()
					, Float.LEFT, "300px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
			listCell.add(new TagListLabelCell(OxList.get(i).isAnswer() ? "O" : "X" 
					, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
			listCell.add(new TagListLabelCell(OxList.get(i).isHintYn() ? "있음" : "없음"
					, Float.LEFT, "130px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
			listCell.add(new TagListLabelCell(OxList.get(i).getHintType() == 0 ? OxList.get(i).getHintbody() :  OxList.get(i).getHintType() == 1 ? OxList.get(i).getHintbody() : OxList.get(i).getHintimgAlt() 
					, Float.LEFT, "322px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
			
			
			TagListRow tagRow = new TagListRow(listCell);
			tagRow.setHeight("40px");
			tagRow.setTagName(IDUtil.uuid());
			tagRow.put("INDEX", i);
			tagRow.addDoubleClickHandler(event -> {
				if(!this.isEditMode)
					return;
				HashMap<String, Object> params = new HashMap<>();
				params.put("OX", OxList.get((int)tagRow.get("INDEX")));
				params.put("subEvtId",this.subEvtId);
				params.put("index",tagRow.get("INDEX"));
				this.window.addDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, new InsertOXQuizDialog(this.window,this));
				window.openDialog(EventApplication.INSERT_EVENT_OXQUIZ_QUESTION, params, 700);
				
			});
			QuizTable.addRowForLastIndex(tagRow);
		}
		
	}
	

}