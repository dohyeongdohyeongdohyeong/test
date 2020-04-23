package kr.or.visitkorea.admin.client.manager.contents.killercontent.model;

public class KillerContentBanner {

	private String title;
	private String kcbId;
	private String imgId;
	private String linkurl;
	private String imgdesc;
	private String imgPath;
	
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public KillerContentBanner(String title, String kcbId, String imgId, String linkurl, String imgdesc, String imgPath,
			int status) {
		super();
		this.title = title;
		this.kcbId = kcbId;
		this.imgId = imgId;
		this.linkurl = linkurl;
		this.imgdesc = imgdesc;
		this.imgPath = imgPath;
		this.status = status;
	}

	private int status;
	
	public KillerContentBanner() {
	}

	public String getTitle() {
		if(title != null)
			return title;
		else
			return "";
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKcbId() {
		return kcbId;
	}

	public void setKcbId(String kcbId) {
		this.kcbId = kcbId;
	}

	public String getImgId() {
		if(imgId != null)
			return imgId;
		else
			return "";
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getLinkurl() {
		if(linkurl != null)
			return linkurl;
		else
			return "";
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getImgdesc() {
		if(imgdesc != null)
			return imgdesc;
		else
			return "";
	}

	public void setImgdesc(String imgdesc) {
		this.imgdesc = imgdesc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
