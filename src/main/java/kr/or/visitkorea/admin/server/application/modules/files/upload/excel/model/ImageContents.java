package kr.or.visitkorea.admin.server.application.modules.files.upload.excel.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jdom2.Document;
import org.jdom2.Element;

public class ImageContents {
	
	private List<ImageInfo> imageInfo = new ArrayList<ImageInfo>();

	public int size() { 
		return imageInfo.size();
	}

	public boolean isEmpty() {
		return imageInfo.isEmpty();
	}

	public boolean contains(Object o) {
		return imageInfo.contains(o);
	}

	public Iterator<ImageInfo> iterator() {
		return imageInfo.iterator();
	}

	public Object[] toArray() {
		return imageInfo.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return imageInfo.toArray(a);
	}

	public boolean add(ImageInfo e) {
		return imageInfo.add(e);
	}

	public boolean remove(ImageInfo o) {
		return imageInfo.remove(o);
	}

	public void clear() {
		imageInfo.clear();
	}

	public boolean equals(Object o) {
		return imageInfo.equals(o);
	}

	public int hashCode() {
		return imageInfo.hashCode();
	}

	public ImageInfo get(int index) {
		return imageInfo.get(index);
	}

	public ImageInfo set(int index, ImageInfo element) {
		return imageInfo.set(index, element);
	}

	public void add(int index, ImageInfo element) {
		imageInfo.add(index, element);
	}

	public ImageInfo remove(int index) {
		return imageInfo.remove(index);
	}

	public int indexOf(Object o) {
		return imageInfo.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return imageInfo.lastIndexOf(o);
	}

	public ListIterator<ImageInfo> listIterator() {
		return imageInfo.listIterator();
	}

	public ListIterator<ImageInfo> listIterator(int index) {
		return imageInfo.listIterator(index);
	}

	public List<ImageInfo> subList(int fromIndex, int toIndex) {
		return imageInfo.subList(fromIndex, toIndex);
	}
	

	// define normal property
	
	private Long contentId;
	
	private Integer contentType;
	
	private String title;
	
	private String category1;
	
	private String category2;
	
	private String category3;
	
	private String area;
	
	private String local;

	public Long getContentId() {
		return contentId == null ? 0L : contentId;
	}

	public void setContentId(Object contentId) {
		this.contentId = (long)Float.parseFloat(contentId+"");
	}

	public int getContentType() {
		return contentType == null ? 0 : contentType;
	}

	public void setContentType(Object contentType) {
		this.contentType = (int)Float.parseFloat(contentType+"");
	}

	public String getTitle() {
		return title == null ? "" : title;
	}

	public void setTitle(Object title) {
		this.title = title+"";
	}

	public String getCategory1() {
		return category1 == null ? "" : category1;
	}

	public void setCategory1(Object category1) {
		this.category1 = category1+"";
	}

	public String getCategory2() {
		return category2 == null ? "" : category2;
	}

	public void setCategory2(Object category2) {
		this.category2 = category2+"";
	}

	public String getCategory3() {
		return category3 == null ? "" : category3;
	}

	public void setCategory3(Object category3) {
		this.category3 = category3+"";
	}

	public String getArea() {
		return area == null ? "" : area;
	}

	public void setArea(Object area) {
		this.area = area+"";
	}

	public String getLocal() {
		return local == null ? "" : local;
	}

	public void setLocal(Object local) {
		this.local = local+"";
	}

	public Document getDocument() {

		Document retDoc = new Document();
		
		Element rootElement = new Element("imageContent");

		rootElement.setAttribute("contentId", this.getContentId().toString());
		rootElement.setAttribute("contentType", String.valueOf(this.getContentType()));
		rootElement.setAttribute("title", this.getTitle());
		rootElement.setAttribute("category1", this.getCategory1());
		rootElement.setAttribute("category2", this.getCategory2());
		rootElement.setAttribute("category3", this.getCategory3());
		rootElement.setAttribute("area", this.getArea());
		rootElement.setAttribute("local", this.getLocal());

		retDoc.setRootElement(rootElement);
		
		for (ImageInfo info : imageInfo) {
			rootElement.addContent(info.getElement());
		}
		
		return retDoc;
		
	}
	
	public Element getElement() {
		
		Element imageContent = new Element("imageContent");
		imageContent.setAttribute("contentId", this.getContentId().toString());
		imageContent.setAttribute("contentType", String.valueOf(this.getContentType()));
		imageContent.setAttribute("title", this.getTitle());
		imageContent.setAttribute("category1", this.getCategory1());
		imageContent.setAttribute("category2", this.getCategory2());
		imageContent.setAttribute("category3", this.getCategory3());
		imageContent.setAttribute("area", this.getArea());
		imageContent.setAttribute("local", this.getLocal());

		for (ImageInfo info : imageInfo) {
			imageContent.addContent(info.getElement());
		}
		
		return imageContent;
		
	}

	
}
