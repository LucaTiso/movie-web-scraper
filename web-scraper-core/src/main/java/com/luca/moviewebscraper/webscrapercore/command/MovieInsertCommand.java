package com.luca.moviewebscraper.webscrapercore.command;

import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.luca.moviewebscraper.webscrapercore.model.UserReviewListWrapper;
import com.luca.moviewebscraper.webscrapercore.service.MovieStoreService;
import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

@Component
public class MovieInsertCommand implements MovieScrapingCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieInsertCommand.class);

	private final MovieDetailCommand movieDetailCommand;

	private final MovieStoreService movieStoreService;

	private final UserReviewCommand userReviewCommand;

	public MovieInsertCommand(MovieDetailCommand movieDetailCommand, MovieStoreService movieStoreService,
			UserReviewCommand userReviewCommand) {
		this.movieDetailCommand = movieDetailCommand;
		this.movieStoreService = movieStoreService;
		this.userReviewCommand = userReviewCommand;
	}

	@Override
	public void execute(List<MovieIdAndUrl> movieUrlList) {
		
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Utente\\OneDrive\\Desktop\\chromedriver-folder\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
		options.addArguments("--remote-allow-origins=*");
		
		ChromeDriver chromeDriver=new ChromeDriver(options);

		LOGGER.info("start processing sub");

		List<MovieDetail> movieDetailList = movieUrlList.stream().map(h -> movieDetailCommand.execute(h.getUrl()))
				.filter(m -> m != null).toList();

		List<UserReviewListWrapper> reviewListWrapper = movieUrlList.stream().map(h -> userReviewCommand.execute(h,chromeDriver))
				.toList();
		
		chromeDriver.quit();

		LOGGER.info("Start saving {} movies", movieDetailList.size());

		this.movieStoreService.storeMovieList(movieDetailList);
		
		for(UserReviewListWrapper w:reviewListWrapper) {
			if(w.getReviewsToInsert().size()>0) {
				this.movieStoreService.assignMovieIdToReview(w.getReviewsToInsert(), w.getUrl());
			}
			
		}

		for (UserReviewListWrapper wrapper : reviewListWrapper) {

			if (wrapper.getReviewsToInsert().size() > 0) {
				this.movieStoreService.insertAllUserReviews(wrapper.getReviewsToInsert());
			}
			if (wrapper.getReviewsToUpdte().size() > 0) {
				this.movieStoreService.insertAllUserReviews(wrapper.getReviewsToUpdte());
			}

		}

		LOGGER.info("Movies persisted");

	}

}
