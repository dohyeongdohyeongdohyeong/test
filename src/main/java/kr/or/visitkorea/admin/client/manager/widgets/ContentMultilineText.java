package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import gwt.material.design.client.ui.MaterialPanel;

public class ContentMultilineText extends MaterialPanel {

	private String[] tgrArry;
	private SafeHtml safeHtml;

	public ContentMultilineText() {
		this.setPadding(10);
	}
	
	public void setText(String content) {
		tgrArry = content.split("\n");
		setupHtml();
	}

	private void setupHtml() {
		String contentStr = "";
		for (String str : tgrArry) {
			contentStr += str + "<br />";
		}
	
		this.getElement().setInnerHTML(contentStr);
	}

	public SafeHtml getSafeHtml(){
		return safeHtml;
	}
	
}
