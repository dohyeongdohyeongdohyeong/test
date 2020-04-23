package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class ContentImageListPanel extends MaterialPanel {

	private List<ContentImageWidget> container;
	private List<ItemInformation> list;
	private MaterialPanel contentBody;
	private MaterialPanel menuArea;
	private MaterialPanel contentCover;
	private int maxWidth = 360;
	private int maxHeight = 250;
	
	private Map<String, Object> valueMap;
	private ContentDetailInputImage contentDetailInputImage;
	private String cotId;
	private Double contentOrder;
	private Double articleOrder;
	private Double articleSubOrder;
	private Double articleType;
	private Map<String, Map<String, Object>> idMap;
	
	public ContentImageListPanel(List<ItemInformation> itemInformationList, Map<String, Object> valueMap,ContentDetailInputImage contentDetailInputImage) {
		super();
		this.list = itemInformationList;
		this.valueMap = valueMap;
		this.cotId = (String) this.valueMap.get("COT_ID");
		this.contentOrder = (Double) this.valueMap.get("CONTENT_ORDER");
		this.articleOrder = (Double) this.valueMap.get("ARTICLE_ORDER");
		this.contentDetailInputImage = contentDetailInputImage;
		if (this.valueMap.get("ARTICLE_TYPE") instanceof Double) {
			this.articleType = (Double) this.valueMap.get("ARTICLE_TYPE");
		}else if (this.valueMap.get("ARTICLE_TYPE") instanceof Double) {
			this.articleType = Double.valueOf(this.valueMap.get("ARTICLE_TYPE") + "");
		}
		
		this.idMap = ( Map<String, Map<String, Object>> ) this.valueMap.get("ID_MAP");
		renderUiFormat();
	}

	public ContentImageListPanel() {
		super();
		Console.log("===============");
		renderUiFormat();
	}
	
	public List<ItemInformation> getItems() {
		return this.list;
	}

	public void setItems(List<ItemInformation> itemList) {
		this.list = itemList;
	}
	
	public void renderUiFormat() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		
		this.menuArea = new MaterialPanel();
		this.menuArea.setHeight("20px");
		this.add(this.menuArea);

		this.contentCover = new MaterialPanel();
		this.contentCover.setHeight("430px");
		this.contentCover.setWidth("810px");
		this.contentCover.getElement().getStyle().setOverflowY(Overflow.HIDDEN);
		this.contentCover.getElement().getStyle().setOverflowX(Overflow.AUTO);
		this.add(this.contentCover);
		
		this.contentBody = new MaterialPanel();
		this.contentBody.setHeight("300px");
		this.contentCover.add(this.contentBody);
		
		MaterialLink addLink = new MaterialLink();
		addLink.setIconType(IconType.ADD);
		addLink.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		addLink.setMarginRight(0);;
		addLink.addClickHandler(event->{
			
			if(list.size() == 3) {
				MaterialToast.fireToast("컴포넌트 내 이미지는 최대 3장까지만 추가할 수 있습니다");
				return;
			}
			//img id;
			String imgId = IDUtil.uuid().toString();			
			
			// additional image item
			
			String aciId = IDUtil.uuid().toString();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("ARTICLE_INSERT_IMAGE"));
			parameterJSON.put("imgId", new JSONString(imgId));
			parameterJSON.put("aciId", new JSONString(aciId));
			parameterJSON.put("cotId", new JSONString(this.cotId));
			parameterJSON.put("contentOrder", new JSONNumber(this.contentOrder));
			parameterJSON.put("articleOrder", new JSONNumber(this.articleOrder));
			parameterJSON.put("articleSubOrder", new JSONNumber(this.list.size()));
			parameterJSON.put("articleType", new  JSONNumber(2));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject)param1)).isObject();
					JSONObject headerObj = resultObj.get("header").isObject();
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						
						String imageUrl = Registry.getDefaultImagePath();
						
						contentBody.setWidth(((list.size() * maxWidth)) + "px");
						ImageInformation imageInformation = new ImageInformation(imageUrl,"", imgId,"",-1);
						imageInformation.setAciId(aciId);
						imageInformation.setCaption("");
						imageInformation.setisCaption(0);
						imageInformation.setAciId(aciId);
						HashMap<String,Object> idmap = new HashMap<String, Object>();
						
						idmap.put("ARTICLE_SUB_ORDER",list.size());
						idmap.put("IMAGE_DESCRIPTION","");
						idmap.put("IMAGE_CAPTION","");
						idmap.put("IS_CAPTION",0);
						idMap.put(imgId, idmap);
						list.add(imageInformation);
						Console.log("idMap :: " +idMap);
						renderItems();
						
					}
				}
			});

		});

		this.menuArea.add(addLink);

		renderItems();
	}

	public void removeItem(ContentImageWidget contentImageWidget) {
		this.list.remove(contentImageWidget);
		this.contentBody.remove(contentImageWidget); 
		renderItems();
	}
	
	public void renderItems() {
		
		this.contentBody.clear();
		this.contentBody.setWidth((list.size() * this.maxWidth) + "px");
		int itemIndex = 0;
		for (ItemInformation  ii : list) {
			
			ImageInformation ii2 = (ImageInformation)ii;
			String id = ii2.getUrl().substring(
					ii2.getUrl().indexOf("id=")+3, 
					ii2.getUrl().indexOf("&chk")
					);

			String descString = "";
			descString = ii2.getComment();
/*			
			if(this.idMap != null && this.idMap.get(id) != null) {
				Object descObject = this.idMap.get(id).get("IMAGE_DESCRIPTION");
				if (descObject != null)  descString = (String)descObject;
			}
*/			

			ContentImageWidget ciw = new ContentImageWidget(list, ii, this, itemIndex, descString, idMap);
			ciw.setLayoutPosition(Position.RELATIVE);
			ciw.setDisplay(Display.INLINE_BLOCK);
			ciw.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			this.contentBody.add(ciw);
			itemIndex++;
		}
	}
	
	public ContentDetailInputImage getcontentDetailInputImage() {
		return contentDetailInputImage;
	}
}
