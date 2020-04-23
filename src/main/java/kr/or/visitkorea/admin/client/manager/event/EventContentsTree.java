package kr.or.visitkorea.admin.client.manager.event;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.Contents;
import kr.or.visitkorea.admin.client.manager.event.components.AbstractEventComponent;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentFactory;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentTreeFactory;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentType;
import kr.or.visitkorea.admin.client.manager.event.components.IEventComponent;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsBase;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsComponents;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsImages;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsProcess;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsPromotion;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsResultComponents;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsResultTemplate;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsTags;
import kr.or.visitkorea.admin.client.manager.event.model.EventComponent;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;
import kr.or.visitkorea.admin.client.manager.event.tree.EventBaseRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventGiftTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventImageRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventProcessRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventProcessTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventPromotionRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventResultRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventTagRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.EventViewRootTreeItem;
import kr.or.visitkorea.admin.client.manager.event.tree.SelectEvent;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTree;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsTree extends AbstractContentPanel implements Contents {
	private String evtId;
	private String cotId;
	private String contentTitle;
	private EventStatus eventStatus;
	private MaterialLabel titleLabel;
	private EventContentsBase contentBase;
	private EventContentsProcess contentProcess;
	private EventContentsComponents contentComponents;
	private EventContentsPromotion contentPromotion;
	private EventContentsTags contentTags;
	private EventContentsImages contentImages;
	private EventContentsResultTemplate contentResultTemplate;
	private EventContentsResultComponents contentResultComponents; 
	private ContentTree tree;
	private MaterialPanel panelTop;
	private AbstractContentPanel panelContentTop;
	private MaterialPanel topSlider;
	private MaterialPanel fixedTop;
	private MaterialWidget detailSliderPanel;
	private int nowPosition;
	private MaterialIcon addIcon;
	private MaterialIcon removeIcon;
	private MaterialIcon textAddIcon;
	private MaterialIcon imageAddIcon;
	private MaterialIcon rouletteAddIcon;
	private MaterialIcon OXQuizAddIcon;
	private MaterialButton submitBtn;
	private MaterialButton approvalCancelBtn;
	private MaterialIcon saveIcon;
	private MaterialIcon backIcon;
	private EventTreeItem baseTreeItem;
	private EventTreeItem processTreeItem;
	private EventTreeItem viewTreeItem;
	private EventTreeItem promotionTreeItem;
	private EventTreeItem tagTreeItem;
	private EventTreeItem imageTreeItem;
	private EventTreeItem resultTreeItem;
	private boolean checkfirst = false;
	private EnumSet<EventStatus> editPossSet = EnumSet.of(
				EventStatus.WRITING, EventStatus.PROGRESSING, EventStatus.NEGATIVE, EventStatus.END, EventStatus.ANNOUNCE
			);	// 이벤트 내용을 수정 가능한 이벤트 상태값 EnumSet
	private MaterialLink previewLink;
	private int annouce_type;
	
	static {
		MaterialDesignBase.injectCss(EventContentBundle.INSTANCE.eventCss());
	}

	public EventContentsTree(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setFloat(Float.LEFT);
		
		this.buildTitle();
		this.buildLayout();
		this.buildMainTree();
		
		this.add(this.backIcon = this.createBackIcon());
		this.add(this.saveIcon = this.createSaveIcon());
	} 
	
	@Override
	public void contentSaveData() {
		
		this.contentBase.saveData2(contentProcess,contentComponents);
		this.contentPromotion.saveData();
		this.contentTags.saveData();
		this.contentImages.saveData();
		this.contentResultTemplate.saveData();
		this.contentResultComponents.saveData(); 
		this.contentStatusProcess();
	}	//	Parallel Save
	
	@Override
	public void contentLoadData() {
		this.contentBase.loadData(null);
		this.contentProcess.loadData(result -> {
			if (result) {
				this.contentComponents.loadData(null);
			}
		});
		this.contentPromotion.loadData(null);
		this.contentTags.loadData(null);
		this.contentImages.loadData(null);
		this.contentResultTemplate.loadData(null);
		this.contentResultComponents.loadData(null); 
		this.contentStatusProcess();
	}	//	Parallel Load
	
	public void contentStatusProcess() {
		this.statusChangeProcess(this.getEventStatus());
		this.contentBase.statusChangeProcess(this.getEventStatus());
		this.contentProcess.statusChangeProcess(this.getEventStatus());
		this.contentComponents.statusChangeProcess(this.getEventStatus());
		this.contentPromotion.statusChangeProcess(this.getEventStatus());
		this.contentTags.statusChangeProcess(this.getEventStatus());
		this.contentImages.statusChangeProcess(this.getEventStatus());
		this.contentResultTemplate.statusChangeProcess(this.getEventStatus());
		this.contentResultComponents.statusChangeProcess(this.getEventStatus()); 
	}	//	Parallel process for change status

	public void statusChangeProcess(EventStatus status) {
		switch (status) {
			case WRITING:
			case PROGRESSING:
			case END: {
				this.visibleProgressChangeBtn(false, false);
			} break;
			case ANNOUNCE: {
				this.visibleProgressChangeBtn(false, false);
			} break;
			case NEGATIVE: {
				this.visibleProgressChangeBtn(true, false);
				this.saveIcon.setVisible(true);
			} break;
			
			case APPROVAL_WAIT: {
				this.visibleProgressChangeBtn(false, true);
				this.visibleComponentsIcon(false);
				this.visibleTreeHandleIcon(false, false);
				this.saveIcon.setVisible(false);
			} break;
			
			default: {
				this.visibleProgressChangeBtn(false, false);
				this.visibleComponentsIcon(false);
				this.visibleTreeHandleIcon(false, false);
				this.saveIcon.setVisible(false);
			} break;
		}
	}
	
	public void setEvtId(String evtId) {
		this.evtId = evtId;
		this.contentBase.setEvtId(this.evtId);
		this.contentProcess.setEvtId(this.evtId);
		this.contentComponents.setEvtId(this.evtId);
		this.contentPromotion.setEvtId(this.evtId);
		this.contentTags.setEvtId(this.evtId);
		this.contentImages.setEvtId(this.evtId);
		this.contentResultTemplate.setEvtId(this.evtId);
		this.contentResultComponents.setEvtId(this.evtId);
	}
	
	@Override
	public void setCotId(String cotId) {
		this.cotId = cotId;
		this.contentBase.setCotId(this.cotId);
		this.contentProcess.setCotId(this.cotId);
		this.contentComponents.setCotId(this.cotId);
		this.contentPromotion.setCotId(this.cotId);
		this.contentTags.setCotId(this.cotId);
		this.contentImages.setCotId(this.cotId);
		this.contentResultTemplate.setCotId(this.cotId);
		this.contentResultComponents.setCotId(this.cotId);
	}

	/**
	 * 내용을 수정 가능한 이벤트 상태값 getter
	 * @return editPossSet
	 */
	public EnumSet<EventStatus> getEditPossSet() {
		return editPossSet;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}
	
	public void setEventStatus(EventStatus status) {
		this.eventStatus = status;
		this.contentBase.setStatus(status);
		this.contentComponents.setEventStatus(status);
		this.contentResultComponents.setEventStatus(status);
		EventContentsList contentsTree = (EventContentsList) getMaterialExtentsWindow().getContentPanel(0);
		if (contentsTree.getTable().getSelectedRows().size() != 0) {
			ContentTableRow selectedRow = contentsTree.getTable().getSelectedRows().get(0);
			selectedRow.put("status", this.eventStatus.getValue());
			
		}
	}

	public void initialize() {
		this.clear();
		this.init();
	}
	
	public void setcheckfirst(boolean check) {
		checkfirst = check;
	}
	
	private MaterialIcon createSaveIcon() {
		MaterialIcon saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setLayoutPosition(Position.ABSOLUTE);
		saveIcon.setTooltip("최종저장");
		saveIcon.setRight(70);
		saveIcon.setTop(25);
		saveIcon.setWidth("24");
		saveIcon.setBorder("1px solid #e0e0e0");
		saveIcon.addClickHandler(event -> {
			this.contentSaveData();
		});
		return saveIcon;
	}
	
	private MaterialIcon createBackIcon() {
		MaterialIcon backIcon = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);
		backIcon.setLayoutPosition(Position.ABSOLUTE);
		backIcon.setTooltip("목록으로");
		backIcon.setRight(30);
		backIcon.setTop(25);
		backIcon.setWidth("24");
		backIcon.setBorder("1px solid #e0e0e0");
		backIcon.addClickHandler(event -> {
			EventContentsList main = (EventContentsList) this.getMaterialExtentsWindow().getContentPanel(0);
			main.setEventClickcheck(true);
			getMaterialExtentsWindow().goContentSlider(0);
			
		});
		return backIcon;
	}
	
	//	Panel title label build
	private void buildTitle() {
		titleLabel = new MaterialLabel();
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE_DARKEN_2);
		titleLabel.setFontSize("1.4em");
		this.add(titleLabel);

		previewLink = new MaterialLink(IconType.PAGEVIEW);
		previewLink.setLayoutPosition(Position.ABSOLUTE);
		previewLink.setTooltip("미리보기");
		previewLink.setRight(105);
		previewLink.setTop(20);
		previewLink.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		previewLink.getElement().getFirstChildElement().getStyle().setFontSize(2.4, Unit.EM);
		previewLink.addClickHandler(event->{
			Registry.openPreview(this.previewLink, Registry.get("service.server") + "/detail/event_detail.do?cotid=" + this.cotId);
		});
		this.add(previewLink);
	}
	
	public void setTitle(String title) {
		super.setTitle(title);
		titleLabel.setText(title);
	}

	public void appendProcessTreeItem(List<EventProcess> processList) {
		EventTreeItem parentItem = null;
		List<Widget> parentChildList = this.tree.getChildrenList();
		for (int i = 0; i < parentChildList.size(); i++) {
			Widget item  = parentChildList.get(i);
			if (item instanceof EventProcessRootTreeItem) {
				parentItem = (EventTreeItem) item;
				parentItem.getTreeItems().forEach(it -> {
					it.removeFromParent();
				});
				break;
			}
		}
		
		for (int i = 0; i < processList.size(); i++) {
			EventProcess eventProcess = processList.get(i);
			EventProcessTreeItem processTreeItem = this.addProcess(parentItem, eventProcess);
			
//			List<EventGift> giftList = eventProcess.getGiftList();
//			giftList.stream()
//					.sorted((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : -1)
//					.forEach(gift -> {
//				if (gift.isNotWin()) {
//					this.contentProcess.setNotWinObj(gift);
//				}
//				this.addGift(processTreeItem, gift);
//			});
		}
	}
	
	public void buildLayout() {
		MaterialRow row = new MaterialRow();	
		row.setLayoutPosition(Position.RELATIVE);
		row.setHeight("695px");
		
		//	BEGIN : Setting Grid of Detail panel
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(54);
		col1.setGrid("s3");
		col1.setHeight("100%");
		row.add(col1);
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingBottom(30);
		col2.setGrid("s9");
		col2.setHeight("100%");
		row.add(col2);
		//	END : Setting Grid of Detail panel

		fixedTop = new MaterialPanel();
		fixedTop.setTop(20);
		fixedTop.setHeight("100%");
		fixedTop.setOverflow(Overflow.HIDDEN);
		fixedTop.setBorder("1px solid #e0e0e0");
		fixedTop.setLayoutPosition(Position.RELATIVE);
		col1.add(fixedTop);
		
		topSlider = new MaterialPanel();
		topSlider.setLayoutPosition(Position.ABSOLUTE);
		topSlider.setTop(0);
		topSlider.setLeft(0);
		topSlider.setOverflow(Overflow.HIDDEN);
		topSlider.setWidth(((458*3)) + "px");
		topSlider.setHeight("100%");
		fixedTop.add(topSlider);
		
		panelTop = new MaterialPanel();
		panelTop.setStyle("overflow-x: hidden; overflow-y: auto;"); 
		panelTop.setLayoutPosition(Position.RELATIVE);
		panelTop.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		panelTop.setWidth("332px");
		panelTop.setHeight("100%");
		topSlider.add(panelTop);
		
		//	BEGIN : Left Tree Bottom Panel Area
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setBorderTop("0px");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(20);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		col1.add(panelBottom);

//		MaterialIcon goTreePrevIcon = addIcon(IconType.KEYBOARD_ARROW_RIGHT, Float.RIGHT, true);
//		goTreePrevIcon.addClickHandler(e -> {
//			this.goTree(2);
//		});
		
//		MaterialIcon goTreeNextIcon = addIcon(IconType.KEYBOARD_ARROW_LEFT, Float.RIGHT, true);
//		goTreeNextIcon.addClickHandler(e -> {
//			this.goTree(1);
//		});

		textAddIcon = addIcon(IconType.VIEW_HEADLINE, Float.LEFT, false);
		textAddIcon.setDisplay(Display.NONE);
		textAddIcon.setTooltip("텍스트 추가");
		textAddIcon.addClickHandler(e -> {
			EventTreeItem selectedItem = (EventTreeItem) this.tree.getSelectedItem();
			if (selectedItem instanceof EventViewRootTreeItem) {

					createComponent(EventComponentType.TEXT);
			} else if(selectedItem instanceof EventResultRootTreeItem){
					createResultComponent(EventComponentType.TEXT);
			}
		});
		
		imageAddIcon = addIcon(IconType.IMAGE, Float.LEFT, false);
		imageAddIcon.setDisplay(Display.NONE);
		imageAddIcon.setTooltip("이미지 추가");
		imageAddIcon.addClickHandler(e -> {
			EventTreeItem selectedItem = (EventTreeItem) this.tree.getSelectedItem();
			if (selectedItem instanceof EventViewRootTreeItem) {
				createComponent(EventComponentType.IMAGE);
			} else if(selectedItem instanceof EventResultRootTreeItem){
				createResultComponent(EventComponentType.IMAGE);
			}
		});
		
		rouletteAddIcon = addIcon(IconType.TIMELAPSE, Float.LEFT, false);
		rouletteAddIcon.setDisplay(Display.NONE);
		rouletteAddIcon.setTooltip("룰렛 추가");
		rouletteAddIcon.addClickHandler(e -> {
			EventTreeItem selectedItem = (EventTreeItem) this.tree.getSelectedItem();
			if (selectedItem instanceof EventViewRootTreeItem) {
				HashMap<String, Object> params = new HashMap<>();
				params.put("processList", this.contentProcess.getProcessList());
				params.put("host", this);
				params.put("Game","Roulette");
				
				this.getMaterialExtentsWindow().openDialog(EventApplication.SELECT_ROULETTE_TARGET, params, 450);
			}
		});
		
		OXQuizAddIcon = addIcon(IconType.HELP_OUTLINE, Float.LEFT, false);
		OXQuizAddIcon.setDisplay(Display.NONE);
		OXQuizAddIcon.setTooltip("OX퀴즈 추가");
		OXQuizAddIcon.addClickHandler(e -> {
			EventTreeItem selectedItem = (EventTreeItem) this.tree.getSelectedItem();
			if (selectedItem instanceof EventViewRootTreeItem) {
				HashMap<String, Object> params = new HashMap<>();
				params.put("processList", this.contentProcess.getProcessList());
				params.put("host", this);
				params.put("Game","OXQuiz");
				this.getMaterialExtentsWindow().openDialog(EventApplication.SELECT_ROULETTE_TARGET, params, 450);
			}
		});
		
		addIcon = addIcon(IconType.ADD, Float.LEFT, false);
		addIcon.setVisible(false);
		addIcon.addClickHandler(this.handleAddIconClick());

		removeIcon = addIcon(IconType.DELETE, Float.LEFT, false);
		removeIcon.setVisible(false);
		removeIcon.addClickHandler(this.handleRemoveIconClick());
		
		panelBottom.add(addIcon);
		panelBottom.add(removeIcon);
		panelBottom.add(textAddIcon);
		panelBottom.add(imageAddIcon);
		panelBottom.add(rouletteAddIcon);
		panelBottom.add(OXQuizAddIcon);
//		panelBottom.add(goTreePrevIcon);
//		panelBottom.add(goTreeNextIcon);
		//	END : Left Tree Bottom Panel Area
		
		//	BEGIN : Detail Slide Panel Area
		MaterialPanel detailPanel = new MaterialPanel();
		detailPanel.setLayoutPosition(Position.RELATIVE);
		detailPanel.setTop(20);
		detailPanel.setOverflow(Overflow.HIDDEN);
		detailPanel.setPadding(10);
		detailPanel.setBorder("1px solid #e0e0e0");
		detailPanel.setHeight("100%");
		col2.add(detailPanel);
		
		detailSliderPanel = new MaterialPanel();
		detailSliderPanel.setLayoutPosition(Position.ABSOLUTE);
		detailSliderPanel.setTop(0);
		detailSliderPanel.setLeft(0);
		detailSliderPanel.setWidth(((959 * 9) + 36 )  + "px");
		detailSliderPanel.setHeight("100%");
		detailPanel.add(detailSliderPanel);
		
		this.contentBase = new EventContentsBase(this, this.getMaterialExtentsWindow());
		this.contentProcess = new EventContentsProcess(this, this.getMaterialExtentsWindow());
		this.contentComponents = new EventContentsComponents(this, this.getMaterialExtentsWindow());
		this.contentPromotion = new EventContentsPromotion(this, this.getMaterialExtentsWindow());
		this.contentTags = new EventContentsTags(this, this.getMaterialExtentsWindow());
		this.contentImages = new EventContentsImages(this, this.getMaterialExtentsWindow());
		this.contentResultTemplate = new EventContentsResultTemplate(this, this.getMaterialExtentsWindow());
		this.contentResultComponents = new EventContentsResultComponents(this, this.getMaterialExtentsWindow());
		
		detailSliderPanel.add(this.contentBase);
		detailSliderPanel.add(this.contentProcess);
		detailSliderPanel.add(this.contentComponents);
		detailSliderPanel.add(this.contentPromotion);
		detailSliderPanel.add(this.contentTags);
		detailSliderPanel.add(this.contentImages);
		DetailPanelReload(0);
		/* 당첨자 선발 방식에 따라  링크 변경  by ymjang
		 * TODO
		 * 기본정보에서 당첨자 선발방식 수정시 detailSliderPanel reload 해야함
		 *  */
		if(this.evtId != null && checkfirst == false) {
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("GET_EVENT_BASE"));
			paramJson.put("evtId", new JSONString(this.evtId));
			paramJson.put("cotId", new JSONString(this.cotId));
			Console.log(evtId + "go");
			VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
				JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
				String process = resultObj.get("header").isObject().get("process").isString().stringValue();
				
				if (process.equals("success")) {
					JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isObject();
					if (bodyResultObj.containsKey("ANNOUNCE_TYPE")) {
						
						annouce_type = (int)bodyResultObj.get("ANNOUNCE_TYPE").isNumber().doubleValue();
						
						if(annouce_type == 0){
							DetailPanelReload(0);
						}else {
							DetailPanelReload(1);
						}
						
					} 
				}
			});
		}
		//	END : Detail Slide Panel Area
		this.add(row);
	}
	
	public void DetailPanelReload(int annouce_type) {
		
		if(annouce_type == 0) {
			detailSliderPanel.remove(contentResultTemplate);
			detailSliderPanel.add(this.contentResultComponents);
			
		}
		else if (annouce_type == 1) {
			detailSliderPanel.remove(contentResultComponents);
			detailSliderPanel.add(this.contentResultTemplate);
			
		}
			
		
	}
	
	public int getAnnouce_type() {
		return annouce_type;
	}
	
	public void setAnnouce_type(int annouce_type) {
		this.annouce_type = annouce_type;
	}
	
	
	//	Left panel tree build
	public MaterialWidget buildMainTree() {
		tree = new ContentTree();
		tree.setHeight("590px");
		tree.addSelectionHandler(e -> {
			EventTreeItem selectedItem = (EventTreeItem) e.getSelectedItem();

			this.visibleTreeHandleIcon(false, false);
			this.visibleComponentsIcon(false);
			
			if (selectedItem.getSelectHandler() != null) {
				SelectEvent event = new SelectEvent();
				event.setTarget(selectedItem);
				selectedItem.getSelectHandler().onSelect(event);
			}
			
			int slidingValue = selectedItem.getSlidingValue();
			
			if (slidingValue > -1) {
				int newSlidingValue = slidingValue * -1;
				goDetail(newSlidingValue);
			}
		});
		
		baseTreeItem = new EventBaseRootTreeItem(0, "기본 정보", IconType.INFO);
		processTreeItem = new EventProcessRootTreeItem(-1, "진행방식 설정", IconType.EVENT_NOTE);
		viewTreeItem = new EventViewRootTreeItem(1082 * 2, "이벤트 화면구성", IconType.BOOK);
		promotionTreeItem = new EventPromotionRootTreeItem(1082 * 3, "홍보배너", IconType.PHOTO_SIZE_SELECT_ACTUAL);
		tagTreeItem = new EventTagRootTreeItem(1082 * 4, "태그", IconType.CODE);
		imageTreeItem = new EventImageRootTreeItem(1082 * 5, "이미지 업로드", IconType.IMAGE);
		resultTreeItem = new EventResultRootTreeItem(1082 * 6, "당첨자발표", IconType.EQUALIZER);

		processTreeItem.addEventTreeSelectHandler(e -> {
			if (this.getEditPossSet().contains(this.eventStatus)) {
				this.visibleTreeHandleIcon(true, false);
			}
		});
		viewTreeItem.addEventTreeSelectHandler(e -> {
			if (this.getEditPossSet().contains(this.eventStatus)) {
				this.visibleComponentsIcon(true);
			}

			this.contentComponents.getComponentList().forEach(item -> {
				item.refresh();
			});
		});
		
		resultTreeItem.addEventTreeSelectHandler(e -> {
			if (this.getEditPossSet().contains(this.eventStatus)) {
				
				if(annouce_type == 0) {
				this.textAddIcon.setVisible(true);
				this.imageAddIcon.setVisible(true);
				} else {
					this.textAddIcon.setVisible(false);
					this.imageAddIcon.setVisible(false);
				}
			}
			
			this.contentResultComponents.getComponentList().forEach(item -> {
				item.refresh();
			});
			
		});

		tree.add(baseTreeItem);
		tree.add(processTreeItem);
		tree.add(viewTreeItem);
//		tree.add(promotionTreeItem);
		tree.add(tagTreeItem);
		tree.add(imageTreeItem);
		tree.add(resultTreeItem);
		
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setHeight("52px");
		panel.setDisplay(Display.FLEX);
		panel.setFlexJustifyContent(FlexJustifyContent.CENTER);
		panel.setFlexAlignItems(FlexAlignItems.CENTER);
		panel.setBorderTop("1px solid rgb(224, 224, 224)");
		
		this.submitBtn = new MaterialButton();
		this.submitBtn.setText("최종작성완료");
		this.submitBtn.setVisible(false);
		this.submitBtn.addClickHandler(e -> {
			this.getMaterialExtentsWindow().confirm("작성완료 확인", "최종 작성완료 시 자동으로 관리자로부터의 승인대기 상태가 되며, 승인대기상태 시 더 이상 수정이 불가능합니다.", 500, event -> {
				if (event.getRelativeElement().getId().equals("yes")) {
					JSONObject paramJson = new JSONObject();
					paramJson.put("cmd", new JSONString("UPDATE_EVENT_STATUS"));
					paramJson.put("evtId", new JSONString(this.evtId));
					paramJson.put("cotId", new JSONString(this.cotId));
					paramJson.put("status", new JSONNumber(EventStatus.APPROVAL_WAIT.getValue()));
					VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
						EventContentsList main = (EventContentsList) this.getMaterialExtentsWindow().getContentPanel(0);
						main.setEventClickcheck(true);
						ContentTableRow selectedRow = main.getTable().getSelectedRows().get(0);
						MaterialLabel columnLabel = (MaterialLabel) selectedRow.getColumnObject(1);
						columnLabel.setText("승인대기");
						
						this.setEventStatus(EventStatus.APPROVAL_WAIT);
						this.contentSaveData();
						this.contentLoadData();
					});
				}
			});
		});
		panel.add(this.submitBtn);

		this.approvalCancelBtn = new MaterialButton();
		this.approvalCancelBtn.setText("승인요청 취소");
		this.approvalCancelBtn.setVisible(false);
		this.approvalCancelBtn.addClickHandler(e -> {
			this.getMaterialExtentsWindow().confirm("승인요청 취소 확인", "승인요청을 취소할 시 다시 작성중 상태가 됩니다. 승인요청을 취소하시겠습니까?", 500, event -> {
				if (event.getRelativeElement().getId().equals("yes")) {
					JSONObject paramJson = new JSONObject();
					paramJson.put("cmd", new JSONString("UPDATE_EVENT_STATUS"));
					paramJson.put("evtId", new JSONString(this.evtId));
					paramJson.put("cotId", new JSONString(this.cotId));
					paramJson.put("status", new JSONNumber(EventStatus.WRITING.getValue()));
					VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
						EventContentsList main = (EventContentsList) this.getMaterialExtentsWindow().getContentPanel(0);
						main.setEventClickcheck(true);
						ContentTableRow selectedRow = main.getTable().getSelectedRows().get(0);
						MaterialLabel columnLabel = (MaterialLabel) selectedRow.getColumnObject(1);
						columnLabel.setText("작성중");
						
						this.setEventStatus(EventStatus.WRITING);
						this.contentLoadData();
					});
				}
			});
		});
		panel.add(this.approvalCancelBtn);
		
		this.panelTop.add(tree);
		this.panelTop.add(panel);
		return panel;
	}
	
	public void visibleProgressChangeBtn(boolean submitBtn, boolean approvalCancelBtn) {
		this.submitBtn.setVisible(submitBtn);
		this.approvalCancelBtn.setVisible(approvalCancelBtn);
	}
	
	public IEventComponent createComponent(EventComponentType componentType) {
		String evcId = IDUtil.uuid();
		
		EventTreeItem treeItem = EventComponentTreeFactory.getInstance(componentType);
		IEventComponent component = EventComponentFactory.getInstance(componentType, this,this.getMaterialExtentsWindow(),"basic");

		EventComponent componentObj = new EventComponent();
		componentObj.setEvcId(evcId);
		componentObj.setEvtId(this.getEvtId());
		componentObj.setCompType(componentType.getValue());
		componentObj.setCompIdx(this.contentComponents.getComponentList().size());
		
		AbstractEventComponent aComponent = (AbstractEventComponent) component;
		aComponent.setTreeItem(treeItem);
		aComponent.setComponentObj(componentObj);
		
		treeItem.addClickHandler(e -> {
			this.contentComponents.getComponentList().forEach(item -> {
				this.goDetail(this.viewTreeItem.getSlidingValue() * -1);
				item.refresh();
			});
		});
		
		this.contentComponents.addComponent(aComponent);
		this.contentComponents.renderComponent();
		
		return component;
	}
	
	public IEventComponent createResultComponent(EventComponentType componentType) {
		String ewcId = IDUtil.uuid();
		
		EventTreeItem treeItem = EventComponentTreeFactory.getInstance(componentType);
		IEventComponent component = EventComponentFactory.getInstance(componentType, this,this.getMaterialExtentsWindow(),"result");

		EventComponent componentObj = new EventComponent();
		componentObj.setEwcId(ewcId);
		componentObj.setEvtId(this.getEvtId());
		componentObj.setCompType(componentType.getValue());
		componentObj.setCompIdx(this.contentResultComponents.getComponentList().size());
		
		AbstractEventComponent aComponent = (AbstractEventComponent) component;
		aComponent.setTreeItem(treeItem);
		aComponent.setComponentObj(componentObj);
		
		treeItem.addClickHandler(e -> {
			this.contentResultComponents.getComponentList().forEach(item -> {
				this.goDetail(this.resultTreeItem.getSlidingValue() * -1);
				item.refresh();
			});
		});
		
		this.contentResultComponents.addComponent(aComponent);
		this.contentResultComponents.renderComponent();
		
		return component;
	}
	
	
	//	삭제 아이콘 이벤트 처리
	private ClickHandler handleRemoveIconClick() {
		return e -> {
			MaterialTreeItem selectedItem = this.tree.getSelectedItem();
			
			if (selectedItem instanceof EventProcessTreeItem) {
				EventProcessTreeItem tItem = (EventProcessTreeItem) selectedItem;
				EventProcess process = tItem.getProcessObj();
				process.setDelete(true);
				
			} else if (selectedItem instanceof EventGiftTreeItem) {
//				EventGiftTreeItem tItem = (EventGiftTreeItem) selectedItem;
//				EventGift gift = tItem.getGiftObj();
//				gift.setDelete(true);
			}
			selectedItem.removeFromParent();
		};
	}
	
	//	프로세스 추가 처리
	private EventProcessTreeItem addProcess(EventTreeItem targetItem, EventProcess process) {
		if (process == null) {
			process = new EventProcess();
			process.setEvtId(this.evtId);
			process.setSubEvtId(IDUtil.uuid());
			process.setEvtTypeCd(2);
			process.setTitle("이름없음");
			process.setStartDate(DateTimeFormat.getFormat("yyyy-MM-dd").format(new Date()));
			process.setEndDate(DateTimeFormat.getFormat("yyyy-MM-dd").format(new Date()));
			this.contentProcess.getProcessList().add(process);
		}
		
		EventProcessTreeItem tItem = new EventProcessTreeItem(process.getTitle(), IconType.ASSISTANT_PHOTO);
		tItem.setProcessObj(process);
		tItem.addEventTreeSelectHandler(e -> {
			if (this.getEditPossSet().contains(this.eventStatus)) {
				//2019-11-18 - 홍준호 추가버튼 제거
				this.visibleTreeHandleIcon(false, true);
			}
			if (this.contentProcess.getNotWinObj() != null)
				this.contentProcess.setNotWinObj(null);
			EventProcessTreeItem treeItem = (EventProcessTreeItem) e.getTarget();
			EventProcess eventProcess = treeItem.getProcessObj();
			this.contentProcess.setSelectedProcessTreeItem(treeItem);
			this.contentProcess.setupContentValue(eventProcess);
			
			goDetail(1082 * -1);
		});
		targetItem.add(tItem);
		return tItem;
	}

	//	상품 추가 처리
	private EventGiftTreeItem addGift(EventProcessTreeItem targetItem, EventGift gift) {
		EventProcess process = targetItem.getProcessObj();
		
		List<EventGift> giftList = process.getGiftList();
		if (gift == null) {
			if (giftList.size() == 8) {
				this.getMaterialExtentsWindow().alert("상품은 최대 8개까지만 등록가능합니다.");
				return null;
			}
			gift = new EventGift();
			gift.setGftId(IDUtil.uuid());
			gift.setEvtId(this.evtId);
			gift.setSubEvtId(process.getSubEvtId());
			gift.setNotWin(false);
			gift.setOrder(giftList.size());
			giftList.add(gift);
		}

		EventGiftTreeItem tItem = new EventGiftTreeItem(gift.isNotWin() ? "꽝" : "경품 " + (gift.getOrder() + 1), IconType.DONE);
		tItem.setGiftObj(gift);
		tItem.addEventTreeSelectHandler(e -> {
			if (this.getEditPossSet().contains(this.eventStatus)) {
				this.visibleTreeHandleIcon(false, true);
			}
			EventGiftTreeItem treeItem = (EventGiftTreeItem) e.getTarget();
			EventProcessTreeItem parent = (EventProcessTreeItem) treeItem.getParent();
			EventProcess eventProcess = parent.getProcessObj();
			this.contentProcess.setSelectedProcessTreeItem(parent);
			this.contentProcess.setupContentValue(eventProcess);
			
			goDetail(1082 * -1);
		});
		targetItem.add(tItem);
		this.contentProcess.addGiftContentPanel(gift);
		return tItem;
	}
	
	//	추가 아이콘 이벤트 처리
	private ClickHandler handleAddIconClick() {
		return e -> {
			EventTreeItem selectedItem = (EventTreeItem) this.tree.getSelectedItem();

			if (selectedItem instanceof EventProcessRootTreeItem) {
				this.addProcess(selectedItem, null);
			} else if (selectedItem instanceof EventProcessTreeItem) {
//				this.addGift((EventProcessTreeItem) selectedItem, null);
			}
		};
	}

	//	컴포넌트 아이콘 설정
	public void visibleComponentsIcon(boolean isVisible) {
		this.textAddIcon.setVisible(isVisible);
		this.imageAddIcon.setVisible(isVisible);
		this.rouletteAddIcon.setVisible(isVisible);
		this.OXQuizAddIcon.setVisible(isVisible);
	}

	//	트리 하단 아이콘 설정
	public void visibleTreeHandleIcon(boolean addIcon, boolean removeIcon) {
		this.addIcon.setVisible(addIcon);
		this.removeIcon.setVisible(removeIcon);
	}

