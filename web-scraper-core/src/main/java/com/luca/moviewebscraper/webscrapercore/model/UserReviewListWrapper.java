package com.luca.moviewebscraper.webscrapercore.model;

import java.util.List;

import com.luca.moviewebscraper.webscraperdata.model.UserReviewDetail;

public class UserReviewListWrapper {

	private List<UserReviewDetail> reviewsToInsert;

	private List<UserReviewDetail> reviewsToUpdte;
	
	private String url;

	public List<UserReviewDetail> getReviewsToInsert() {
		return reviewsToInsert;
	}

	public void setReviewsToInsert(List<UserReviewDetail> reviewsToInsert) {
		this.reviewsToInsert = reviewsToInsert;
	}

	public List<UserReviewDetail> getReviewsToUpdte() {
		return reviewsToUpdte;
	}

	public void setReviewsToUpdte(List<UserReviewDetail> reviewsToUpdte) {
		this.reviewsToUpdte = reviewsToUpdte;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
