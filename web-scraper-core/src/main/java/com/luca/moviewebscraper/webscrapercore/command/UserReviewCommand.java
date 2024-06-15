package com.luca.moviewebscraper.webscrapercore.command;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.luca.moviewebscraper.webscrapercore.model.UserReviewListWrapper;
import com.luca.moviewebscraper.webscrapercore.service.MovieStoreService;
import com.luca.moviewebscraper.webscrapercore.utils.ParsingUtils;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;
import com.luca.moviewebscraper.webscraperdata.model.UserReviewDetail;

@Component
public class UserReviewCommand {

	private final MovieStoreService movieStoreService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserReviewCommand.class);

	public UserReviewCommand(MovieStoreService movieStoreService) {
		this.movieStoreService = movieStoreService;
	}

	public UserReviewListWrapper execute(MovieIdAndUrl movieIdAndUrl,ChromeDriver chromeDriver) {
		
		//String reviewUrl="https://www.metacritic.com/movie/music-of-the-heart/" + "user-reviews/";
		
		String reviewUrl = movieIdAndUrl.getUrl() + "user-reviews/";

		UserReviewListWrapper userReviewListWrapper = new UserReviewListWrapper();
		List<UserReviewDetail> userReviewDetailInsert = new ArrayList<>();

		List<UserReviewDetail> userReviewDetailUpdate = new ArrayList<>();
		
		try {
			
		
			
			chromeDriver.get(reviewUrl);
			
			System.out.println(reviewUrl);

			
			List<WebElement> webElements= chromeDriver.findElements(By.className("c-siteReview"));

			System.out.println("Size : "+webElements.size());
			
			LocalDate lastReviewDate = this.movieStoreService.retrieveLastReviewDate(movieIdAndUrl.getId());

			for (WebElement e : webElements) {

				UserReviewDetail userReviewDetail = new UserReviewDetail();
				userReviewDetail.setMovieId(movieIdAndUrl.getId());

				String score = e.findElement(By.className("c-siteReviewScore")).getText();

				userReviewDetail.setRating(Integer.parseInt(score));

				String username = e.findElement(By.className("c-siteReviewHeader_username")).getText();

				userReviewDetail.setUsername(username);

				String reviewDate = e.findElement(By.className("c-siteReviewHeader_reviewDate")).getText();

				userReviewDetail.setReviewDate(ParsingUtils.parseReviewDate(reviewDate));

				String text = e.findElement(By.className("c-siteReview_quote")).getText();

				userReviewDetail.setText(text);

				if (lastReviewDate == null) {

					userReviewDetailInsert.add(userReviewDetail);

				}

				else {

					// test for insert
					Long reviewId = this.movieStoreService.retrievePreviousReview(movieIdAndUrl.getId(),
							userReviewDetail.getUsername());

					if (reviewId != null) {
						userReviewDetail.setId(reviewId);
						userReviewDetailUpdate.add(userReviewDetail);
					} else {
						userReviewDetailInsert.add(userReviewDetail);
					}

				}

			}


			
			
			
		}catch(NoSuchElementException e) {
			LOGGER.error("No such element exception for url :"+reviewUrl);
		}catch(Exception e) {
			LOGGER.error("Unhandled selenium exception :",e);
		}
		
		
		userReviewListWrapper.setUrl(movieIdAndUrl.getUrl());
		userReviewListWrapper.setReviewsToInsert(userReviewDetailInsert);
		userReviewListWrapper.setReviewsToUpdte(userReviewDetailUpdate);

		return userReviewListWrapper;
	}

}
