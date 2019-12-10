package com.suncreate.webService.model;

public class JgbBmxm  implements java.io.Serializable {
	
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String bmxmTitle;//项目名称
	private String bmxmGrade;//项目密级
	private String bmxmType;//项目类别
	private String bmxmModel;//项目代号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBmxmTitle() {
		return bmxmTitle;
	}
	public void setBmxmTitle(String bmxmTitle) {
		this.bmxmTitle = bmxmTitle;
	}
	public String getBmxmGrade() {
		return bmxmGrade;
	}
	public void setBmxmGrade(String bmxmGrade) {
		this.bmxmGrade = bmxmGrade;
	}
	public String getBmxmType() {
		return bmxmType;
	}
	public void setBmxmType(String bmxmType) {
		this.bmxmType = bmxmType;
	}
	public String getBmxmModel() {
		return bmxmModel;
	}
	public void setBmxmModel(String bmxmModel) {
		this.bmxmModel = bmxmModel;
	}
	


}
