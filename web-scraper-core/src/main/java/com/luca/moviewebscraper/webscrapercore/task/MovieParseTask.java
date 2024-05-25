package com.luca.moviewebscraper.webscrapercore.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luca.moviewebscraper.webscrapercore.command.MovieScrapingCommand;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

public class MovieParseTask implements Runnable{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MovieParseTask.class);
	
	private final  MovieScrapingCommand movieScrapingCommand;
	private final List<MovieIdAndUrl> movieHrefList;
	
	
	public MovieParseTask(MovieScrapingCommand movieScrapingCommand,List<MovieIdAndUrl> movieHrefList){
		this.movieScrapingCommand=movieScrapingCommand;
		this.movieHrefList=movieHrefList;
	}

	@Override
	public void run() {
		
		try {
			this.movieScrapingCommand.execute(movieHrefList);
		}catch(Exception e) {
			LOGGER.error("unhandled exception : ",e);
		}
	}

}
