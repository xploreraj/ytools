package data;

import java.util.*;

import service.InconsistentDataException;
import util.CommonUtil;
import model.Module;

public class ModulesData {
	private HashMap<String, Module> modulesMap = new HashMap<>();
	
	public void addModule(Module module) throws InconsistentDataException {
		if(modulesMap.containsKey(module.getName()))
			throw new InconsistentDataException("Addition of module <b>" + module.getName() + "</b> failed as another module by same name already exists!");
		modulesMap.put(module.getName(), module);
	}
	
	public Set<String> getModuleNames() {
		return new TreeSet<String>(modulesMap.keySet());
	}
	
	public Module getModule(String name) {
		return modulesMap.get(CommonUtil.getNameFormattedString(name));
	}
	
	public void removeModule(String name) {
		modulesMap.remove(CommonUtil.getNameFormattedString(name));
	}
}
