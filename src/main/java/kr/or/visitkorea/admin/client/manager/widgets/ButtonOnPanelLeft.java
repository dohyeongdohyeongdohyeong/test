package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Document;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.html.Span;

public class ButtonOnPanelLeft extends Span {


    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.buttonOnPanelCss());
    }

    /**
     * Creates a badge component that can be added to Link,
     * Collection, DropDown, SideNav and any other Material components.
     */
    public ButtonOnPanelLeft() {
        super(Document.get().createSpanElement(), "buttonOnPanelRight");
    }

    /**
     * @param text text of the badge
     */
    public ButtonOnPanelLeft(String text) {
        this();
        setText(text);
    }

    /**
     * @param text     text of the ButtonOnPanelextends
     * @param isCircle is a circle ButtonOnPanelextends
     */
    public ButtonOnPanelLeft(String text, boolean isCircle) {
        this();
        setText(text);
        setCircle(isCircle);
    }

    /**
     * Badge with text and color
     *
     * @param text      text of the ButtonOnPanelextends
     * @param textColor text color of the ButtonOnPanelextends
     * @param bgColor   background color of the ButtonOnPanelextends
     */
    public ButtonOnPanelLeft(String text, Color textColor, Color bgColor) {
        this();
        setText(text);
        setTextColor(textColor);
        setBackgroundColor(bgColor);
    }

 
	
}
