package edu.utd.security.blueraywebapp.controller;

import java.io.BufferedReader;
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

		System.out.println(":Request received: "+prefix);
		Set<InputBean> results =searchService.select(prefix);
		System.out.println(":results received: "+results);
		if(results.size()>0)
		{
			request.setAttribute("searchResults", results);	
		}
		else
		{
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
			request.getSession().setAttribute("user", loginBean);
			model.addAttribute("userName", loginBean.getUserName());
			request.setAttribute("output", executeProcess(loginBean.getUserName().trim()));
		 
			return new ModelAndView("home");

		} else if (loginBean.getUserName().equals("admin") && loginBean.getPassword().equalsIgnoreCase("utd123")) {
			request.getSession().setAttribute("user", loginBean);
			model.addAttribute("userName", loginBean.getUserName());
			request.setAttribute("output", executeProcess(loginBean.getUserName().trim()));

			
		}return new ModelAndView("home");


	}

	public String executeProcess(String user)
	{
		Process p;
		StringBuilder out = new StringBuilder();
		try {
			p = Runtime.getRuntime().exec("java -classpath /driver:/usr/lib/spark-2.0.0-bin-hadoop2.7/conf/:/usr/lib/spark-2.0.0-bin-hadoop2.7/jars/*:/home/kanchan/workspace2/blueray/target/blueray-0.0.8-SNAPSHOT.jar -javaagent:/home/kanchan/Downloads/aspectjweaver-1.8.5.jar  -Xmx1g org.apache.spark.deploy.SparkSubmit --master local[1] --class BlueRayTest  --verbose /home/kanchan/workspace2/BlueRayTest/target/BlueRayTest-0.0.1-SNAPSHOT.jar "+user);
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine())!= null) {
				out.append(line+"</br>");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
