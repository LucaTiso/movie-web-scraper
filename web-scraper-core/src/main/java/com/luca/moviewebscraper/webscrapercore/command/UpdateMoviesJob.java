package com.luca.moviewebscraper.webscrapercore.command;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.executor.ScrapingExecutorService;
import com.luca.moviewebscraper.webscrapercore.service.MovieStoreService;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

@Component
public class UpdateMoviesJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateMoviesJob.class);

	private final MovieStoreService movieStoreService;
	
	private final ScrapingExecutorService executorService;

	private List<MovieIdAndUrl> updateUrlList;

	public UpdateMoviesJob(MovieStoreService movieStoreService,ScrapingExecutorService executorService) {
		this.movieStoreService = movieStoreService;
		this.executorService=executorService;

	}

	public List<MovieIdAndUrl> getUpdateUrlList() {
		return updateUrlList;
	}
	
	public void setUpdateUrlList(List<MovieIdAndUrl> updateUrlList) {
		this.updateUrlList = updateUrlList;
	}

	public void execute() {
		
		LOGGER.info("Start update job");
		
		updateUrlList = this.movieStoreService.retrieveAllUrl();
		
		updateUrlList.forEach(u->{
			LOGGER.info("url to update : {}",u);
		});
		
		executorService.esecuteUpdate(updateUrlList);

	}

}