//	public void goTree(int position) {
//		int newTreePosition = (position-1) * -332;
//		TransitionConfig cfg = new TransitionConfig();
//		cfg.setProperty("left");
//		cfg.setDuration(100);
//		topSlider.setTransition(cfg);
//		topSlider.setTransform("translate("+newTreePosition+"px,0);");
//		topSlider.setLeft(newTreePosition);
//	}

	public void goDetail(int position) {
		this.nowPosition = position;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		detailSliderPanel.setTransition(cfg);
		detailSliderPanel.setTransform("translate(" + nowPosition + "px,0);");
		detailSliderPanel.setLeft(this.nowPosition);
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.titleLabel.setText(contentTitle);
	}

	public ContentTree getTree() {
		return tree;
	}

	public String getEvtId() {
		return evtId;
	}
	
	public void setEvtParamInitialize(String cotId, String evtId ) {
		this.cotId = cotId;
		this.evtId = evtId; 
	}

	private MaterialIcon addIcon(IconType iconType, Float floatAlign, boolean isBorderLeft) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setLineHeight(26);
		icon.setVerticalAlign(VerticalAlign.MIDDLE);
		icon.setFontSize("1.0em");
		icon.setHeight("26px");
		icon.setMargin(0);
		icon.setWidth("26px");
		icon.setFloat(floatAlign);
		if (isBorderLeft) 
			icon.setBorderLeft("1px solid #e0e0e0");
		else
			icon.setBorderRight("1px solid #e0e0e0");
		return icon;
	}

	public EventContentsTags getContentTags() {
		return contentTags;
	}

	public EventContentsImages getContentImages() {
		return contentImages;
	}

	public MaterialIcon getSaveIcon() {
		return saveIcon;
	}

	public EventContentsComponents getContentComponents() {
		return contentComponents;
	}

	public EventContentsProcess getContentProcess() {
		return contentProcess;
	}

	public EventTreeItem getViewTreeItem() {
		return viewTreeItem;
	}

	public EventTreeItem getResultTreeItem() {
		return resultTreeItem;
	}
	
}
