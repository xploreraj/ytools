package test;

import java.io.*;
import java.util.*;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import data.ModulesData;
import model.Module;
import model.SubModule;

/**
 * Creates the file with some information as an example.
 * Can be used for internal testing.
 * @author rbiswas
 *
 */
public class App {

	public static void main(String[] args) throws Exception {
		//transaction
		SubModule split = new SubModule();
		split.setName("Split");
		split.setPreChecksInfo("Hello world");
		split.setFunctionalInfo("functional information");
		split.setTechnicalInfo("some technical information");
		
		SubModule reconcile = new SubModule();
		reconcile.setName("Reconcile");
		reconcile.setPreChecksInfo("Hi....");
		reconcile.setFunctionalInfo("functional information");
		reconcile.setTechnicalInfo("some technical information");
		
		Module transaction = new Module();
		transaction.setName("Transaction");
		transaction.addSubModule(split);
		transaction.addSubModule(reconcile);
		
		//networth
		SubModule datapoints = new SubModule();
		datapoints.setName("Datapoints");
		datapoints.setPreChecksInfo("Hello world");
		datapoints.setFunctionalInfo("functional information");
		datapoints.setTechnicalInfo("some technical information");
		
		Module networth = new Module();
		networth.setName("Networth");
		networth.addSubModule(datapoints);
		
		ModulesData modulesData = new ModulesData();
		modulesData.addModule(networth);
		modulesData.addModule(transaction);
		
		System.out.println(modulesData.getModuleNames());
		
		ObjectMapper mapper = JsonFactory.create();
//		String json = mapper.writeValueAsString(modules);
//		System.out.println(json);
		//Boon.puts(Boon.toPrettyJson(modules));
		
		//File file = File.createTempFile( "user", ".json" );
		mapper.writeValue(new FileOutputStream("data/info_db.json"), modulesData);
		
		//read data and modify
		ObjectMapper mapper1 = JsonFactory.create();
		ModulesData modulesData1 = mapper1.readValue(new FileInputStream("data/info_db.json"), ModulesData.class);
		//get module
		Module module = modulesData1.getModule("Transaction");
		//rename
		module.setName("Transactions");
		//delete existing entry
		modulesData1.removeModule("Transaction");
		//add updated info
		modulesData1.addModule(module);
		
		mapper.writeValue(new FileOutputStream("data/info_db.json"), modulesData1);
		
		Set<String> moduleSet = modulesData1.getModuleNames();
		System.out.println(moduleSet);
		//for(Module mod: moduleSet)
	}

}
