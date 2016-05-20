package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	/**
	 * Parse raw JSON data into HTML formattable text.
	 * At this moment only following substitutions are supported:<br>
	 * 1. Replace '\n' to '&lt;br&gt;', for occurrances not inside [code] tags.<br>
	 * 2. Change [code] tags to &lt;pre&gt; tags since we are using Alex Gorbatchev's syntax highlighter.<br>
	 * Works with only following tags regex:
	 * <b>[code=(java|js|sql|xml)][/code]</b><br>
	 * Nested tags will result in wrong formatting and improper rendering in UI.
	 * 
	 * @param jsonString
	 * @return parsed HTML String
	 */
	public static String parseJsontoHTML(String jsonString) {
		
		if (jsonString == null || jsonString.isEmpty())
			return jsonString;
		
		/*
		 * 1. replace all newline characters with <br> tags, not inside code tags,
		 * and replace < and > with &lt; and &gt; within code tags.
		 * The lazy match *? allows us to separate multiple code snippets
		 */
		String regex1="\\[code=(java|js|sql|xml)].*?\\[/code]";
		Pattern pattern = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(jsonString);
		int start=0, end=0;
		String mod = "";
		
		if(!matcher.find()) {
			mod = jsonString.replaceAll("\n", "<br>");
			return mod;
		}
		else {
			matcher.reset();
		}
		
		while(matcher.find()){
			end = matcher.start();
			mod = mod + jsonString.substring(start,end).replaceAll("\n", "<br>");
			mod = mod + matcher.group().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
			//the end index for current processing becomes start point for next processing
			start = matcher.end();
		}
		if (start < jsonString.length())
			mod = mod + jsonString.substring(start);
		
		/*
		 * 2. Replace all opening code tags with 'pre' counterpart
		 */
		String regex2 = "\\[code=(java|js|sql|xml)]";
		Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);
		Matcher matcher2 = pattern2.matcher(mod);
		
		while(matcher2.find()){
			mod = mod.replace(matcher2.group(), "<pre class=\"brush: " + matcher2.group(1)+"\">");
		
		}
		
		/*
		 * 3. Replace all closing code tags with 'pre' counterpart
		 */
		mod = mod.replaceAll("\\[/code]", "</pre>");

		return mod;
	}
	
}
