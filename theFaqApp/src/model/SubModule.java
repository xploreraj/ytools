package model;

import java.io.Serializable;
import java.util.Date;

import org.boon.json.annotations.JsonInclude;

import service.CommonUtil;

public class SubModule implements Serializable, Comparable<SubModule> {

	private static final long serialVersionUID = 1L;
	
	private @JsonInclude String name;
	private @JsonInclude String functionalInfo;
	private @ JsonInclude String technicalInfo;
	private @JsonInclude String preChecksInfo;
	
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
	public String getPreChecksInfo() {
		return preChecksInfo;
	}
	public void setPreChecksInfo(String preChecksInfo) {
		this.preChecksInfo = preChecksInfo;
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
