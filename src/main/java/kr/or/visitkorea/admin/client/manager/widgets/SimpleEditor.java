package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.base.MaterialWidget;

public class SimpleEditor extends AbstractContentPanel {

	@Override
	public void init() {
		this.getElement().setAttribute("contentEditable", "true");
		this.setBorder("1px solid #333");
		this.setOverflow(Overflow.AUTO);
		this.addMouseUpHandler(event->{
			Element element = getSelectedText();
			MaterialWidget panel = new MaterialWidget(element);
			panel.setFontSize("1.5em");
			panel.setFontWeight(FontWeight.BOLD);
		});
	}

	private native Element getSelectedText() /*-{ 
    	var divNode = document.createElement("DIV");
	    
	    if ($wnd.getSelection) {
			if ($wnd.getSelection().toString().length > 0){
	    		$wnd.getSelection().getRangeAt(0).surroundContents(divNode);
	    	}
	    }
	    
//	    else if ($doc.getSelection) {
//	        txt = $doc.getSelection();
//	    }

//	    else if ($doc.selection) {
//	        txt = $doc.selection.createRange().text;
//	    }

	    else return;
	    
	    return divNode;
	    
	}-*/;

}
