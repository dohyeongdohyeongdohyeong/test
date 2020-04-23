package kr.or.visitkorea.admin.client.manager.imageManager.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.WindowParamter;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.imageManager.ImageManagerApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


/**
 * @author Admin
 * 이미지 관리 Main
 */
public class ImageManagerMain extends AbstractContentPanel implements ImageManagerApplication.OnSearchListener {

	private ImageManagerApplication appview;
	private MaterialRow row;
	private MaterialColumn col1, col2;
	private ContentTable mainTable;
	private MaterialPanel iconPanel;
	private MaterialTree tree;
	private MaterialTreeItem selectedItem;
	private MaterialIcon collapseIcon;
	private MaterialLabel countLabel;
	private Map<String, Object> treeItem = new HashMap<String, Object>();

	public ImageManagerMain(MaterialExtentsWindow materialExtentsWindow, ImageManagerApplication ia) {
		super(materialExtentsWindow);
		appview = ia;
	}

	@Override
	public void init() {
		Registry.put("ImageManagerMain", this);
		setLayoutPosition(Position.RELATIVE);

		buildLayout();
		directoryView();
		detailDirectoryView();
		roadXml();

	}

	private void buildLayout() {
		row = new MaterialRow();
		row.setHeight("100%");

		col1 = new MaterialColumn();
		col1.setLayoutPosition(Position.RELATIVE);
		col1.setHeight("549px");
		col1.setWidth("300px");
		col1.setLeft(35);
		col1.setTop(55);

		col2 = new MaterialColumn();

		row.add(col1);
		row.add(col2);

		this.add(row);
	}

	// 좌측 폴더 경로
	public void directoryView() {

		tree = new MaterialTree();
		tree.setLayoutPosition(Position.RELATIVE);
		tree.setOverflow(Overflow.AUTO);
		tree.setHeight("100%");
		tree.setBorder("1px solid #e0e0e0");
		tree.setBackgroundColor(Color.WHITE);
		tree.setTextAlign(TextAlign.LEFT);
		tree.setFontSize("1.0em");

		tree.addOpenHandler(event->{
			selectedItem = (MaterialTreeItem) event.getTarget();
			selectedItem.setIconType(IconType.FOLDER_OPEN);
		});

		tree.addCloseHandler(event->{
			selectedItem = (MaterialTreeItem) event.getTarget();
			selectedItem.setIconType(IconType.FOLDER);
			for(String key : treeItem.keySet()){
				if (key.substring(0, selectedItem.getId().length()).equals(selectedItem.getId())) {
		            MaterialTreeItem t = (MaterialTreeItem) treeItem.get(key);
		            t.setIconType(IconType.FOLDER);
		            t.collapse();
				}
	        }
		});

		tree.addSelectionHandler(event->{
			selectedItem = (MaterialTreeItem) event.getSelectedItem();
			// 폴더 클릭시 우측 상세 리스트 초기화
			selectList(selectedItem.getId());
		});
		
		iconPanel = new MaterialPanel();
		iconPanel.setHeight("26px");
		iconPanel.setBorder("1px solid #e0e0e0");
		iconPanel.setBackgroundColor(Color.WHITE);
		iconPanel.setPadding(0);
		iconPanel.setLayoutPosition(Position.RELATIVE);

		collapseIcon = new MaterialIcon(IconType.VERTICAL_ALIGN_TOP);
		collapseIcon.setLineHeight(26);
		collapseIcon.setFontSize("1.0em");
		collapseIcon.setBorderRight("1px solid #e0e0e0");
		collapseIcon.setWidth("26px");
		collapseIcon.setHeight("26px");
		collapseIcon.setMargin(0);
		collapseIcon.setTooltip("경로 전체 접기");
		collapseIcon.setFloat(Style.Float.LEFT);

		collapseIcon.addClickHandler(event->{
			
			for(String key : treeItem.keySet()){
	            MaterialTreeItem t = (MaterialTreeItem) treeItem.get(key);
	            t.setIconType(IconType.FOLDER);
	        }
			
			tree.collapse();
			mainTable.clearRows();
		});

		iconPanel.add(collapseIcon);

		col1.add(tree);
		col1.add(iconPanel);
	}

