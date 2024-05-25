package com.luca.moviewebscraper.webscrapercore.command;

import java.util.List;

import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

public interface MovieScrapingCommand {
	
	public void execute(List<MovieIdAndUrl> movieUrlList);

}
