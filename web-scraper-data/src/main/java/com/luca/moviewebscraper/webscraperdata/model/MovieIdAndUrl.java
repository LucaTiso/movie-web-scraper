package com.luca.moviewebscraper.webscraperdata.model;

public class MovieIdAndUrl {

	private Long id;
	
	private String url;

	public MovieIdAndUrl(Long id, String url) {
		this.id = id;
		this.url = url;
	}
	
	public MovieIdAndUrl() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
