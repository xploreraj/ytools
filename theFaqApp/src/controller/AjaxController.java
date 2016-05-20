package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.DataService;
import service.InconsistentDataException;
import data.ModulesData;

/**
 * Servlet implementation class AjaxController
 */
@WebServlet("/AjaxController")
public class AjaxController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		ModulesData modulesData = DataService.INSTANCE_.getModulesData();
		
		String action = request.getParameter("action");
		
		if("getModuleNames".equals(action)) {
			String json;
			if (modulesData == null) json = "";
			else json = DataService.setToJsonString(modulesData.getModuleNames());
            out.write(json);  
		}
		else if("getSubModuleNames".equals(action)) {
			String moduleName = request.getParameter("moduleName");
			String json = DataService.setToJsonString(modulesData.getModule(moduleName).getSubModuleNames());
            out.write(json);
		}
		else if("getSubModule".equals(action)) {
			String modulename = request.getParameter("moduleName");
			String subModuleName = request.getParameter("subModuleName");
			boolean formatInfo = false;
			if ("true".equals(request.getParameter("formatInfo"))){
				formatInfo = true;
			}
			String json = DataService.getSubModule(modulesData, modulename, subModuleName, formatInfo);
			out.write(json);
		}
		else if("submitForm".equals(action)) {
			String currModuleName = request.getParameter("currModuleName");
			String newModuleName = request.getParameter("newModuleName");
			String currSubModuleName = request.getParameter("currSubModuleName");
			String newSubModuleName = request.getParameter("newSubModuleName");
			String preChecksInfo = request.getParameter("preChecksInfo");
			String functionalInfo = request.getParameter("functionalInfo");
			String technicalInfo = request.getParameter("technicalInfo");
			String lastUpdatedParam = request.getParameter("lastUpdated"); //will be null when SubModule info is not requested in clientside
			
			long lastUpdated = lastUpdatedParam==null||lastUpdatedParam.isEmpty() ? 0 : Long.parseLong(lastUpdatedParam.trim());
			
			try {
				DataService.INSTANCE_.saveModuleData(currModuleName, newModuleName, 
						currSubModuleName, newSubModuleName, lastUpdated,
						preChecksInfo, functionalInfo, technicalInfo);
				
				response.setStatus(200);
				out.print("Data successfully saved.");
				out.flush();
				out.close();
			} catch (InconsistentDataException e) {
				response.setStatus(400);
				out.print("Inconsistent Data: " + e.getMessage());
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
				out.print("SERIOUS: Please report this to admin:<br>" + e.getClass().getSimpleName() + ": " + e.getMessage());
				out.flush();
			}
		}
	}

}
