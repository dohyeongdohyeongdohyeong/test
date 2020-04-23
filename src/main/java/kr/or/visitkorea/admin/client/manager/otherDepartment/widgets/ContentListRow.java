package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.List;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialPanel;

public class ContentListRow extends MaterialPanel implements VisitKoreaListRow {

	private String cotId;
	private double count;
	private List<VisitKoreaListCell> cells;

	public ContentListRow() {
		init();
	}
	
	public ContentListRow(List<VisitKoreaListCell> cells) {
		this.cells = cells;
		init();
	}

	private void init() {
		
		this.setLeft(0);
		this.setRight(0);
		this.setWidth("99.5%");
		this.setHeight("50");
		this.setMargin(2);
		this.setHeight("50px");
		this.setBorderBottom("1px solid #c8c8c8");
		
		renderChild();
	}

	public void renderChild() {
		
		for (int i=0; i<cells.size(); i++) {
			
			VisitKoreaListCell materialWidet = (VisitKoreaListCell)cells.get(i);

			if (materialWidet.isDivBorder()) {
				((MaterialWidget)materialWidet).setBorderRight("1px solid #c8c8c8");
			}
			
			this.add((MaterialWidget)materialWidet);
			
		}
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
	
	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public String getCotId() {
		return cotId;
	}

	public void setCotId(String cotId) {
		this.cotId = cotId;
	}

	@Override
	public String toString() {
		return "ContentListRow [cotId=" + cotId + ", count=" + count + ", cells=" + cells + "]";
	}

}
