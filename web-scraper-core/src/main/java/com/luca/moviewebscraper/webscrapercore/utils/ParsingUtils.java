package com.luca.moviewebscraper.webscrapercore.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;

public class ParsingUtils {

	private ParsingUtils() {
	}
	
	public static Map<String,Integer> monthMap=new HashMap<>();
	
	
	static {
		monthMap.put("JAN", 1);
		monthMap.put("FEB", 2);
		monthMap.put("MAR", 3);
		monthMap.put("APR", 4);
		monthMap.put("MAY", 5);
		monthMap.put("JUN", 6);
		monthMap.put("JUL", 7);
		monthMap.put("AUG", 8);
		monthMap.put("SEP", 9);
		monthMap.put("OCT", 10);
		monthMap.put("NOV", 11);
		monthMap.put("DEC", 12);
	}
	
	

	public static int parseDuration(String durationText) {
		String[] arr = durationText.split(" ");

		int minutes = 0;

		if (arr.length == 4) {
			minutes = Integer.parseInt(arr[0]) * 60;
			minutes += Integer.parseInt(arr[2]);
		} else if (arr[1].contentEquals("h")) {
			minutes = Integer.parseInt(arr[0]) * 60;
		} else if (arr[1].contentEquals("m")) {
			minutes += Integer.parseInt(arr[0]);
		}

		return minutes;
	}

	public static void parseNumRatings(MovieDetail movieDetail, String text) {
		String[] arr = text.split(" ");
		
			if (arr[3].contentEquals("Critic")) {
				movieDetail.setMetascoreNumRatings(Integer.parseInt(arr[2]));
			} else if (arr[3].contentEquals("User")) {
				
				if(arr[2].contains(",")) {
					movieDetail.setUserNumRatings(Integer.parseInt(arr[2].replace(",", "")));
				}else {
					movieDetail.setUserNumRatings(Integer.parseInt(arr[2]));
				}
				
				
			}
		
		
		
	}

	public static void parseRatings(MovieDetail movieDetail, Element element) {
		if (element.html().contains("Metascore")) {
			movieDetail.setMetascore(Integer.parseInt(element.text()));
		} else if (element.html().contains("User")) {
			if(!element.text().contentEquals("tbd")) {
				movieDetail.setUserRating(Float.parseFloat(element.text()));
			}
			
		}
	}

	public static String parseRegia(String text) {
		if (text.contains(" Written By:")) {
			text = text.split(" Written By:")[0];

		}
		text = text.replace("Directed By: ", "");
		return text;
	}
	
	public static LocalDate parseReviewDate(String stringDate) {
		
		String[] arr=stringDate.split(" ");
		Integer month=monthMap.get(arr[0]);
		
		Integer day = Integer.parseInt(arr[1].substring(0,arr[1].length()-1));
		
		Integer year = Integer.parseInt(arr[2]);
		
		LocalDate date=LocalDate.of(year, month, day);
		
		return date;
		
	}
}
