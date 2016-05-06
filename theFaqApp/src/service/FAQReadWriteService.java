package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import model.Module;
import model.SubModule;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import data.ModulesData;

public class FAQReadWriteService {
	
	private static ObjectMapper mapper = JsonFactory.create();
	private static final String FILE_NAME = "C:/Users/rbiswas/Dropbox/Public/Java_bin/eclipse_workspace/theFaqApp/data/info_db.json";
	
	public static ModulesData getModulesData() throws FileNotFoundException {
		/*
		 * TO DO: file can be empty initially, this gives class cast exception
		 */
		ModulesData modulesData = mapper.readValue(new FileInputStream(FILE_NAME), ModulesData.class);
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
	public static String getSubModuleNamesAsJson(ModulesData modulesData, String moduleName) {
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
	public static void saveModuleData(ModulesData modulesData, String currModuleName,
			String newModuleName, String currSubModuleName,
			String newSubModuleName, String info) throws FileNotFoundException, InconsistentDataException {

		Module module;
		SubModule subModule;
		
		newModuleName 		= CommonUtil.getNameFormattedString(newModuleName);
		newSubModuleName 	= CommonUtil.getNameFormattedString(newSubModuleName);
		currModuleName 		= CommonUtil.getNameFormattedString(currModuleName);
		currSubModuleName 	= CommonUtil.getNameFormattedString(currSubModuleName);
		
		//check for inconsistent data
		/*if(newModuleName.isEmpty() || 
				!newSubModuleName.isEmpty() && info.isEmpty() ||
				newSubModuleName.isEmpty() && !info.isEmpty())
			throw new InconcsistentDataException("Module name is empty or Submodule name and Info are inconsistently passed");*/
		if(newModuleName.isEmpty())
				throw new InconsistentDataException("Module name can not be empty.");
		
		if(!newSubModuleName.isEmpty() && info.isEmpty() ||
				newSubModuleName.isEmpty() && !info.isEmpty())
				throw new InconsistentDataException("Submodule name and Info must not be empty.");
		
		if(!newModuleName.isEmpty() && !newSubModuleName.isEmpty() && !info.isEmpty()
				&& (currModuleName.isEmpty() || modulesData.getModule(currModuleName)==null)) {
			//create
			module = new Module();
			module.setName(newModuleName);
			
			subModule = new SubModule();
			subModule.setName(newSubModuleName);
			subModule.setInfo(info);
			
			module.addSubModule(subModule);
			modulesData.addModule(module);
			
			mapper.writeValue(new FileOutputStream(FILE_NAME), modulesData);
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
			if(!newSubModuleName.isEmpty() && !info.isEmpty()
					&& (currSubModuleName.isEmpty() || module.getSubModule(currSubModuleName)==null)) {
				subModule = new SubModule();
				subModule.setName(newSubModuleName);
				subModule.setInfo(info);
				module.addSubModule(subModule);
			}
			else {
				subModule = module.getSubModule(currSubModuleName);
				//remove old name entry and insert new name entry
				if (!currSubModuleName.equals(newSubModuleName)) {
					module.removeSubModule(currSubModuleName);
					subModule.setName(newSubModuleName);
					module.addSubModule(subModule);
				}
				if(!info.isEmpty())
					subModule.setInfo(info);
			}
			mapper.writeValue(new FileOutputStream(FILE_NAME), modulesData);
		}
		
		
	}

}
