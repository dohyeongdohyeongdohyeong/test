
package kr.or.visitkorea.admin.client.manager.dialogs;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.fileuploader.MaterialUploadLabel;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectContentDialog extends DialogContent {


	private MaterialLabel messageLabel;
	private MaterialPanel bodyScrollPanel;
	private MaterialPanel scrollMain;
	private MaterialPanel body01;
	private MaterialTextBox textBox01;
	private MaterialTextBox textBox02;
	private MaterialLabel prev;

	public SelectContentDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		
		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("컨텐츠 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		this.add(dialogTitle);
		
		// dialog title define
		prev = new MaterialLabel("목록으로");
		prev.setLayoutPosition(Position.ABSOLUTE);
		prev.setFontSize("1.2em");
//		prev.setFontWeight(FontWeight.BOLD);
		prev.setTextColor(Color.BLUE);
		prev.setTop(15);
		prev.setLeft(610);
		
		prev.addClickHandler(event->{
			go(0);
		});
//		prev.setVisibility(Visibility.HIDDEN);
		
		this.add(prev);

		messageLabel = new MaterialLabel("");
		messageLabel.setLayoutPosition(Position.ABSOLUTE);
		messageLabel.setLeft(30);
		messageLabel.setFontSize("1.2em");
		messageLabel.setTop(getHeight() - 30 - 25);
		this.add(messageLabel);
		this.addDefaultButtons();
		
		MaterialButton saveButton = new MaterialButton("선택");
		saveButton.setLayoutPosition(Position.ABSOLUTE);
		saveButton.setLeft(500);
		saveButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		saveButton.setTop(getHeight() - 30 - 30);
		saveButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		this.add(saveButton);

		int bspWidth = (760*3)+50;
		
		scrollMain = new MaterialPanel();
		scrollMain.setOverflow(Overflow.HIDDEN);
		scrollMain.setWidth("720px");
		scrollMain.setHeight("400px");
		
		bodyScrollPanel = new MaterialPanel();
		bodyScrollPanel.setLayoutPosition(Position.RELATIVE);
		bodyScrollPanel.setTop(0);
		bodyScrollPanel.setLeft(0);
		bodyScrollPanel.setWidth(bspWidth+"px");
		bodyScrollPanel.setHeight("400px");
		bodyScrollPanel.add(getSearchArea());
		bodyScrollPanel.add(getTargetSelectionArea());
		
		scrollMain.add(bodyScrollPanel);
	
		this.add(scrollMain);
	}

	public void go(int position) {
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(250);
		bodyScrollPanel.setTransition(cfg);
		bodyScrollPanel.setTransform("translate("+position+"px,0);");
		bodyScrollPanel.setLeft(position);
		
	}

	public void setPrevButton(Transition transition) {
		
		MaterialAnimation animation = new MaterialAnimation();
		animation.setDelay(0);
		animation.setDuration(500);
		animation.setTransition(transition);
		animation.animate(prev);

	}
	
	private MaterialPanel getTargetSelectionArea() {
		
		MaterialPanel dataTable =new MaterialPanel();
		
		dataTable.setMargin(20);
		dataTable.setPadding(0);
		dataTable.setWidth("680px");
		dataTable.setHeight("360px");
		dataTable.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialCollection collection = new MaterialCollection();
		textBox01 = buildTextInputForm(collection, "컨텐츠 이름");
		textBox02 = buildTextInputForm(collection, "이미지 설명");
		
		collection.setHeight("196px");
		collection.setPaddingLeft(250);
		collection.setPaddingRight(35);
		dataTable.add(collection);
		
		MaterialFileUploader fileUploader = new MaterialFileUploader();
		fileUploader.setLayoutPosition(Position.RELATIVE);
		fileUploader.setUrl("/pudas");
		fileUploader.setMaxFiles(100);
		fileUploader.setShadow(0);
		fileUploader.setMaxFileSize(20);
		fileUploader.setLeft(40);
		fileUploader.setTop(-210);
		fileUploader.setWidth("210px");
		fileUploader.setHeight("193px");
		MaterialUploadLabel upLabel = new MaterialUploadLabel();
		upLabel.setTitle("컨텐츠 사진");
		upLabel.setDescription("이미지 사이즈(111x111)");
		
		fileUploader.add(upLabel);
		fileUploader.setEnabled(true);

		dataTable.add(fileUploader);

		return dataTable;
	}

	public MaterialPanel getSearchArea() {
		
		MaterialPanel dataTable =new MaterialPanel();
		dataTable.setMargin(20);
		dataTable.setPadding(0);
		dataTable.setHeight("360px");
		dataTable.setWidth("680px");
		dataTable.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialPanel header = new MaterialPanel();
		header.setWidth("680px");
		header.setHeight("48px");
		
		String dropDownUniqueId = Document.get().createUniqueId();
		
		MaterialButton dropDownCaller = new MaterialButton();
		dropDownCaller.setMarginTop(10);
		dropDownCaller.setText("컨텐츠 이름");
		dropDownCaller.setIconType(IconType.ARROW_DROP_DOWN);
		dropDownCaller.setIconPosition(IconPosition.RIGHT);
		dropDownCaller.setTextColor(Color.WHITE);
		dropDownCaller.setActivates(dropDownUniqueId);
		dropDownCaller.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialDropDown mdd = new MaterialDropDown();
		mdd.setActivator(dropDownUniqueId);
		mdd.setConstrainWidth(true);
		mdd.add(new MaterialLink("컨턴츠 이름"));
		mdd.add(new MaterialLink("CID"));
		
		this.add(mdd);
		
		MaterialInput child2 = new MaterialInput(InputType.TEXT);
		child2.setLineHeight(42);
		child2.setWidth("480px");
		child2.setMarginLeft(20);
		child2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		child2.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13) {
			}
		});
		
		header.add(dropDownCaller);
		header.add(child2);
		

		body01 = new MaterialPanel();
		body01.setWidth("680px");
		body01.setHeight("258px");
		body01.setOverflow(Overflow.AUTO);
		body01.setBorder("1px solid #BFBFBF");
		
		MaterialPanel title = new MaterialPanel();
		title.setWidth("680px");
		title.setHeight("42px");
		
		MaterialPanel rows = new MaterialPanel();
		rows.setHeight("40px");
	
		MaterialLabel header1 = new MaterialLabel("CID");
		header1.setTextAlign(TextAlign.CENTER);
		header1.setWidth("120px");
		header1.setFontWeight(FontWeight.BOLD);
		header1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		rows.add(header1);

		MaterialLabel header2 = new MaterialLabel("지역");
		MaterialLabel header3 = new MaterialLabel("컨텐츠 이름");
		MaterialLabel header4 = new MaterialLabel("대표 태그");
		MaterialLabel  header5 = new MaterialLabel("미리 보기");
		
		header2.setTextAlign(TextAlign.CENTER);
		header3.setTextAlign(TextAlign.CENTER);
		header4.setTextAlign(TextAlign.CENTER);
		header5.setTextAlign(TextAlign.CENTER);

		header2.setFontWeight(FontWeight.BOLD);
		header3.setFontWeight(FontWeight.BOLD);
		header4.setFontWeight(FontWeight.BOLD);
		header5.setFontWeight(FontWeight.BOLD);

		header2.setWidth("120px");
		header3.setWidth("280px");
		header4.setWidth("80px");
		header5.setWidth("80px");
		
		header2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header4.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header5.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);

		rows.add(header2);
		rows.add(header3);
		rows.add(header4);
		rows.add(header5);
		
		title.add(rows);
		
		dataTable.add(header);
		dataTable.add(title);
		dataTable.add(body01);

		body01.addMouseOutHandler(event->{
			getElement().getStyle().setCursor(Style.Cursor.POINTER);
		});
		
		return dataTable;
	}
	
	private void addRows(MaterialPanel body) {
		for (int i=0; i<10; i++) {
			
			MaterialPanel rows = new MaterialPanel();
			rows.setHeight("40px");
			rows.setBorderBottom("1px solid #9F9F9F");
			Color bgColor = Color.WHITE;
			
			if (i%2 == 0) {
				bgColor = Color.WHITE;
			}else {
				bgColor = Color.GREY_LIGHTEN_4;
			}
			
			String[] record = new String[] {
					"192020201",
					"서울특별시f11111111",
					i+" :: 어린이 박물관 전경",
					"#바다",
					"미리보기"
			};
			
			addLabelObject(rows, TextAlign.CENTER,  record[0], "18%", bgColor);
			addLabelObject(rows, TextAlign.CENTER,  record[1], "18%", bgColor);
			addLabelObject(rows, TextAlign.LEFT,  record[2],"40%", bgColor);
			addLabelObject(rows, TextAlign.CENTER,  record[3], "12%", bgColor);
			addLinkObject(rows, TextAlign.CENTER,  record[4], "12%", bgColor);
			
			rows.addMouseOverHandler(event->{
				textBox01.setText(record[2]);
			});
			
			body.add(rows);

		}
	}
	
	private MaterialTextBox buildTextInputForm(MaterialCollection collection, String inputTitle) {
		MaterialCollectionItem item = new MaterialCollectionItem();
		MaterialTextBox component = new MaterialTextBox();
		component.setPlaceholder("값을 입력해 주세요.");
		component.setLabel(inputTitle);
		component.setActive(true);
		item.add(component);
		collection.add(item);
		return component;
	}

	private void addLinkObject(MaterialPanel rows, TextAlign align, String title, String width, Color bgColor) {
		
		MaterialLink header1 = new MaterialLink(title);
		header1.setStyle("overflow-x: hidden; text-overflow: ellipsis; white-space: nowrap;");
		header1.setDisplay(Display.INLINE_BLOCK);
		header1.setLineHeight(40);
		header1.setTextAlign(align);
		header1.setWidth(width);
		header1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header1.setBackgroundColor(bgColor);
		rows.add(header1);
		
	}

	private void addLabelObject(MaterialPanel rows, TextAlign align, String title, String width, Color bgColor) {
		
		MaterialLabel header1 = new MaterialLabel(title);
		header1.setStyle("overflow-x: hidden; text-overflow: ellipsis; white-space: nowrap;");
		header1.setDisplay(Display.INLINE_BLOCK);
		header1.setLineHeight(40);
		header1.setTextAlign(align);
		header1.setWidth(width);
		header1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header1.setBackgroundColor(bgColor);
		header1.addMouseUpHandler(event->{
			go(-720);
		});

		rows.add(header1);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		messageLabel.setText("");
		addRows(body01);
		go(0);
	}

	@Override
	public int getHeight() {
		return 500;
	}

}
