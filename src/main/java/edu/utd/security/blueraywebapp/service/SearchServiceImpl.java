package edu.utd.security.blueraywebapp.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.utd.security.blueraywebapp.dao.DatabaseManager;

@Service
public class SearchServiceImpl {

	public void populateIndex(List<String> shifts, String originalString, String url, int priority, boolean delete) {
		List<InputBean> inputBeans = new ArrayList<InputBean>();
		for (String shift : shifts) {
			if (delete) {
				delete(url);
			} else {
				InputBean bean = new InputBean(shift, originalString, url, priority);
				inputBeans.add(bean);
			}
		}
		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.insert(inputBeans);
	}

	public void deleteAll() {
		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.deleteAll();

	}

	public void delete(String url) {

		DatabaseManager dbm = DatabaseManager.getInstance();
		dbm.delete(url);
	}

	public List<String> autocomplete(String prefix)
	{
		List<String> list = new ArrayList<String>();
		Set<InputBean> beans = select(prefix);
		for (InputBean inputBean : beans) {
			list.add(inputBean.originalString);
		}
		return list;
	}
	public Set<InputBean> select(String prefix) {
		System.out.println("Prefix call"+prefix);
		DatabaseManager dbm = DatabaseManager.getInstance();
		String[] splits = null;
		Set<InputBean> op = new LinkedHashSet<InputBean>();
		if (prefix.contains(" AND ")) {
			splits = prefix.split(" AND ");
			Set<InputBean> set1 = new LinkedHashSet<InputBean> (dbm.select(splits[0]));
			Set<InputBean> set2 = new LinkedHashSet<InputBean> (dbm.select(splits[1]));
			set1.retainAll(set2);
			op= set1;
		} else if (prefix.contains(" OR ")) {
			splits = prefix.split(" OR ");
			Set<InputBean> set1 = new LinkedHashSet<InputBean> (dbm.select(splits[0]));
			Set<InputBean> set2 = new LinkedHashSet<InputBean> (dbm.select(splits[1]));
			set1.addAll(set2);
			op= set1;
		}else if (prefix.startsWith("NOT ")) {
			System.out.println("Found"+	new LinkedHashSet<InputBean> (	dbm.selectNOT( prefix.replaceAll("NOT ",""))));
			op= new LinkedHashSet<InputBean> (	dbm.selectNOT( prefix.replaceAll("NOT ","")));
			 
		}
		else
		{
		op=new LinkedHashSet<InputBean> (dbm.select(prefix));
		}
		System.out.println("OP:"+op);
		return op;
	}

}
