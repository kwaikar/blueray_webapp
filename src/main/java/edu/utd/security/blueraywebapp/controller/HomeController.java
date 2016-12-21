package edu.utd.security.blueraywebapp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

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
import edu.utd.security.blueraywebapp.service.InputBean;
import edu.utd.security.blueraywebapp.service.SearchServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private SearchServiceImpl searchService = null;

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

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(ModelMap model, HttpServletRequest request, @RequestParam String prefix) {

		System.out.println(":Request received: " + prefix);
		Set<InputBean> results = searchService.select(prefix);
		System.out.println(":results received: " + results);
		if (results.size() > 0) {
			request.setAttribute("searchResults", results);
		} else {
			request.setAttribute("message", "* No Search Results found");
		}

		return new ModelAndView("home");
	}

	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody List<String> autocomplete(ModelMap model, HttpServletRequest request,
			@RequestParam String term) {

		return searchService.autocomplete(term);
	}

	@RequestMapping(value = "/deleteIndex", method = RequestMethod.GET)
	public ModelAndView deleteIndex(ModelMap model, HttpServletRequest request) {

		request.getSession().removeAttribute("indexPresent");
		searchService.deleteAll();
		request.setAttribute("message", "Kwic Index was removed from the system");
		request.removeAttribute("searchResults");
		return new ModelAndView("home");
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
		} else if (loginBean.getUserName().equals("admin") && loginBean.getPassword().equalsIgnoreCase("utd123")) {
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
				System.out.println("File deletion status :"+new File(fileName).delete());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return new ModelAndView("home");
	}

	public String executeProcess(String user, String className, String fileName) {
		Process p;
		StringBuilder out = new StringBuilder();
		try {
			String command ="/usr/lib/spark-2.0.2-bin-hadoop2.7"+File.separator+"bin"+File.separator+"spark-submit --master local[1] --class "
					+ className + "  --verbose " + fileName + " " + user;
			System.out.println("Executing command : \n"+command);
			p = Runtime.getRuntime()
					.exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				out.append(line + "</br>");
			}

		} catch (Exception e) {
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
