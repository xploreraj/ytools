package service;

//import javax.validation.constraints.NotNull;

public class CommonUtil {
	
	private CommonUtil(){}
	
	/**
	 * Formats string by trimming and removing additional inner whitespaces, and
	 * by title casing.
	 * @param str name string
	 * @return formatted String
	 */
	public static String getNameFormattedString(String str) {
		if (str == null || str.isEmpty()) return str;
		str = inStringTrim(str);
		String fmtd = "";
		for(String word : str.split(" "))
			fmtd = fmtd +  " " + word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
		return fmtd.trim();
	}
	
	public static String inStringTrim(String str) {
		if (str == null) return str;
		String pat = "(?:\\s){2,}"; //2 or more consecutive whitespaces
		str = str.trim().replaceAll(pat, " ");
		return str;
	}
}
