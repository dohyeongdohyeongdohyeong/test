package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp;

import com.google.gwt.dom.client.Style.Float;

import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;

public class SingleSmallImage extends AreaComponent {

	public SingleSmallImage(Float flt) {
		super();
		setCompType("06.png");
		buildUi();
		this.setFloat(flt);
	}

	
	
}
