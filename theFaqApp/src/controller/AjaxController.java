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
		
		ModulesData modulesData = DataService.getModulesData();
		
		String action = request.getParameter("action");
		
		if("getModuleNames".equals(action)) {
			String json;
			if (modulesData == null) json = "";
			else json = DataService.getModuleNamesAsJson(modulesData);
            out.write(json);  
		}
		else if("getSubModuleNames".equals(action)) {
			String moduleName = request.getParameter("moduleName");
			String json = DataService.getSubModuleNames(modulesData, moduleName);
            out.write(json);
		}
		else if("getSubModule".equals(action)) {
			String modulename = request.getParameter("moduleName");
			String subModuleName = request.getParameter("subModuleName");
			String json = DataService.getSubModule(modulesData, modulename, subModuleName);
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
			
			try {
				DataService.saveModuleData(modulesData, currModuleName, newModuleName, 
						currSubModuleName, newSubModuleName, 
						preChecksInfo, functionalInfo, technicalInfo);
				response.setStatus(201);
				out.print("Data successfully saved.");
			} catch (InconsistentDataException e) {
				e.printStackTrace();
				response.sendError(400, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(500, "Some internal error occurred.");
			}
		}
	}

}
