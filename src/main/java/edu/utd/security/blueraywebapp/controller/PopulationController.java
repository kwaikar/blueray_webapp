package edu.utd.security.blueraywebapp.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PopulationController {


 
	
	@RequestMapping(value = "/getNumPopulation", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody int numMatches(@RequestParam String races, @RequestParam String genders,@RequestParam String ages,@RequestParam String zips
			,HttpServletRequest request) {

		return getNumMatches(races,genders,ages,zips);

	}
  
		  {
			try {
			if (intermediaryMap == null) {
				intermediaryMap = new HashMap<String, TreeMap<Integer, TreeMap<Integer, Integer>>>();
				finalMap = new HashMap<String, Integer>();
				String[] dictOfPopulation = FileUtils.readFileToString(new File("/data/kanchan/dict.csv")).split(
						"\n");
				String[] zips = FileUtils.readFileToString(new File("/data/kanchan/sortedziplist")).split("\n");
				Map<Integer, Integer> zipIndexMap = new HashMap<Integer, Integer>();
				for (String zip : zips) {
					String[] zipSplit = zip.split(",");
					zipIndexMap.put(Integer.parseInt(zipSplit[0].trim()), Integer.parseInt(zipSplit[1].trim()));
				}

				for (String dictEntry : dictOfPopulation) {
					String[] dictEntrySplit = dictEntry.split(",");
					String key = dictEntrySplit[0].trim() + dictEntrySplit[1].trim();

					TreeMap<Integer, TreeMap<Integer, Integer>> mapOfAges = intermediaryMap.get(key);
					if (mapOfAges == null) {
						mapOfAges = new TreeMap<Integer, TreeMap<Integer, Integer>>();
					}
					int age = Integer.parseInt(dictEntrySplit[2].trim());
					TreeMap<Integer, Integer> mapOfZips = mapOfAges.get(age);

					if (mapOfZips == null) {
						mapOfZips = new TreeMap<Integer, Integer>();
					}
					mapOfZips.put(zipIndexMap.get(Integer.parseInt(dictEntrySplit[3].trim())),
							(int) Double.parseDouble(dictEntrySplit[4].trim()));

					mapOfAges.put(age, mapOfZips);

					intermediaryMap.put(key, mapOfAges);

				}
			}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	}
	static HashMap<String, TreeMap<Integer, TreeMap<Integer, Integer>>>  intermediaryMap = null;
	static  Map<String, Integer> finalMap = null;

	public int getNumMatches(String races, String genders, String ageRange, String zipRange) {
	
			
			Integer cntToBeReturned = finalMap.get(races + "|" + genders + "|" + zipRange + "|" + ageRange);
			if (cntToBeReturned == null) {
				String[] racesSplit = races.split(",");
				String[] gendersSplit = genders.split(",");
				String[] zipSplit = zipRange.split("_");

				String[] ageSplit = ageRange.split("_");
				int count = 0;
				for (String race : racesSplit) {
					for (String gender : gendersSplit) {
						TreeMap<Integer, TreeMap<Integer, Integer>> mapOfAges = intermediaryMap.get(race.trim()
								+ gender.trim());
						if (mapOfAges != null) {
							Map<Integer, TreeMap<Integer, Integer>> mapOfSubAges = mapOfAges.subMap(
									(int) Double.parseDouble(ageSplit[0].trim()), true,
									(int) Double.parseDouble(ageSplit[1].trim()), true);
							for (TreeMap<Integer, Integer> map : mapOfSubAges.values()) {

								Map<Integer, Integer> subMap = map.subMap((int) Double.parseDouble(zipSplit[0].trim()),
										true, (int) Double.parseDouble(zipSplit[1].trim()), true);
								if (subMap != null) {
									for (Integer val : subMap.values()) {
										count += val;
									}
								}

							}
						}
					}
				}
				System.out.println("Cache miss [" + races + "|" + genders + "|" + zipRange + "|" + ageRange + "] :"
						+ count);
				synchronized (finalMap) {
					finalMap.put(races + "|" + genders + "|" + zipRange + "|" + ageRange, count);	
				}
				return count;
			}
			else {
				return cntToBeReturned;
			}
		

	}
}
