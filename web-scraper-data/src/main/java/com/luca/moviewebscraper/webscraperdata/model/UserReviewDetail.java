package com.luca.moviewebscraper.webscraperdata.model;

import java.time.LocalDate;

public class UserReviewDetail {
	
	private Long id;
	
	private Long movieId;
	
	private String username;
	
	private int rating;
	
	private LocalDate reviewDate;
	
	private String text;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDate getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(LocalDate reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "UserReviewDetail [id=" + id + ", movieId=" + movieId + ", username=" + username + ", rating=" + rating
				+ ", reviewDate=" + reviewDate + ", text=" + text + "]";
	}
	
	
	

}
