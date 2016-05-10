package model;

import java.io.Serializable;
import java.util.*;

import service.CommonUtil;
import service.InconsistentDataException;

public class Module implements Serializable, Comparable<Module>{

	private static final long serialVersionUID = 1L;

	private String name;
	private final HashMap<String, SubModule> subModulesMap = new HashMap<String, SubModule>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = CommonUtil.getNameFormattedString(name);
	}
	
	@Override
	public int compareTo(Module module) {
		return this.getName().compareToIgnoreCase(module.getName());
	}
	
	public Set<String> getSubModuleNames() {
		if (subModulesMap.isEmpty())
			return null;
		return new TreeSet<String>(subModulesMap.keySet());
	}
//	
//	public SubModule getSubModule(String name) {
//		return subModulesMap.get(name);
//	}
//	
	public void addSubModule(SubModule subModule) throws InconsistentDataException {
		if(subModulesMap.containsKey(subModule.getName()))
			throw new InconsistentDataException("Addition of sub module <b>" + subModule.getName() + "</b> failed as another sub module by same name already exists!");
		subModulesMap.put(subModule.getName(), subModule);
	}
	
	public SubModule getSubModule(String name) {
		return subModulesMap.get(CommonUtil.getNameFormattedString(name));
	}
	
	public void removeSubModule(String name) {
		subModulesMap.remove(CommonUtil.getNameFormattedString(name));
	}
	
}
