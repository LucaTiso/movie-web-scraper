package com.luca.moviewebscraper.webscrapercore.command;

import java.io.IOException;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.model.MovieRatingSet;
import com.luca.moviewebscraper.webscrapercore.utils.ParsingUtils;
import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;

@Component
public class MovieDetailCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieDetailCommand.class);

	public MovieDetail execute(String movieHref) {

		LOGGER.info("Start processing movie page: {}", movieHref);

		Document document = null;
		try {
			document = Jsoup.connect(movieHref).get();
		} catch (IOException e) {

			LOGGER.error("cannot get movie page: {}", movieHref);
			return null;
		}

		Set<String> movieRatingSet = MovieRatingSet.getMovieRatings();

		MovieDetail movieDetail = new MovieDetail();

		movieDetail.setHref(movieHref);

		Element movieHeader;

		try {
			movieHeader = document.getElementsByClass("c-heroMetadata").get(0);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.error("cannot get c-heroMetadata for movie : {}", movieHref);
			return null;
		}

		Elements headerElements = movieHeader.getElementsByTag("span");

		Element titleElement = document.getElementsByClass("c-productHero_title").get(0);

		Elements reviewsNumberElement = document.getElementsByClass("c-productScoreInfo_reviewsTotal");

		Elements reviewScoreElements = document.getElementsByClass("c-productScoreInfo_scoreNumber");

		Element plotElement = document.getElementsByClass("c-productDetails_description").get(0);

		Element genreListElement = document.getElementsByClass("c-genreList").get(0);

		Elements castElements = document.getElementsByClass("c-globalPersonCard_name");

		if (headerElements.size() == 4) {
			movieDetail.setYear(Integer.parseInt(headerElements.get(0).text()));
			movieDetail.setMovieRatingCategory(headerElements.get(1).text());
			movieDetail.setProduction(headerElements.get(2).text());
			int duration = ParsingUtils.parseDuration(headerElements.get(3).text());
			movieDetail.setDuration(duration);
		} else if (headerElements.size() == 3) {
			try {
				movieDetail.setYear(Integer.parseInt(headerElements.get(0).text()));
			} catch (NumberFormatException exception) {
				// movie with
				movieDetail.setYear(null);
				movieDetail.setMovieRatingCategory(headerElements.get(0).text());
			}

			if (movieRatingSet.contains(headerElements.get(1).text())) {
				movieDetail.setMovieRatingCategory(headerElements.get(1).text());
			} else {
				movieDetail.setProduction(headerElements.get(1).text());
			}

			Integer duration = null;
			try {
				duration = ParsingUtils.parseDuration(headerElements.get(2).text());
			} catch (Exception e) {
				duration = null;
			}

			if (duration != null) {
				movieDetail.setDuration(duration);
			} else {
				movieDetail.setProduction(headerElements.get(2).text());
			}

		} else if (headerElements.size() == 2) {

			try {
				movieDetail.setYear(Integer.parseInt(headerElements.get(0).text()));
			} catch (Exception e) {

				if (movieRatingSet.contains(headerElements.get(0).text())) {
					movieDetail.setMovieRatingCategory(headerElements.get(0).text());
				} else {
					movieDetail.setProduction(headerElements.get(0).text());
				}

			}

			Integer duration = null;
			try {
				duration = ParsingUtils.parseDuration(headerElements.get(1).text());

			} catch (Exception e) {
				duration = null;
			}

			if (duration != null) {
				movieDetail.setDuration(duration);
			} else {
				if (movieRatingSet.contains(headerElements.get(1).text())) {
					movieDetail.setMovieRatingCategory(headerElements.get(1).text());
				} else {
					movieDetail.setProduction(headerElements.get(1).text());
				}

			}

		} else {
			try {
				movieDetail.setYear(Integer.parseInt(headerElements.get(0).text()));
			} catch (Exception e) {
				Integer duration = null;
				try {
					duration = ParsingUtils.parseDuration(headerElements.get(0).text());
				} catch (Exception e2) {
					duration = null;
				}

				if (duration != null) {
					movieDetail.setDuration(duration);
				} else {
					if (movieRatingSet.contains(headerElements.get(0).text())) {
						movieDetail.setMovieRatingCategory(headerElements.get(0).text());
					} else {
						movieDetail.setProduction(headerElements.get(0).text());
					}

				}
			}
		}

		movieDetail.setTitle(titleElement.text());

		reviewsNumberElement.forEach(e -> {

			try {

				ParsingUtils.parseNumRatings(movieDetail, e.text());
			} catch (Exception exception) {
				LOGGER.info("Cannot parse num rating for text : {}", e.text());
			}

		});

		reviewScoreElements.forEach(e -> {

			try {

				ParsingUtils.parseRatings(movieDetail, e);
			} catch (Exception exception) {
				LOGGER.info("Cannot parse rating for text : {}", e.text());
			}
		});

		movieDetail.setPlot(plotElement.text());

		movieDetail.setGenre(genreListElement.text());

		try {

			Element staffElement = document.getElementsByClass("c-productDetails_staff").get(0);
			movieDetail.setRegia(ParsingUtils.parseRegia(staffElement.text()));
		} catch (IndexOutOfBoundsException e) {
			LOGGER.info("missing regia element for href : {}", movieHref);
		}

		StringBuilder stringBuilder = new StringBuilder("");

		castElements.forEach(e -> {

			stringBuilder.append(e.text() + ", ");

		});

		String castString = stringBuilder.toString();
		if (castString.length() > 1) {
			castString = castString.substring(0, castString.length() - 2);
		}

		movieDetail.setCast(castString);

		LOGGER.info("Movie page: {} processed succesfully", movieHref);
		LOGGER.info("MovieDetailCommand output for movieHref {} : ", movieHref, movieDetail.toString());

		return movieDetail;

	}

}
