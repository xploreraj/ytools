package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

import model.Module;
import model.SubModule;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import util.CommonUtil;
import data.ModulesData;

public class DataService {
	
	private static ObjectMapper mapper = JsonFactory.create();
	private static final String FILE_NAME = "C:/Users/rbiswas/Documents/GitHub/ytools/theFaqApp/data/info_db.json";
	
	public static ModulesData getModulesData() throws FileNotFoundException {

		ModulesData modulesData;
		try {
			modulesData = mapper.readValue(new FileInputStream(FILE_NAME),  ModulesData.class);
		}
		catch(ClassCastException e) {
			//If file is empty, then Boon throws CCE while trying to map with ModulesData.class
			modulesData = null;
		}
		return modulesData;
	}
	
	/**
	 * @param modulesData
	 * @return Set of module names as JSON string
	 */
	public static String getModuleNamesAsJson(ModulesData modulesData) {
		Set<String> moduleNames = modulesData.getModuleNames();	
		String json = mapper.toJson(moduleNames);
		//mapper.writeValue(out, subModuleNames);
		return json;
	}
	
	/**
	 * @param modulesData
	 * @param parentModule
	 * @return Set of submodule names as JSON string
	 */
	public static String getSubModuleNames(ModulesData modulesData, String moduleName) {
		Set<String> subModuleNames = modulesData.getModule(moduleName).getSubModuleNames();
		String json = mapper.toJson(subModuleNames);
		return json;
	}
	
	/**
	 * @param modulesData
	 * @param modulename
	 * @param subModuleName
	 * @param formatInfo 
	 * @return
	 */
	public static String getSubModule(ModulesData modulesData, String modulename,
			String subModuleName, boolean formatInfo) {
		Module module = modulesData.getModule(modulename);
		SubModule subModule = module.getSubModule(subModuleName);
		if (formatInfo) {
			subModule.parseJson2HtmlSubModuleInfos();
		}
		String json = mapper.toJson(subModule);
		return json;
	}
	
	/**
	 * This will be used to add new modules or update existing modules, based upon parameters
	 * 
	 * @param modulesData ModulesData
	 * @param currModuleName String
	 * @param newModuleName String
	 * @param currSubModuleName String
	 * @param newSubModuleName String
	 * @param info String
	 * @throws FileNotFoundException
	 * @throws InconsistentDataException 
	 */
	public static void saveModuleData(String currModuleName, String newModuleName, 
			String currSubModuleName, String newSubModuleName, 
			String preChecksInfo, String functionalInfo, String technicalInfo) 
					throws FileNotFoundException, InconsistentDataException {

		Module module;
		SubModule subModule;
		ModulesData modulesData = DataService.getModulesData();
		
		//check for inconsistent data
		/*if(newModuleName.isEmpty() || 
				!newSubModuleName.isEmpty() && info.isEmpty() ||
				newSubModuleName.isEmpty() && !info.isEmpty())
			throw new InconcsistentDataException("Module name is empty or Submodule name and Info are inconsistently passed");*/
		if(newModuleName.isEmpty())
				throw new InconsistentDataException("Module name can not be empty.");
		
		boolean infoPresent = preChecksInfo!=null || !preChecksInfo.isEmpty() ||
				functionalInfo!=null || !functionalInfo.isEmpty() ||
				technicalInfo!=null || !technicalInfo.isEmpty();
		
		if(!newSubModuleName.isEmpty() && !infoPresent ||
				newSubModuleName.isEmpty() && infoPresent)
				throw new InconsistentDataException("Submodule name and Info must not be empty.");
		
		newModuleName 		= CommonUtil.getNameFormattedString(newModuleName);
		newSubModuleName 	= CommonUtil.getNameFormattedString(newSubModuleName);
		currModuleName 		= CommonUtil.getNameFormattedString(currModuleName);
		currSubModuleName 	= CommonUtil.getNameFormattedString(currSubModuleName);
		
		if(modulesData == null || !newModuleName.isEmpty() && !newSubModuleName.isEmpty() && infoPresent
				&& (currModuleName.isEmpty() || modulesData.getModule(currModuleName)==null)) {
			//create
			module = new Module();
			if(modulesData == null)
				modulesData = new ModulesData();
			module.setName(newModuleName);
			
			subModule = new SubModule();
			subModule.setName(newSubModuleName);
			subModule.setInfos(preChecksInfo, functionalInfo, technicalInfo);
			subModule.setLastUpdated();
			
			module.addSubModule(subModule);
			modulesData.addModule(module);
		}
		else {
			module = modulesData.getModule(currModuleName);
			//remove old name entry and insert new name entry
			if(!newModuleName.isEmpty() && !currModuleName.equals(newModuleName)) {
				modulesData.removeModule(currModuleName);
				module.setName(newModuleName);
				modulesData.addModule(module);
			}
			
			//see if need to create submodule, currSubModuleName field is empty in page for create
			if(!newSubModuleName.isEmpty() && infoPresent
					&& (currSubModuleName.isEmpty() || module.getSubModule(currSubModuleName)==null)) {
				subModule = new SubModule();
				subModule.setName(newSubModuleName);
				subModule.setInfos(preChecksInfo, functionalInfo, technicalInfo);
				subModule.setLastUpdated();
				module.addSubModule(subModule);
			}
			else {
				//edit submodule
				subModule = module.getSubModule(currSubModuleName);
				//remove old name entry and insert new name entry?
				if (!currSubModuleName.equals(newSubModuleName)) {
					module.removeSubModule(currSubModuleName);
					subModule.setName(newSubModuleName);
					module.addSubModule(subModule);
				}
				
				if(infoPresent) {
					subModule.setInfos(preChecksInfo, functionalInfo, technicalInfo);
				}
				
				subModule.setLastUpdated();
			}
		}
		
		mapper.writeValue(new FileOutputStream(FILE_NAME), modulesData);
	}

}