	// 우측 폴더 상세 리스트
	public void detailDirectoryView() {
		mainTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		mainTable.setLayoutPosition(Position.ABSOLUTE);
		mainTable.setWidth("1100px");
		mainTable.setHeight(575);
		mainTable.setRight(35);
		mainTable.setTop(55);
		mainTable.appendTitle("파일명", 300, TextAlign.LEFT);
		mainTable.appendTitle("수정한 날짜", 200, TextAlign.CENTER);
		mainTable.appendTitle("유형", 80, TextAlign.LEFT);
		mainTable.appendTitle("크기", 80, TextAlign.RIGHT);
		mainTable.appendTitle("경로", 300, TextAlign.LEFT);
		mainTable.appendTitle("분류", 80, TextAlign.CENTER);

//		MaterialIcon deleteIcon = new MaterialIcon(IconType.DELETE);
//		deleteIcon.setTextAlign(TextAlign.CENTER);
//		deleteIcon.addClickHandler(event-> {
//
//			getMaterialExtentsWindow().confirm("정말 삭제 하겠습니까?", 500, e2-> {
//				if(((MaterialButton)e2.getSource()).getId().equals("yes")) {
//
//				}
//			});
//		});
//		mainTable.getTopMenu().addIcon(deleteIcon, "삭제", Style.Float.RIGHT, "1.8em", "26px", 24, false);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		mainTable.getButtomMenu().addLabel(countLabel, Style.Float.RIGHT);
		
		col2.add(mainTable);
	}

