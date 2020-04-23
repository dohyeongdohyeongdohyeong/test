package kr.or.visitkorea.admin.client.manager.contents.database.widget;

import java.util.ArrayList;
import java.util.List;

public class PamphletItem {
	private String pplId = null;
	private String title = null;
	private String fontColor = null;
	private int idx = -1;
	private int viewType;
	private List<PamphletImage> imageList = new ArrayList<PamphletImage>();
	
	public PamphletItem() {
		super();
	}

	public PamphletItem(String title) {
		super();
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public List<PamphletImage> getImageList() {
		return imageList;
	}

	public void setImageList(List<PamphletImage> imageList) {
		this.imageList = imageList;
	}

	public String getPplId() {
		return pplId;
	}

	public void setPplId(String pplId) {
		this.pplId = pplId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	@Override
	public String toString() {
		return "PamphletItem [pplId=" + pplId + ", title=" + title + ", fontColor=" + fontColor + ", idx=" + idx
				+ ", viewType=" + viewType + ", imageList=" + imageList + "]";
	}
}
