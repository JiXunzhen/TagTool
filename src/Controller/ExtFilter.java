package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ExtFilter {
	HashMap<String, ExtType> set=new HashMap<>();

	public ExtFilter(String cfgfilepath) {
		set.put("java", ExtType.JAVA);
		set.put("JAVA", ExtType.JAVA);
	}

	public void init(String cfgfilepath) {
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(cfgfilepath));
			String item = null;
			while ((item = bfr.readLine()) != null) {
			}
		} catch (Exception e) {
			return;
		}
	}

	public ExtType contain(String filename) {
		for (Map.Entry<String, ExtType> entry : set.entrySet()) {
			if (filename.endsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return ExtType.NORMAL;
	}

}
