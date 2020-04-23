package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialPanel;

public class TagListRow extends MaterialPanel implements VisitKoreaListRow {

	private String uniqueName;
	private double count;
	private int isAutoCount;
	private int isAuto;
	private List<VisitKoreaListCell> cells;
	private Map<String, Object> internalMap = new HashMap<String, Object>();
	
	public TagListRow() {
		init();
	}
	
	public TagListRow(List<VisitKoreaListCell> cells) {
		this.cells = cells;
		init();
	}

	private void init() {
		
		this.setLeft(0);
		this.setRight(0);
		this.setWidth("100%");
		this.setHeight("50");
		this.setHeight("50px");
		this.setMargin(0);
		this.setPadding(0);
		this.setBorderBottom("1px solid #c8c8c8");
		
		renderChild();
	}

	public void renderChild() {
		
		for (int i=0; i<cells.size(); i++) {
			
			VisitKoreaListCell materialWidet = (VisitKoreaListCell)cells.get(i);

			if (materialWidet.isDivBorder()) {
				if(i != cells.size()-1)
				((MaterialWidget)materialWidet).setBorderRight("1px solid #c8c8c8");
			}
			
			MaterialWidget mWidget = (MaterialWidget)materialWidet;
			
			mWidget.addMouseOverHandler(event->{
				mWidget.setTooltip(materialWidet.getValue());
			});
			
			mWidget.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
			mWidget.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
			mWidget.getElement().getStyle().setOverflow(Overflow.HIDDEN);

			this.add((MaterialWidget)materialWidet);
			
		}
	}

	public String getUniqueName() {
		return uniqueName;
	}
	
	public List<VisitKoreaListCell> getCells(){
		return this.cells; 
	}
	
	public VisitKoreaListCell getCell(int index){
		return this.cells.get(index); 
	}
	
	public void addCell(VisitKoreaListCell cell){
		this.cells.add(cell);
	}
	
	public void setTagName(String tagName) {
		this.uniqueName = tagName;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}
	
	public void setIsAutoCount(int isAutoCount) {
		this.isAutoCount = isAutoCount;
	}
	
	public int getIsAutoCount() {
		return this.isAutoCount;
	}
	
	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}
	
	public int getIsAuto() {
		return this.isAuto;
	}

	@Override
	public String toString() {
		return "TagListRow [uniqueName=" + uniqueName + ", count=" + count + ", isAutoCount=" + isAutoCount + ", isAuto=" + isAuto + ", childCount=" + this.internalMap.size() + "]";
	}
	
	public void put(String key, Object value) {
		this.internalMap.put(key, value);
	}
	
	public Object get(String key) {
		return this.internalMap.get(key);
	}
	
	public JSONObject getJSONObject() {
		
		JSONObject recordObject = new JSONObject();
		JSONObject cellsObject = new JSONObject();
		JSONObject detailObject = new JSONObject();
		JSONObject baseObject = new JSONObject();
		
		recordObject.put("cells", cellsObject);
		recordObject.put("base", baseObject);
		recordObject.put("detail", detailObject);
		
		for (int i=0; i < cells.size(); i++) {
			
			VisitKoreaListCell materialWidget = (VisitKoreaListCell)cells.get(i);
			cellsObject.put("idx_" + i, new JSONString(materialWidget.getValue()));
		}
		
		baseObject.put("uniqueName", new JSONString(uniqueName));
		baseObject.put("isAuto", new JSONNumber(isAuto));
		
		Set<String> keyStringSet = this.internalMap.keySet();
		for (String str : keyStringSet) {
			Object retObject = this.internalMap.get(str);
			int startIndex = 0;
			
			if (retObject !=  null && retObject instanceof List<?> ) {
				List<TagListRow> retObjectTagListRow = (List<TagListRow>)retObject;
				JSONArray listObject = new JSONArray();
				for (TagListRow tmpListRow : retObjectTagListRow) {
					listObject.set(startIndex, tmpListRow.getJSONObject());
					startIndex ++;
				}
				detailObject.put(str, listObject);
			}else if (retObject !=  null && retObject instanceof String ) {
				detailObject.put(str, new JSONString((String)retObject));
			}
		}
		
		return recordObject;
	}
}
