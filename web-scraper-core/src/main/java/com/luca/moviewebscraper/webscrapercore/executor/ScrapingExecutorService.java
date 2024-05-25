package com.luca.moviewebscraper.webscrapercore.executor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.luca.moviewebscraper.webscrapercore.command.MovieInsertCommand;
import com.luca.moviewebscraper.webscrapercore.command.MovieUpdateCommand;
import com.luca.moviewebscraper.webscrapercore.task.MovieParseTask;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

@Component
public class ScrapingExecutorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingExecutorService.class);

	private ExecutorService executorService;

	private final MovieInsertCommand movieInsertCommand;
	
	private final MovieUpdateCommand movieUpdateCommand;

	private final int threadPoolSize = 10;

	public ScrapingExecutorService(MovieInsertCommand movieInsertCommand, MovieUpdateCommand movieUpdateCommand) {
		this.movieInsertCommand = movieInsertCommand;
		this.movieUpdateCommand = movieUpdateCommand;
	}

	public void executeInsert(List<MovieIdAndUrl> insertUrlList) {
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);

		List<List<MovieIdAndUrl>> partitioned = Lists.partition(insertUrlList, 50);

		partitioned.forEach(p -> {
			executorService.execute(new MovieParseTask(movieInsertCommand, p));
		});

		awaitTermination();
	}

	public void esecuteUpdate(List<MovieIdAndUrl> updateUrlList) {
		this.executorService = Executors.newFixedThreadPool(threadPoolSize);
		List<List<MovieIdAndUrl>> partitioned = Lists.partition(updateUrlList, 50);

		partitioned.forEach(p -> {
			executorService.execute(new MovieParseTask(movieUpdateCommand, p));
		});

		awaitTermination();
	}

	private void awaitTermination() {
		try {
			executorService.shutdown();
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			LOGGER.error("Errore InterruptedException : ", e1);

		}
	}

}
