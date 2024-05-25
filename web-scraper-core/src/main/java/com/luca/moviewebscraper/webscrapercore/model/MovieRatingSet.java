package com.luca.moviewebscraper.webscrapercore.model;

import java.util.HashSet;
import java.util.Set;

public class MovieRatingSet {
	
	private static Set<String> movieRatings=new HashSet<>();
	
	
	static {
		movieRatings.add("R");
		movieRatings.add("TV-MA");
		movieRatings.add("PG-13");
		movieRatings.add("TV-PG");
		movieRatings.add("Not Rated");
		movieRatings.add("PG");
		movieRatings.add("TV-14");
		
	}
	
	private MovieRatingSet() {}
	
	
	
	public static Set<String> getMovieRatings() {
		return movieRatings;
	}

}
