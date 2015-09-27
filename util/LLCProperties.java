package util;

import java.util.Properties;

public class LLCProperties {

	public static Properties p;
	
	public static void init (Properties p) {
		if(p != null)
			LLCProperties.p = p;
	}
	
	public Properties parameters() {
		return LLCProperties.p;
	}
}
