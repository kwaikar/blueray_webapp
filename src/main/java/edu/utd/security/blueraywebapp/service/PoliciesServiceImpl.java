package edu.utd.security.blueraywebapp.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import edu.utd.security.blueraywebapp.common.Policy;

@Service
public class PoliciesServiceImpl {

	Map<String, HashSet<Policy>> policies = null;

	/*
	 * public static void main(String[] args) {
	 * PoliciesServiceImpl service = new PoliciesServiceImpl();
	 * service.loadPolicies();
	 * Set<Policy> allPolicies = new HashSet<Policy>();
	 * for (Set<Policy> set : service.policies.values()) {
	 * allPolicies.addAll(set);
	 * }
	 * System.out.println("Are total number of policies found 5? :" + ":" +
	 * (allPolicies.size() == 5));
	 * System.out.println(service.getPolicy(
	 * "hdfs://localhost/user/user_all_phones.csv", "kanchan"));
	 * System.out.println(service.getNumMatches("0,1,2,3,4", "0,1",
	 * "38363.0_38363.0", "48.0_48.0"));
	 * }
	 */
	 

	public String getPolicy(String filePath, String priviledge) {
		Policy policy = new Policy(filePath, "", "");
		if (policies == null) {
			loadPolicies();
		}
		if (!(filePath == null || filePath.trim().length() == 0)) {
			if (priviledge == null || priviledge.trim().length() == 0) {
				for (String key : policies.keySet()) {
					if (key.toLowerCase().startsWith(filePath.toLowerCase())) {
						/**
						 * There exists some policy for this path, it means
						 * that this path should be blocked completely.
						 * empty regex means everything gets blocked.
						 */
						return policy.toString();
					}
				}
			}
			else {
				/*
				 * Priviledge is present,
				 */
				for (String key : policies.keySet()) {
					System.out.println("Checking :" + key);
					if (key.toLowerCase().startsWith(filePath.toLowerCase())) {
						/**
						 * There exists some policy for this path, get
						 * appropriate policy for given priviledge.
						 */
						System.out.println("policies.get(key)" + policies.get(key));
						for (Policy currentPolicy : policies.get(key)) {
							if (currentPolicy.getPriviledge().equalsIgnoreCase(priviledge)) {
								return currentPolicy.toString();
							}
						}
						return policy.toString();
					}
				}
				return "No Policy";
			}

		}
		else {
			/**
			 * Return null because policy does not exist.
			 */
			return "";
		}
		return policy.toString();
	}

	public Set<Policy> getAllPolicies() {
		if (policies == null) {
			loadPolicies();
		}
		Set<Policy> allPolicies = new HashSet<Policy>();
		for (Set<Policy> set : policies.values()) {
			allPolicies.addAll(set);
		}
		return allPolicies;
	}

	public void deregisterPolicy(Policy policy) {
		if (policies == null) {
			loadPolicies();
		}
		String keyToBeRemoved = "";
		for (String key : policies.keySet()) {
			if (key.toLowerCase().startsWith(policy.getFilePath().toLowerCase())) {
				/**
				 * There exists some policy for this path, get
				 * appropriate policy for given priviledge.
				 */
				HashSet<Policy> newPolicies = new HashSet<Policy>();

				for (Policy currentPolicy : policies.get(key)) {
					if (!currentPolicy.getPriviledge().equalsIgnoreCase(policy.getPriviledge())) {
						newPolicies.add(currentPolicy);
					}
				}
				if (newPolicies.size() != 0) {
					policies.put(key, newPolicies);
				}
				else {
					keyToBeRemoved = key;
				}
			}
		}
		policies.remove(keyToBeRemoved);
	}

	public boolean enforcePolicy(Policy policy) {
		if (policies == null) {
			loadPolicies();
		}
		HashSet<Policy> policiesSet = policies.get(policy.getFilePath());
		if (policiesSet == null) {
			policiesSet = new HashSet<Policy>();
		}
		policiesSet.add(policy);
		policies.put(policy.getFilePath(), policiesSet);
		return true;
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
					Policy policy = new Policy(arr[2], regex, arr[1]);
					enforcePolicy(policy);
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
