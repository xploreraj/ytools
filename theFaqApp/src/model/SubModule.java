package model;

import java.io.Serializable;
import java.util.Date;

import service.CommonUtil;

public class SubModule implements Serializable, Comparable<SubModule> {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String functionalInfo;
	private String technicalInfo;
	private String preChecks;
	private String info; //has to be removed, since we have three specialized info data as above
	private Date date;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = CommonUtil.getNameFormattedString(name);
	}	
	public String getFunctionalInfo() {
		return functionalInfo;
	}
	public void setFunctionalInfo(String functionalInfo) {
		this.functionalInfo = functionalInfo;
	}
	public String getTechnicalInfo() {
		return technicalInfo;
	}
	public void setTechnicalInfo(String technicalInfo) {
		this.technicalInfo = technicalInfo;
	}
	public String getPreChecks() {
		return preChecks;
	}
	public void setPreChecks(String preChecks) {
		this.preChecks = preChecks;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	/*public ArrayList<String> getInfoList() {
		return infoList;
	}
	
	public void addInfo(String info) {
		infoList.add(info);
	}*/
	@Override
	public int compareTo(SubModule subModule) {
		return this.getName().compareToIgnoreCase(subModule.getName());
	}
	
}
