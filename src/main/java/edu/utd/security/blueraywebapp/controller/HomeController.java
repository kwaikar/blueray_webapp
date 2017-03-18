package edu.utd.security.blueraywebapp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import edu.utd.security.blueraywebapp.common.LoginBean;
import edu.utd.security.blueraywebapp.common.Policy;
import edu.utd.security.blueraywebapp.service.PoliciesServiceImpl;

@Controller
public class HomeController {


	@Autowired
	private PoliciesServiceImpl policiesService = null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request, @RequestParam String indexURL) {
		String message = "";
		if (request.getSession().getAttribute("user") != null) {
			if (indexURL != null && indexURL.trim().length() != 0) {

				String dataRead = null;

				request.setAttribute("message", message);
			}
		}
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/policy", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody String autocomplete(@RequestParam String filePath, @RequestParam String priviledge,
			HttpServletRequest request) {

		return policiesService.getPolicy(filePath, priviledge);

	}

	@RequestMapping(value = "/policies", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody String allPolicies(HttpServletRequest request) {

		return policiesService.getAllPolicies().toString();

	}
	
	@RequestMapping(value = "/enforcePolicy", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody Boolean enforcePolicy(@RequestParam String filePath, @RequestParam String priviledge,@RequestParam String regex,
			HttpServletRequest request) {

		return policiesService.enforcePolicy(new Policy(filePath, regex,priviledge));

	}
	
	 
	@RequestMapping(value = "/deregisterPolicy", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody String deregisterPolicy(@RequestParam String filePath, @RequestParam String priviledge,@RequestParam String regex,
			HttpServletRequest request) {

		  policiesService.deregisterPolicy(new Policy(filePath, regex,priviledge));
return "true";
	}
 
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		return new ModelAndView("home");

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		System.out.println("loginBean= " + loginBean);

		if (loginBean.getUserName().equals("kanchan") && loginBean.getPassword().equalsIgnoreCase("utd123")) {
			request.getSession().setAttribute("userName", loginBean.getUserName());
		}
		else if (loginBean.getUserName().equals("admin") && loginBean.getPassword().equalsIgnoreCase("utd123")) {
			request.getSession().setAttribute("userName", loginBean.getUserName());
		}
		return new ModelAndView("home");

	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, HttpServletRequest request, @RequestParam String className,
			@RequestParam MultipartFile multipartFile) {

		System.out.println("className" + className);
		if (multipartFile != null) {
			try {
				String fileName = "/tmp/" + System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
				System.out.println(fileName);

				multipartFile.transferTo(new File(fileName));
				request.setAttribute("output",
						executeProcess(request.getSession().getAttribute("userName").toString(), className, fileName));
				System.out.println("File deletion status :" + new File(fileName).delete());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ModelAndView("home");
	}

	public String executeProcess(String user, String className, String fileName) {
		Process p;
		StringBuilder out = new StringBuilder();
		try {
			String command = System.getenv("SPARK_SUBMIT_COMMAND") + " " + className + "  --verbose " + fileName + " "
					+ user;
			System.out.println("Executing command : \n" + command);
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				out.append(line + "</br>");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Setting" + out.toString());
		return out.toString();
	}

	/**
	 * This method logs user out
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logUserOut(HttpServletRequest request) {
		request.getSession().invalidate();
		System.out.println("loggingout");
		return "redirect:/";
	}

}
