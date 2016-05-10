package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;

import model.Module;
import model.SubModule;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

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
			System.out.println("modules data in classcastException=" + modulesData);
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
	 * @return
	 */
	public static String getSubModule(ModulesData modulesData, String modulename,
			String subModuleName) {
		Module module = modulesData.getModule(modulename);
		SubModule subModule = module.getSubModule(subModuleName);
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
	public static void saveModuleData(ModulesData modulesData, String currModuleName, String newModuleName, 
			String currSubModuleName, String newSubModuleName, 
			String preChecksInfo, String functionalInfo, String technicalInfo) 
					throws FileNotFoundException, InconsistentDataException {

		Module module;
		SubModule subModule;
		
		newModuleName 		= CommonUtil.getNameFormattedString(newModuleName);
		newSubModuleName 	= CommonUtil.getNameFormattedString(newSubModuleName);
		currModuleName 		= CommonUtil.getNameFormattedString(currModuleName);
		currSubModuleName 	= CommonUtil.getNameFormattedString(currSubModuleName);
		
		String infoConcatenated = preChecksInfo + functionalInfo + technicalInfo + "";
		
		
		//check for inconsistent data
		/*if(newModuleName.isEmpty() || 
				!newSubModuleName.isEmpty() && info.isEmpty() ||
				newSubModuleName.isEmpty() && !info.isEmpty())
			throw new InconcsistentDataException("Module name is empty or Submodule name and Info are inconsistently passed");*/
		if(newModuleName.isEmpty())
				throw new InconsistentDataException("Module name can not be empty.");
		
		if(!newSubModuleName.isEmpty() && infoConcatenated.isEmpty() ||
				newSubModuleName.isEmpty() && !infoConcatenated.isEmpty())
				throw new InconsistentDataException("Submodule name and Info must not be empty.");
		
		if(modulesData == null || !newModuleName.isEmpty() && !newSubModuleName.isEmpty() && !infoConcatenated.isEmpty()
				&& (currModuleName.isEmpty() || modulesData.getModule(currModuleName)==null)) {
			//create
			module = new Module();
			if(modulesData == null)
				modulesData = new ModulesData();
			module.setName(newModuleName);
			
			subModule = new SubModule();
			subModule.setName(newSubModuleName);
			subModule.setPreChecksInfo(preChecksInfo);
			subModule.setFunctionalInfo(functionalInfo);
			subModule.setTechnicalInfo(technicalInfo);
			
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
			if(!newSubModuleName.isEmpty() && !infoConcatenated.isEmpty()
					&& (currSubModuleName.isEmpty() || module.getSubModule(currSubModuleName)==null)) {
				subModule = new SubModule();
				subModule.setName(newSubModuleName);
				subModule.setPreChecksInfo(preChecksInfo);
				subModule.setFunctionalInfo(functionalInfo);
				subModule.setTechnicalInfo(technicalInfo);
				module.addSubModule(subModule);
			}
			else {
				//edit submodule
				subModule = module.getSubModule(currSubModuleName);
				//remove old name entry and insert new name entry
				if (!currSubModuleName.equals(newSubModuleName)) {
					module.removeSubModule(currSubModuleName);
					subModule.setName(newSubModuleName);
					module.addSubModule(subModule);
				}
				
				if(!infoConcatenated.isEmpty()) {
					subModule.setPreChecksInfo(preChecksInfo);
					subModule.setFunctionalInfo(functionalInfo);
					subModule.setTechnicalInfo(technicalInfo);
				}
			}
		}
		
		mapper.writeValue(new FileOutputStream(FILE_NAME), modulesData);
		
		
	}

}