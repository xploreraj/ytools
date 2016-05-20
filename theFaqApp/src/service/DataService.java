package service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import model.Module;
import model.SubModule;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import util.CommonUtil;
import data.ModulesData;

public class DataService {
	
	private static final ObjectMapper mapper = JsonFactory.create();
	private static final String FILE_NAME = "C:/Users/rbiswas/Documents/GitHub/ytools/theFaqApp/data/info_db.json";
	public static final DataService INSTANCE_ = new DataService();
	
	private DataService(){
		
	}
	
	/**
	 * 
	 * @return ModulesData object, either with mapped data from file or empty object if file is empty
	 * @throws IOException
	 */
	public synchronized ModulesData getModulesData() throws IOException {

		ModulesData modulesData;
		FileInputStream fis = new FileInputStream(FILE_NAME);
		try {
			modulesData = mapper.readValue(fis,  ModulesData.class);
		}
		//If file is empty, then Boon throws CCE while trying to map with ModulesData.class
		catch(ClassCastException e) {
			modulesData = new ModulesData();
		}
		finally {
			if (fis!=null) fis.close();
		}
		return modulesData;
	}
	
	/**
	 * Takes a Set&lt;String> as input
	 * @param stringSet
	 * @return JSON String representation of input
	 */
	public static String setToJsonString(Set<String> stringSet) {
		return mapper.toJson(stringSet);
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
	 * This will be used to add new modules or update existing modules, based upon parameters.
	 * Empty currModuleName and currSubModuleName signify creating of new data
	 */
	public synchronized void saveModuleData(String currModuleName, String newModuleName, 
			String currSubModuleName, String newSubModuleName, 
			long lastUpdated, String preChecksInfo, String functionalInfo, String technicalInfo) 
					throws IOException, InconsistentDataException {

		Module module;
		SubModule subModule;
		ModulesData modulesData = getModulesData();
		
		newModuleName 		= CommonUtil.getNameFormattedString(newModuleName);
		newSubModuleName 	= CommonUtil.getNameFormattedString(newSubModuleName);
		currModuleName 		= CommonUtil.getNameFormattedString(currModuleName);
		currSubModuleName 	= CommonUtil.getNameFormattedString(currSubModuleName);
		
		if (newModuleName.isEmpty())
			throw new InconsistentDataException("Module name can not be empty.");
		
		//At least one info has to be present to save submodule information
		boolean infoPresent = preChecksInfo!=null && !preChecksInfo.isEmpty() ||
				functionalInfo!=null && !functionalInfo.isEmpty() ||
				technicalInfo!=null && !technicalInfo.isEmpty();
		

		// 1. Create new module
		if(currModuleName.isEmpty()
				&& !newModuleName.isEmpty() 
				&& !newSubModuleName.isEmpty() 
				&& infoPresent) {

			if (modulesData.getModule(newModuleName) != null)
				throw new InconsistentDataException("Another module with same name already exists.");
			
			module = new Module();
			module.setName(newModuleName);
			
			subModule = new SubModule();
			subModule.setName(newSubModuleName);
			subModule.setPreChecksInfo(preChecksInfo);
			subModule.setFunctionalInfo(functionalInfo);
			subModule.setTechnicalInfo(technicalInfo);
			subModule.setLastUpdated();
			
			module.addSubModule(subModule);
			modulesData.addModule(module);
			
			saveDataToFile(modulesData);
			return;
		}
		// 2. Update existing module
		else {
			module = modulesData.getModule(currModuleName);
			
			// 2.1. Rename existing module only?
			if (currSubModuleName.isEmpty()
					&& newSubModuleName.isEmpty()
					&& !infoPresent) {
				
				if (modulesData.getModuleNames().contains(newModuleName))
					throw new InconsistentDataException("Another module with same name already exists.");
				
				module.setName(newModuleName);
				modulesData.removeModule(currModuleName);
				modulesData.addModule(module);
				
				saveDataToFile(modulesData);
				return;
				
			}
			// 2.2. Create new SubModule
			else if (currSubModuleName.isEmpty()
					&& !module.getSubModuleNames().contains(currSubModuleName)
					&& !newSubModuleName.isEmpty()
					&& infoPresent){
				
				subModule = new SubModule();
				subModule.setName(newSubModuleName);
				subModule.setPreChecksInfo(preChecksInfo);
				subModule.setFunctionalInfo(functionalInfo);
				subModule.setTechnicalInfo(technicalInfo);
				subModule.setLastUpdated();
				
				module.addSubModule(subModule);
				
				saveDataToFile(modulesData);
				return;
				
			}
			// 2.3. Update existing submodule
			else if (!currSubModuleName.isEmpty()
					&& !newSubModuleName.isEmpty()
					&& infoPresent) {
				
				// 2.3.1 Has newSubModuleName changed and matches with an existing SubModule name?
				if (!currSubModuleName.equalsIgnoreCase(newSubModuleName)
						&& module.getSubModuleNames().contains(newSubModuleName))
					throw new InconsistentDataException("Another submodule with same name already exists");
				
				subModule = module.getSubModule(currSubModuleName);
				
				// 2.3.2 Has Submodule information changed in file since last read by client?
				// Either Submodule name is changed or info updated
				if(subModule == null || Long.compare(lastUpdated, subModule.getLastUpdated()) == -1)
					throw new InconsistentDataException("The Submodule has been updated since last read by client."
							+ " Please store your changes locally, refresh the page and merge them to update.");
				
				subModule.setName(newSubModuleName);
				subModule.setPreChecksInfo(preChecksInfo);
				subModule.setFunctionalInfo(functionalInfo);
				subModule.setTechnicalInfo(technicalInfo);
				subModule.setLastUpdated();
				
				module.removeSubModule(currSubModuleName);
				module.addSubModule(subModule);
				
				saveDataToFile(modulesData);
				return;
				
			}
		}
		
		// 3. Any save condition mismatch from above will result in exception
		throw new InconsistentDataException("Module name can not be empty or match existing module."
				+ " If submodule data is passed, then submodule name should not be empty and"
				+ " at least one information field should have data.");
		
	}
	
	/**
	 * Save ModulesData object to Json file.
	 * @throws IOException if file not found or stream close failed.
	 */
	public synchronized void saveDataToFile(ModulesData modulesData) throws IOException {
		FileOutputStream fos = new FileOutputStream(FILE_NAME);
		try {
			mapper.writeValue(fos,  modulesData);
		}
		finally {
			if (fos!=null) fos.close();
		}
	}


}
