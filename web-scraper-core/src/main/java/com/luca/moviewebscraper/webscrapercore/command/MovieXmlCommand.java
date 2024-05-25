package com.luca.moviewebscraper.webscrapercore.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.task.MovieParseTask;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

@Component
public class MovieXmlCommand {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieXmlCommand.class);

	private final MovieInsertCommand movieParsingCommand;

	private final String mainXmlPage = "https://www.metacritic.com/movies.xml";

	private final ExecutorService executorService;

	public MovieXmlCommand(MovieInsertCommand movieParsingCommand) {
		this.movieParsingCommand = movieParsingCommand;
		this.executorService = Executors.newFixedThreadPool(10);
	}

	public void execute() {

		LOGGER.info("Movie parser started");

		Document document = null;

		List<MovieParseTask> taskList = new ArrayList<>();

		try {
			document = Jsoup.connect(this.mainXmlPage).get();

			Elements mainXmlElements = document.getElementsByTag("loc");
			LOGGER.info("Main xml elements list : ");

			List<String> subXmlList = mainXmlElements.stream().map(Element::text).toList();

			for (String e : subXmlList) {
				LOGGER.info(e);

				Document subXmlDoument = null;

				subXmlDoument = Jsoup.connect(e).get();

				Elements subXmlElements = subXmlDoument.getElementsByTag("loc");

				List<String> movieHrefList = subXmlElements.stream().map(Element::text).toList();
				
				MovieIdAndUrl idAndUrl = new MovieIdAndUrl();
				
				List<MovieIdAndUrl> idAndUrlList=new ArrayList<>();
				
				for(String s:movieHrefList) {
					MovieIdAndUrl u=new MovieIdAndUrl();
					
					u.setId(null);
					u.setUrl(s);
					idAndUrlList.add(u);
				}

				MovieParseTask task = new MovieParseTask(movieParsingCommand, idAndUrlList);

				taskList.add(task);
			}
			LOGGER.info("Starting tasks");

			for (MovieParseTask task : taskList) {
				this.executorService.execute(task);
			}

			try {
				executorService.shutdown();
				executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				LOGGER.error("Errore InterruptedException : ", e1);

			}

		} catch (IOException e) {

			LOGGER.error("Cannot get main xml : ", e);

		}

	}

}
