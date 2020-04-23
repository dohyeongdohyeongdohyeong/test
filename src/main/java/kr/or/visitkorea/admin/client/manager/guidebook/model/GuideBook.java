package kr.or.visitkorea.admin.client.manager.guidebook.model;

public class GuideBook {
	public String gbId;

	public String title;
	public String author;
	public String areaCode;
	public String sigunguCode;
	public String createDate;
	public String pdfLink;
	public String pdfName;
	public Integer use;

    public String imageId;
	public String imagePath;
	public String imageDesc;
	public String areaName;
	public String sigunguName;
	public String themeCode;
	public String publisher;
	public String publishDate;

    public GuideBook() {
	}

	@Override
	public String toString() {
    	return "GuideBook {"
				+ "gbId: " + gbId
				+ ", title: " + title
				+ ", author: " + author
				+ ", areaCode: " + areaCode
				+ ", sigunguCode: " + sigunguCode
				+ ", createDate: " + createDate
				+ ", pdfLink: " + pdfLink
				+ ", pdfName: " + pdfName
				+ ", use: " + use
				+ ", imageId: " + imageId
				+ ", imagePath: " + imagePath
				+ ", imageDesc: " + imageDesc
				+ ", areaName: " + areaName
				+ ", sigunguName: " + sigunguName
				+ ", themeCode: " + themeCode
				+ ", publisher: " + publisher
				+ ", publishDate: " + publishDate
				+ "}";
	}
}