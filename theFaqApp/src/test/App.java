package test;

import java.io.*;
import java.util.*;

import org.boon.Boon;
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
		split.setInfo("Hello world");
		
		SubModule reconcile = new SubModule();
		reconcile.setName("Reconcile");
		reconcile.setInfo("Hi....");
		
		Module transaction = new Module();
		transaction.setName("Transaction");
		transaction.addSubModule(split);
		transaction.addSubModule(reconcile);
		
		//networth
		SubModule datapoints = new SubModule();
		datapoints.setName("Datapoints");
		datapoints.setInfo("Hello world");
		
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
		/*ObjectMapper mapper = JsonFactory.create();
		ModulesData modulesData = mapper.readValue(new FileInputStream("data/info_db.json"), ModulesData.class);
		//get module
		Module module = modulesData.getModule("Transaction");
		//rename
		module.setName("Transactions");
		//delete existing entry
		modulesData.removeModule("Transaction");
		//add updated info
		modulesData.addModule(module);
		
		mapper.writeValue(new FileOutputStream("data/info_db.json"), modulesData);*/
		
		Set<String> moduleSet = modulesData.getModuleNames();
		System.out.println(moduleSet);
		//for(Module mod: moduleSet)
	}

}
