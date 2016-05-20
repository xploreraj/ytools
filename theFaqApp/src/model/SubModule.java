package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.boon.json.annotations.JsonIgnore;
import org.boon.json.annotations.JsonInclude;

import util.CommonUtil;

public class SubModule implements Serializable, Comparable<SubModule> {

	private static final long serialVersionUID = 1L;
	
	private @JsonInclude String name;
	private @JsonInclude String functionalInfo;
	private @JsonInclude String technicalInfo;
	private @JsonInclude String preChecksInfo;
	private @JsonInclude long lastUpdated;
	private @JsonIgnore boolean writeLock;
	
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
	
	/**
	 * Helper method to set information strings for this SubModule
	 * @param preChecks
	 * @param funcInfo
	 * @param techInfo
	 */
	public void setInfos(String preChecks, String funcInfo, String techInfo) {
		
		setPreChecksInfo(preChecks);
		setFunctionalInfo(funcInfo);
		setTechnicalInfo(techInfo);
		
	}
	
	public long getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(){
		lastUpdated = new Date().getTime();
	}
	
	public void parseJson2HtmlSubModuleInfos() {
		setPreChecksInfo(CommonUtil.parseJsontoHTML(getPreChecksInfo()));
		setFunctionalInfo(CommonUtil.parseJsontoHTML(getFunctionalInfo()));
		setTechnicalInfo(CommonUtil.parseJsontoHTML(getTechnicalInfo()));
	}
	
	@Override
	public int compareTo(SubModule subModule) {
		return this.getName().compareToIgnoreCase(subModule.getName());
	}
	/**
	 * Someone should get a lock for write access
	 * @return true if lock obtained, false if write already locked by someone else
	 */
	public boolean tryLockWrite(){
		return writeLock ? false : (writeLock=true);
			
	}
	/**
	 * option of releasing the lock in between,
	 * or after saving data to file
	 */
	public void unlockWrite(){
		writeLock = false;
	}
	
}
