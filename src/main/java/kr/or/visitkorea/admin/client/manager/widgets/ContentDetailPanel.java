package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Map;

import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.VisitKorea;

public class ContentDetailPanel extends MaterialPanel {

	private Map<String, Object> data;
	private VisitKorea renderMode;
	private AbstractContentComposite contentComposite;
	
	public ContentDetailPanel() {
		super();
		init();
	}

	public ContentDetailPanel(String... initialClass) {
		super(initialClass);
		init();
	}

	protected void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setWidth("100%");
		this.setDisplay(Display.INLINE_BLOCK);
		this.setVerticalAlign(VerticalAlign.MIDDLE);
		this.setStyleName("contentoverflow");
		this.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.setBorder("1px solid rgb(224, 224, 224)");
		
	}

	public void setData(Map<String, Object> map) {
		this.data = map;
		if (this.contentComposite != null){
			this.contentComposite.setParameters(this.data);
		}
	}

    @Override
    protected void onLoad() {
        super.onLoad();

    }

	public void setType(VisitKorea mode) {
		this.renderMode = mode;
		if (this.contentComposite != null) {
			this.contentComposite.setType(mode);
		}
	}

	public void setComposite(AbstractContentComposite icc) {
		this.contentComposite = icc;
		this.add(this.contentComposite);
	}
	

}
