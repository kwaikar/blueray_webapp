package edu.utd.security.blueraywebapp.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;

import edu.utd.security.blueraywebapp.common.Policy;

@Service
public class PoliciesServiceImpl {

	Map<String, HashSet<Policy>> policies = null;

	public static void main(String[] args) {
		PoliciesServiceImpl service = new PoliciesServiceImpl();
		service.loadPolicies();
		Set<Policy>allPolicies = new HashSet<Policy>();
		for(Set<Policy> set:service.policies.values()){
		allPolicies.addAll(set);
		}
		System.out.println("Are total number of policies found 5? :"+  ":"+ (allPolicies.size()==5));
		System.out.println(service.getPolicy("hdfs://localhost/user", "kanchan"));

	}

	public String getPolicy(String filePath, String priviledge) {
		Policy policy = new Policy(filePath, "", "");

		if (!StringUtils.isEmptyOrWhitespaceOnly(filePath)) {
			if (StringUtils.isEmptyOrWhitespaceOnly(priviledge)) {
				if (policies == null) {
					loadPolicies();
				}
				else {
					for (String key : policies.keySet()) {
						if (key.toLowerCase().startsWith(filePath)) {
							/**
							 * There exists some policy for this path, it means
							 * that this path should be blocked completely.
							 * empty regex means everything gets blocked.
							 */
							return policy.toJson();
						}
					}
				}
			}
			else {
				/*
				 * Priviledge is present,
				 */
				for (String key : policies.keySet()) {
					if (key.toLowerCase().startsWith(filePath)) {
						/**
						 * There exists some policy for this path, get
						 * appropriate policy for given priviledge.
						 */
						for (Policy currentPolicy : policies.get(key)) {
							if (currentPolicy.getPriviledge().equalsIgnoreCase(priviledge)) {
								return currentPolicy.toJson();
							}
						}
						return policy.toJson();
					}
				}
			}

		}
		else {
			/**
			 * Return null because policy does not exist.
			 */
			return "";
		}
		return policy.toJson();
	}

	public void loadPolicies() {
		if (policies == null) {
			// Load policies from file.
			String file;
			try {
				policies = new HashMap<String, HashSet<Policy>>();
				System.out.println("Reading file " + System.getenv("BLUERAY_POLICIES_PATH"));
				file = FileUtils.readFileToString(new File(System.getenv("BLUERAY_POLICIES_PATH")));
				String[] lines = file.split("\n");
				for (String line : lines) {
					String[] arr = line.split(",");
					String regex = arr[0];
					if (regex.startsWith("\"")) {
						regex = regex.replaceAll("\"", "");
					}
					Policy policy =new Policy(arr[2], regex, arr[1]);
					HashSet<Policy> policiesSet = policies.get(arr[2]);
					if (policiesSet == null) {
						policiesSet = new HashSet<Policy>();
					}
					policiesSet.add(policy);
					policies.put(policy.getFilePath(),policiesSet);
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
