package com.luca.moviewebscraper.webscraperdata.model;

public class MovieDetail {
	
	private Long id;

	private String title;

	private String href;

	private Integer year;

	private Integer duration;

	private String genre;

	private Float userRating;

	private int userNumRatings;

	private String plot;

	private String regia; // director

	private String cast;

	private String production;

	private Integer metascore;

	private int metascoreNumRatings;
	
	private String movieRatingCategory;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getRegia() {
		return regia;
	}

	public void setRegia(String regia) {
		this.regia = regia;
	}

	public Integer getMetascore() {
		return metascore;
	}

	public void setMetascore(Integer metascore) {
		this.metascore = metascore;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Float getUserRating() {
		return userRating;
	}

	public void setUserRating(Float userRating) {
		this.userRating = userRating;
	}



	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public int getUserNumRatings() {
		return userNumRatings;
	}

	public void setUserNumRatings(int userNumRatings) {
		this.userNumRatings = userNumRatings;
	}

	public int getMetascoreNumRatings() {
		return metascoreNumRatings;
	}

	public void setMetascoreNumRatings(int metascoreNumRatings) {
		this.metascoreNumRatings = metascoreNumRatings;
	}
	
	

	public String getMovieRatingCategory() {
		return movieRatingCategory;
	}

	public void setMovieRatingCategory(String movieRatingCategory) {
		this.movieRatingCategory = movieRatingCategory;
	}

	@Override
	public String toString() {
		return "MovieDetail [id=" + id + ", title=" + title + ", href=" + href + ", year=" + year + ", duration="
				+ duration + ", genre=" + genre + ", userRating=" + userRating + ", userNumRatings=" + userNumRatings
				+ ", plot=" + plot + ", regia=" + regia + ", cast=" + cast + ", production=" + production
				+ ", metascore=" + metascore + ", metascoreNumRatings=" + metascoreNumRatings + ", movieRatingCategory="
				+ movieRatingCategory + "]";
	}

	
	

}