	// xml read
	private void roadXml() {

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_FILE_REPOSITORY_TEMPLATE"));

		// json으로 변환
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();

				if (processResult.equals("success")) {

					JSONObject result = resultObj.get("body").isObject().get("result").isObject();
					JSONObject rootNode = result.get("data").isObject().get("images").isObject();
					JSONArray mainNodes = rootNode.get("dir").isArray();

					int sizeMainNodes = mainNodes.size();
					for (int i=0; i<sizeMainNodes; i++) {
						JSONObject jObj = mainNodes.get(i).isObject();
						buildList(tree, jObj);
					}

					tree.collapse();
				} else {
					getMaterialExtentsWindow().alert("오류가 발생하였습니다.", 400);
				}
			}
		});


	}

	private void buildList(MaterialWidget widget, JSONObject jObj) {

		MaterialTreeItem mainNode = new MaterialTreeItem(jObj.get("name").toString().replace("\"", ""), IconType.FOLDER);
		String path = jObj.get("path").isString().stringValue();
		mainNode.setId(path);
		treeItem.put(path, mainNode);
		buildRepository(mainNode, jObj);

		widget.add(mainNode);
	}

	private void buildRepository(MaterialTreeItem mainNode, JSONObject jObj) {

		if (jObj.get("dir") != null) {
			JSONValue dir = jObj.get("dir");
			if (dir instanceof JSONObject) {
				JSONObject dirObj = dir.isObject();
				if (dirObj != null) {
					MaterialTreeItem dirNode = new MaterialTreeItem(dirObj.get("name").toString().replace("\"", ""), IconType.FOLDER);
					if (dirNode.isAttached()) {
						mainNode.add(dirNode);
					}
					buildList(mainNode, dirObj);
				}
			} else {
				JSONArray dirArr = dir.isArray();
				if (dirArr != null) {
					for (int i=0; i<dirArr.size(); i++) {
						JSONObject dirObj = dirArr.get(i).isObject();
						buildList(mainNode, dirObj);
					}
				}
			}
		}
	}

	private void selectList(String path) {

		mainTable.loading(true);
		mainTable.clearRows();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_CHILD_REPOSITORY"));
		parameterJSON.put("path", new JSONString(path));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				
				if (processResult.equals("success")) {

					JSONObject result = resultObj.get("body").isObject().get("result").isObject();
					int totcnt = 0;
					JSONArray dir = result.get("dir").isArray();

					if (dir != null && dir.size() > 0) {
						
						totcnt = dir.size();
						
						for (int i=0; i<dir.size(); i++) {

							JSONObject dirObj = dir.get(i).isObject();
							String dPath = dirObj.get("path").isString().stringValue();
							ContentTableRow tableRow = mainTable.addRow(Color.WHITE,
									dirObj.get("name").isString().stringValue(),
									"",
									"",
									"",
									dPath,
									"폴더");
							
							tableRow.addDoubleClickHandler(event->{
						        MaterialTreeItem t = (MaterialTreeItem) treeItem.get(dPath);
						        t.select();
							});
						}
					}

					JSONArray file = result.get("file").isArray();

					if (file != null && file.size() > 0) {
						
						totcnt += file.size();
						
						for (int i=0; i<file.size(); i++) {
							
							JSONObject fileObj = file.get(i).isObject();
							ContentTableRow tableRow = mainTable.addRow(Color.WHITE,
									fileObj.get("fileNm").isString().stringValue(),
									fileObj.get("lastModiDt").isString().stringValue(),
									fileObj.get("ext").isString().stringValue(),
									getSize(fileObj.get("size").isString().stringValue()),
									fileObj.get("folderPath").isString().stringValue(),
									fileObj.get("link").isString().stringValue().equals("true")?"링크":"원본");
							
							tableRow.addDoubleClickHandler(event->{
								
								Map<String, Object> param = new HashMap<String, Object>();
								
								param.put("abPath",fileObj.get("abPath").isString().stringValue());
								param.put("folderPath",fileObj.get("folderPath").isString().stringValue());
								param.put("fileNm",fileObj.get("fileNm").isString().stringValue());
								param.put("ext",fileObj.get("ext").isString().stringValue());
								param.put("md5Cs",fileObj.get("md5Cs").isString().stringValue());
								param.put("size",getSize(fileObj.get("size").isString().stringValue()));
								param.put("width",fileObj.get("width").isString().stringValue());
								param.put("height",fileObj.get("height").isString().stringValue());
								param.put("createDt",fileObj.get("createDt").isString().stringValue());
								param.put("lastModiDt",fileObj.get("lastModiDt").isString().stringValue());
								param.put("link",fileObj.get("link").isString().stringValue().equals("true")?"링크":"원본");
								param.put("desc", fileObj.get("desc").isString().stringValue());
								
								openPreview(param);
							});
						}
					}
					
					countLabel.setText(totcnt + " 건");
				} else {
					getMaterialExtentsWindow().alert("오류가 발생하였습니다.", 400);
				}
			}
		});

		mainTable.loading(false);
	}
	
	private void openPreview(Map<String, Object> param) {
		
		MaterialLink tgrLink = new MaterialLink("상세 정보");
    	tgrLink.setIconType(IconType.CARD_MEMBERSHIP);
    	tgrLink.setWaves(WavesType.DEFAULT);
    	tgrLink.setIconPosition(IconPosition.LEFT);
    	tgrLink.setTextColor(Color.BLUE);
		WindowParamter winParam = new WindowParamter(tgrLink, ApplicationView.WINDOW_KEY_IMAGE_PREVIEW, "이미지", 700, 750);
		winParam.setParams(param);
		Registry.put("TARGET_LINK", winParam);
		this.appview.getAppView().openTargetWindow(param);
	}
	
	public String getSize(String sizeStr) {
		Long size = Long.parseLong(sizeStr);
        String s = "";
        double kb = size / 1024.0;
        double mb = kb / 1024.0;
        double gb = mb / 1024.0;
        
        if(mb < 1) {
        	s = Math.round(kb*100)/100.0 + " KB";
        } else if(mb >= 1 && gb < 1) {
        	s = Math.round(mb*100)/100.0 + " MB";
        } else if(gb >= 1) {
        	s = Math.round(gb*100)/100.0 + " GB";
        }
        return s;
    }

	@Override
	public void onSearch(String startDate, String endDate) {
		
	}
}