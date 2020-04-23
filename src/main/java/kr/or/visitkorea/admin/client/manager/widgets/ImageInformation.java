package kr.or.visitkorea.admin.client.manager.widgets;

public class ImageInformation extends ItemInformation { 

	private String url;
	
	private String comment;
	
	private String caption;
	
	private String imgId;
	
	private String aciId;
	
	private int iscaption;

	public ImageInformation() {
		super();
	}

	public ImageInformation(String url, String comment, String imgId, String caption, int iscaption) {
		super();
		this.url = url;
		this.comment = comment;
		this.imgId = imgId;
		this.caption = caption;
		this.iscaption = iscaption;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getComment() {
		return this.comment == null ? "" : this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getCaption() {
		return this.caption == null ? "" : this.caption;
	}
	
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public int getisCaption() {
		return this.iscaption;
	}
	
	public void setisCaption(int iscaption) {
		this.iscaption = iscaption;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getAciId() {
		return aciId;
	}

	public void setAciId(String aciId) {
		this.aciId = aciId;
	}

	@Override
	public String toString() {
		return "ImageInformation [url=" + url + ", comment=" + comment + ", caption=" + caption + ", imgId=" + imgId
				+ ", aciId=" + aciId + ", iscaption=" + iscaption + "]";
	}
}
