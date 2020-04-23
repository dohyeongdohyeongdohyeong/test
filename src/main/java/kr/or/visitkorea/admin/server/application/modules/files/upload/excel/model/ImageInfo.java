package kr.or.visitkorea.admin.server.application.modules.files.upload.excel.model;

import org.jdom2.Element;

public class ImageInfo {
	
	private String sequence;
	
	private String referenceId;
	
	private String referenceTypeId;
	
	private String using;
	
	private String picture;
	
	private String path;
	
	private String mainImgChk;
	
	private String service;

	private int order;

	
	public ImageInfo(String sequence, String referenceId, String referenceTypeId, String using, String picture, String path, String mainImgChk, String service, int order) {
		super();
		this.service = service;
		this.sequence = sequence;
		this.referenceId = referenceId;
		this.referenceTypeId = referenceTypeId;
		this.using = using;
		this.picture = picture;
		this.path = path;
		this.mainImgChk = mainImgChk;
		this.order = order;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = Integer.parseInt(service)+"";
	}

	public String getSequence() {
		return sequence == null ? "" : sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getReferenceId() {
		return referenceId == null ? "" : referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceTypeId() {
		return referenceTypeId == null ? "" : referenceTypeId;
	}

	public void setReferenceTypeId(String referenceTypeId) {
		this.referenceTypeId = referenceTypeId;
	}

	public String getUsing() {
		return using == null ? "" : using;
	}

	public void setUsing(String using) {
		this.using = using;
	}

	public String getPicture() {
		return picture == null ? "" : picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPath() {
		return path == null ? "" : path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMainImgChk() { 
		return mainImgChk;
	}

	public void setMainImgChk(String mainImgChk) {
		this.mainImgChk = mainImgChk;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Element getElement() {
		
		Element imageElement = new Element("image");
		
		imageElement.setAttribute("order", this.getOrder()+"");
		
		imageElement.setAttribute("referenceId", this.getReferenceId().replaceAll(".0", ""));
		
		imageElement.setAttribute("referenceTypeId", this.getReferenceTypeId().replaceAll(".0", ""));
		
		imageElement.setAttribute("using", this.getUsing());
		
		imageElement.setAttribute("picture", this.getPicture());
		
		imageElement.setAttribute("path", this.getPath());
		
		imageElement.setAttribute("service", this.getService());
		
		if (!this.getMainImgChk().equals("null")) {
			imageElement.setAttribute("main", "true");
		}

		return imageElement;
		
	}
}
