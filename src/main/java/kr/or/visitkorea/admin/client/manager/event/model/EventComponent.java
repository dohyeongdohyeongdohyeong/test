package kr.or.visitkorea.admin.client.manager.event.model;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class EventComponent {
	private String evcId;
	private String ewcId;
	private String evtId;
	private String imgId;
	private String subEvtId;
	private int compType;
	private int compIdx;
	private String compBody;
	private int sizeType;
	private String imgLink;
	private String imgDesc;
	private boolean isBox;
	private boolean isVertical;
	private boolean isDelete;
	
	public static EventComponent fromJson(JSONObject obj) {
		EventComponent component = new EventComponent();
		component.setDelete(false);
		if (obj.containsKey("EVC_ID"))
			component.setEvcId(obj.get("EVC_ID").isString().stringValue());
		if (obj.containsKey("EWC_ID"))
			component.setEwcId(obj.get("EWC_ID").isString().stringValue());
		if (obj.containsKey("SUB_EVT_ID"))
			component.setSubEvtId(obj.get("SUB_EVT_ID").isString().stringValue());
		if (obj.containsKey("EVT_ID"))
			component.setEvtId(obj.get("EVT_ID").isString().stringValue());
		if (obj.containsKey("COMP_BODY"))
			component.setCompBody(obj.get("COMP_BODY").isString().stringValue());
		if (obj.containsKey("IMG_ID"))
			component.setImgId(obj.get("IMG_ID").isString().stringValue());
		if (obj.containsKey("IMG_LINK"))
			component.setImgLink(obj.get("IMG_LINK").isString().stringValue());
		if (obj.containsKey("IMG_DESC"))
			component.setImgDesc(obj.get("IMG_DESC").isString().stringValue());
		if (obj.containsKey("SIZE_TYPE"))
			component.setSizeType((int) obj.get("SIZE_TYPE").isNumber().doubleValue());
		if (obj.containsKey("COMP_TYPE"))
			component.setCompType((int) obj.get("COMP_TYPE").isNumber().doubleValue());
		if (obj.containsKey("COMP_IDX"))
			component.setCompIdx((int) obj.get("COMP_IDX").isNumber().doubleValue());
		if (obj.containsKey("IS_VERTICAL"))
			component.setVertical(obj.get("IS_VERTICAL").isBoolean().booleanValue());
		if (obj.containsKey("IS_BOX"))
			component.setBox(obj.get("IS_BOX").isBoolean().booleanValue());
		
		return component;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		if (this.evcId != null)
			obj.put("EVC_ID", new JSONString(this.evcId));
		if (this.ewcId != null)
			obj.put("EWC_ID", new JSONString(this.ewcId));
		if (this.subEvtId != null)
			obj.put("SUB_EVT_ID", new JSONString(this.subEvtId));
		if (this.evtId != null)
			obj.put("EVT_ID", new JSONString(this.evtId));
		if (this.imgId != null)
			obj.put("IMG_ID", new JSONString(this.imgId));
		if (this.imgLink != null)
			obj.put("IMG_LINK", new JSONString(this.imgLink));
		if (this.imgDesc != null)
			obj.put("IMG_DESC", new JSONString(this.imgDesc));
		if (this.compBody != null)
			obj.put("COMP_BODY", new JSONString(this.compBody));
		obj.put("SIZE_TYPE", new JSONNumber(this.sizeType));
		obj.put("COMP_TYPE", new JSONNumber(this.compType));
		obj.put("COMP_IDX", new JSONNumber(this.compIdx));
		obj.put("IS_VERTICAL", JSONBoolean.getInstance(this.isVertical));
		obj.put("IS_BOX", JSONBoolean.getInstance(this.isBox));
		obj.put("IS_DELETE", JSONBoolean.getInstance(this.isDelete));
		return obj;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}

	public String getEvcId() {
		return evcId;
	}

	public void setEvcId(String evcId) {
		this.evcId = evcId;
	}

	public String getEwcId() {
		return ewcId;
	}

	public void setEwcId(String ewcId) {
		this.ewcId = ewcId;
	}
	
	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getSubEvtId() {
		return subEvtId;
	}

	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}

	public int getCompType() {
		return compType;
	}

	public void setCompType(int compType) {
		this.compType = compType;
	}

	public int getCompIdx() {
		return compIdx;
	}

	public void setCompIdx(int compIdx) {
		this.compIdx = compIdx;
	}

	public String getCompBody() {
		return compBody;
	}

	public void setCompBody(String compBody) {
		this.compBody = compBody;
	}

	public int getSizeType() {
		return sizeType;
	}

	public void setSizeType(int sizeType) {
		this.sizeType = sizeType;
	}

	public boolean isBox() {
		return isBox;
	}

	public void setBox(boolean isBox) {
		this.isBox = isBox;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}

	@Override
	public String toString() {
		return "EventGift ["
				+ "\n\t evcId=" + evcId
				+ "\n\t ewcId=" + ewcId
				+ ",\n\t subEvtId=" + subEvtId
				+ ",\n\t evtId=" + evtId
				+ ",\n\t imgId=" + imgId
				+ ",\n\t compType=" + compType
				+ ",\n\t compBody=" + compBody
				+ ",\n\t compIdx=" + compIdx
				+ ",\n\t imgLink=" + imgLink
				+ ",\n\t imgDesc=" + imgDesc
				+ ",\n\t sizeType=" + sizeType
				+ ",\n\t isBox=" + isBox
				+ ",\n\t isVertical=" + isVertical
				+ ",\n\t isDelete=" + isDelete
				+ ",\n]";
	}
	
}
