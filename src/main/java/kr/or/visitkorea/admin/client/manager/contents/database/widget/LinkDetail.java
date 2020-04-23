package kr.or.visitkorea.admin.client.manager.contents.database.widget;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.ListItem;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;

public class LinkDetail extends ListItem {

	private LinkDetailType linkDetailType;
	private MaterialPanel detailColumn;
	private MaterialLink link;
	private String bgRgbColor;
	private String linkText = "";
	private String titleTextColor;
	private String url;
	private String imgSource;
	private String imgId;
	private String startDate;
	private String ImagePath;
	private String endDate;
	private String adiId;
	private MaterialImage image;
	private int ImageCheck;
	
    public LinkDetail(LinkDetailType linkDetailType, MaterialPanel detailPanel, String adiId) {
        super();
        
        this.linkDetailType = linkDetailType;
        this.detailColumn = detailPanel;
        this.adiId = adiId;
        
        this.getElement().getStyle().setProperty("listStyle", "none");
        this.getElement().getStyle().setProperty("width", "100%");
        this.getElement().getStyle().setProperty("position", "relative");
        this.getElement().getStyle().setProperty("cursor", "pointer");
        this.setBorder("1px solid #e6e6e6");
        this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
        this.setPadding(15);
        this.setDisplay(Display.BLOCK);
        this.setWidth("140px");
        this.setHeight("140px");
        this.setMarginRight(7.5);
        this.setMarginBottom(7.5);
        
        loadDefaultType();
        
    }

    public String getAdiId() {
		return adiId;
	}

	public void setAdiId(String adiId) {
		this.adiId = adiId;
	}

	public void setDefaultType(LinkDetailType linkDetailType) {
        this.linkDetailType = linkDetailType;
        reload();
    }
    
    public void reload() {
    	this.clear();
    	loadDefaultType();
    }
    
    public void loadDefaultType() {
		
        link = new MaterialLink();
		link.setText(this.linkText);
		link.getElement().getFirstChildElement().getStyle().setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		link.getElement().getFirstChildElement().getStyle().setTextAlign(TextAlign.JUSTIFY);
		
		if (bgRgbColor == null) {
			this.bgRgbColor = "#4da6ff";
		}
		this.getElement().getStyle().setProperty("backgroundColor", this.bgRgbColor);

		if (titleTextColor == null) {
			this.titleTextColor = "#ffffff";
		}
		link.getElement().getFirstChildElement().getStyle().setProperty("color", titleTextColor);
		
		link.setFontSize("1.1em");
        
        if (this.url != null) {
        	link.getElement().setAttribute("link", this.url);
        	link.setTarget("_blank");
        }
        
        image = new MaterialImage();

        link.add(image);
		
		if (linkDetailType.equals(LinkDetailType.NORMAL)) {
			
	        this.add(link);
	        this.setPadding(15);
	        
	        image.setLayoutPosition(Position.ABSOLUTE);
	        image.setRight(15);
	        image.setBottom(15);
	        image.setWidth("24px");
	        image.setHeight("24px");
	        if (this.imgSource != null) {
	        	image.setUrl(this.imgSource);
	        }else{
	        	image.setUrl((String) Registry.get("service.server") + "/resources/images/sub/icon_m_mapsearch.png");
	        }
		}else if (linkDetailType.equals(LinkDetailType.FULL_IMAGE)) {
			
			this.add(link);
			this.setPadding(1);
		
	        image.setLayoutPosition(Position.STATIC);
	        image.setWidth("100%");
	        if (this.imgSource != null) {
	        	image.setUrl(this.imgSource);
	        }else{
	        	image.setUrl((String) Registry.get("service.server") + "/resources/images/sub/btn_coupondown.png");
	        }
			
		}
		
	}

    @Override
    public void setTitle(String title) {
    	super.setTitle(title);
    	this.linkText = title;
    	link.setText(this.linkText);
    }
    
    public String getTitle() {
    	return link.getText();
    }

	public LinkDetailType getLinkDetailType() {
		return linkDetailType;
	}

	public void setLinkDetailType(LinkDetailType linkDetailType) {
		this.linkDetailType = linkDetailType;
	}

	public void setBGColor(String rgb) {
		this.bgRgbColor = rgb;
	}

	public String getBGColor() {
		return this.bgRgbColor;
	}
	
	public void setTitleTextColor(String rgb) {
		this.titleTextColor = rgb;
	}

	public String getTitleTextColor() {
		return this.titleTextColor;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public String getImageSource() {
		return this.imgSource;
	}
	public String getImagepath() {
		return this.ImagePath;
	}
	public void setImagepath(String path) {
		this.ImagePath = path;
	}
	
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public void setImageSource(String src) {
		imgSource = src;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getImageCheck() {
		return ImageCheck;
	}
	
	public void setImageCheck(int ImageCheck) {
		this.ImageCheck = ImageCheck;
	}
	

}
