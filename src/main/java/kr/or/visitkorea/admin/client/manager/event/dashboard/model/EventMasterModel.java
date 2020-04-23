package kr.or.visitkorea.admin.client.manager.event.dashboard.model;

public class EventMasterModel {

	
    private String evtId;
    private String cotId;
    private String title;
    private String startDate;
    private String endDate;
    private String announceDate;
    private String status;
    private String contents;
    private String reject;
    private String caution;
    private String announceType;
    private String templateType;
    private String isCaution;
    private String isStaff;
    private String isCollect;
    private String isLogin;
    private String winDesc;
    private String isWinDesc;
	private String cautionTemplate;
    private String staffTemplate;
    private String imgId ;
    
    private String statusNm;
    
    
	public String getEvtId() {
		return evtId;
	}
	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}
	public String getCotId() {
		return cotId;
	}
	public void setCotId(String cotId) {
		this.cotId = cotId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getAnnounceDate() {
		return announceDate;
	}
	public void setAnnounceDate(String announceDate) {
		this.announceDate = announceDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getReject() {
		return reject;
	}
	public void setReject(String reject) {
		this.reject = reject;
	}
	public String getCaution() {
		return caution;
	}
	public void setCaution(String caution) {
		this.caution = caution;
	}
	public String getAnnounceType() {
		return announceType;
	}
	public void setAnnounceType(String announceType) {
		this.announceType = announceType;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getIsCaution() {
		return isCaution;
	}
	public void setIsCaution(String isCaution) {
		this.isCaution = isCaution;
	}
	public String getIsStaff() {
		return isStaff;
	}
	public void setIsStaff(String isStaff) {
		this.isStaff = isStaff;
	}
	public String getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	public String getWinDesc() {
		return winDesc;
	}
	public void setWinDesc(String winDesc) {
		this.winDesc = winDesc;
	}
	public String getIsWinDesc() {
		return isWinDesc;
	}
	public void setIsWinDesc(String isWinDesc) {
		this.isWinDesc = isWinDesc;
	}
	public String getCautionTemplate() {
		return cautionTemplate;
	}
	public void setCautionTemplate(String cautionTemplate) {
		this.cautionTemplate = cautionTemplate;
	}
	public String getStaffTemplate() {
		return staffTemplate;
	}
	public void setStaffTemplate(String staffTemplate) {
		this.staffTemplate = staffTemplate;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	
    public String getStatusNm() {
		return statusNm;
	}
	public void setStatusNm(String statusNm) {
		this.statusNm = statusNm;
	}
	
	@Override
	public String toString() {
		return "EventMasterModel [evtId=" + evtId + ", cotId=" + cotId + ", title=" + title + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", announceDate=" + announceDate + ", status=" + status + ", contents="
				+ contents + ", reject=" + reject + ", caution=" + caution + ", announceType=" + announceType
				+ ", templateType=" + templateType + ", isCaution=" + isCaution + ", isStaff=" + isStaff
				+ ", isCollect=" + isCollect + ", isLogin=" + isLogin + ", winDesc=" + winDesc + ", isWinDesc="
				+ isWinDesc + ", cautionTemplate=" + cautionTemplate + ", staffTemplate=" + staffTemplate + ", imgId="
				+ imgId + "]";
	}
    
    
    


}
