package Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ImportFilter {

	ArrayList<String> set=new ArrayList<>();

	ImportFilter(String cfgfilepath) {
		init(cfgfilepath);
	}
	
	void init(String cfgfilepath){
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(cfgfilepath));
			String item = null;
			while ((item = bfr.readLine()) != null) {
				set.add(item);
			}
		} catch (Exception e) {
			return;
		}
	}
	
	String contain(String imp){
		for(String item:set){
			if(item.length()<imp.length()){
				String[] items=item.split(".");
				String[] imps=imp.split(".");
				for(int i=0;i<items.length;i++){
					if(i>=imps.length||!items[i].equals(imps[i])){
						return null;
					}
				}
				return item;
			}
			else if(imp.equals(item)){
				return item;
			}
		}
		return null;
	}

}
