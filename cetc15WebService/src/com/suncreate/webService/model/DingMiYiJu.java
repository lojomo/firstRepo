package com.suncreate.webService.model;

public class DingMiYiJu  implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private String id;
	private String name;
	private String parent_id;
	private String project_level;
	private String file_level;
	private String ordinal;
	private String security_year_limit;
	private String knowledge_range;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parentId) {
		parent_id = parentId;
	}
	public String getProject_level() {
		return project_level;
	}
	public void setProject_level(String projectLevel) {
		project_level = projectLevel;
	}
	public String getFile_level() {
		return file_level;
	}
	public void setFile_level(String fileLevel) {
		file_level = fileLevel;
	}
	public String getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(String ordinal) {
		this.ordinal = ordinal;
	}
	public String getSecurity_year_limit() {
		return security_year_limit;
	}
	public void setSecurity_year_limit(String securityYearLimit) {
		security_year_limit = securityYearLimit;
	}
	public String getKnowledge_range() {
		return knowledge_range;
	}
	public void setKnowledge_range(String knowledgeRange) {
		knowledge_range = knowledgeRange;
	}


}
