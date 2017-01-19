package edu.utd.security.blueraywebapp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import edu.utd.security.blueraywebapp.common.Policy;
import edu.utd.security.blueraywebapp.service.InputBean;
import edu.utd.security.blueraywebapp.service.PoliciesServiceImpl;
import edu.utd.security.blueraywebapp.service.SearchServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private SearchServiceImpl searchService = null;

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
	@RequestMapping(value = "/policies", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody String autocomplete(@RequestParam String filePath,@RequestParam String priviledge, HttpServletRequest request,
			@RequestParam String term) {

	return	policiesService.getPolicy(filePath,priviledge);
		
	}

	
	
	/**
	 *  // val logger = Logger(LoggerFactory.getLogger(this.getClass))
  var policies: HashMap[String, HashSet[Policy]] = new scala.collection.mutable.HashMap
  var policiesLoaded = false;
  loadDefaultPolicy();
  def loadDefaultPolicy()
  {
    enforcePolicy(new Policy(sys.env("BLUERAY_POLICIES_PATH"),Util.encrypt( Util.getSC().sparkUser), "zxasdsxccsdcsd"));
  }
  def enforcePolicy(policy: Policy) {
    policy.priviledge = Util.decrypt(policy.priviledge)
    var policiesSet: HashSet[Policy] = if (policies.get(policy.resourcePath) != None) (policies.get(policy.resourcePath).get) else (new HashSet[Policy]);
    policiesSet.add(policy)
    println("Added policy:" + policy);
    policies.put(policy.resourcePath, policiesSet)
  }
  def deRegisterPolicy(policy: Policy) {
    var policiesSet: Option[HashSet[Policy]] = policies.get(policy.resourcePath)
    if (policiesSet != None) {
      for (entry <- policiesSet.get) {
        if (entry.regex.equalsIgnoreCase(policy.regex) && entry.resourcePath.equalsIgnoreCase(policy.resourcePath) && entry.regex.equalsIgnoreCase(policy.regex)) {
          policiesSet.get.remove(entry)
          if (policiesSet.get.size > 0) {
            policies.put(policy.resourcePath, policiesSet.get);
          } else {
            policies.remove(entry.resourcePath)
          }
        }
      }
    }
    println("Policies deregistered:" + policies)
  }
  def loadPolicies() {
    if (!policiesLoaded) {
      println("Reading policies from path : " + sys.env("BLUERAY_POLICIES_PATH"))
      val lines = Util.getSC().textFile(sys.env("BLUERAY_POLICIES_PATH")).collect().toArray;
      lines.foreach(println);
      for (line <- lines) {

        val arr = line.split(",");
        var regex = arr(0);
        if (arr(0).startsWith("\"")) {
          regex = arr(0).replaceAll("\"", "");
        }
        println("Final: " + arr(0) + " : " + regex);
        enforcePolicy(new Policy(arr(2), Util.encrypt(arr(1)), regex));
      }
      println("Policies read");
      policiesLoaded = true;
    }
  }
  def getPolicy(path: String, priviledgeRestriction: Option[String]): Option[Policy] =
    {
      println("going through======================"+path);
      loadPolicies();
        var policyToBeReturned: Option[Policy] = None;

        for (hashSet <- policies) {
          breakable {
            //println("path.trim:" + path.trim())
            if (hashSet._1.startsWith(path.trim())) {
              if (priviledgeRestriction == None) {
                // println("policyToBeReturned:" + "New")
                return Some(new Policy(path, "", ""))
              }
              for (policy <- hashSet._2) {
                if (policy.priviledge.equalsIgnoreCase(priviledgeRestriction.get)) {
                  policyToBeReturned = Some(policy);
                  //  println("policyToBeReturned:" + policyToBeReturned)
                  break;
                }
              }
              //println("returning some")
              return Some(new Policy(path, "", ""))
            }
          }
        }
        println("Returning policy" + policyToBeReturned)
        return policyToBeReturned
    }*/
	
	

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
