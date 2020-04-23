package kr.or.visitkorea.admin.client.widgets.window;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.addins.client.MaterialAddins;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.TextAlign;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.manager.main.HorizontalScrollableContentPanel;

public class HorizontalContentPanel extends HorizontalScrollableContentPanel {

    static {
        if (MaterialAddins.isDebug()) {
            MaterialDesignBase.injectCss(MaterialWindowDebugClientBundle.INSTANCE.windowCssDebug());
        } else {
            MaterialDesignBase.injectCss(MaterialWindowClientBundle.INSTANCE.windowCss());
        }
    }

	private int nowPosition = 0;
//	private int width;

	public HorizontalContentPanel() {
		super();
		init();
	}

	public HorizontalContentPanel(AbstractContentPanel panel) {
		super(panel);
		init();
	}

	public HorizontalContentPanel(MaterialExtentsWindow meWindow) {
		super(meWindow);
		init();
	}

	public void init() {

		this.setStyleName("startContent");
		this.setTextAlign(TextAlign.CENTER);
		this.setHeight("100%");
		this.setWidth("100%");
		this.setLayoutPosition(Position.RELATIVE);
	}

	@Override
	public void setPixelSize(int width, int height) {
		
//		this.width = width;
		
		if (width >= 0) {
			setWidth((width*2+10)  + "px");
		}
		if (height >= 0) {
			setHeight(height - 40 + "px");
		}

	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.setLeft(this.nowPosition);
	}

	@Override
	public void next(int offset) {
		this.nowPosition -= offset;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		this.setTransition(cfg);
   		this.setTransform("translate("+nowPosition+"px,0);");
		this.setLeft(this.nowPosition);
	}

	@Override
	public void prev(int offset) {
		this.nowPosition += offset;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		this.setTransition(cfg);
   		this.setTransform("translate("+nowPosition+"px,0);");
		this.setLeft(this.nowPosition);
	}

	public void go(int position) {
		this.nowPosition = position;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		this.setTransition(cfg);
   		this.setTransform("translate("+nowPosition+"px,0);");
		this.setLeft(this.nowPosition);
	}

}
