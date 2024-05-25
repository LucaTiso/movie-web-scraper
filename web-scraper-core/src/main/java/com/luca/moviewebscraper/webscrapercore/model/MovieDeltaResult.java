package com.luca.moviewebscraper.webscrapercore.model;

import java.util.ArrayList;
import java.util.List;

import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;

public class MovieDeltaResult {

	public List<MovieDetail> insertList;

	public List<MovieDetail> updateList;

	public MovieDeltaResult() {
		this.insertList = new ArrayList<>();
		this.updateList = new ArrayList<>();
	}

	public List<MovieDetail> getInsertList() {
		return insertList;
	}

	public List<MovieDetail> getUpdateList() {
		return updateList;
	}

	public void addToInsert(MovieDetail movieDetail) {
		this.insertList.add(movieDetail);
	}

	public void addToUpdate(MovieDetail movieDetail) {
		this.updateList.add(movieDetail);
	}

}
