package com.suncreate.webService.model;


public class PrintOrBurnJobResult {
	private String  jobtype;
	private String  jobnumber;
	private String  wfid;
    private String secrettype;
	public String getJobtype() {
		return jobtype;
	}
	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}
	public String getJobnumber() {
		return jobnumber;
	}
	public void setJobnumber(String jobnumber) {
		this.jobnumber = jobnumber;
	}
	public String getWfid() {
		return wfid;
	}
	public void setWfid(String wfid) {
		this.wfid = wfid;
	}
	public String getSecrettype() {
		return secrettype;
	}
	public void setSecrettype(String secrettype) {
		this.secrettype = secrettype;
	}
	
}
