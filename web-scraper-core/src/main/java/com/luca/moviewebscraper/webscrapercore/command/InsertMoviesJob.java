package com.luca.moviewebscraper.webscrapercore.command;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.executor.ScrapingExecutorService;
import com.luca.moviewebscraper.webscrapercore.service.MovieStoreService;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;

@Component
public class InsertMoviesJob {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertMoviesJob.class);

	private final MovieStoreService movieStoreService;

	private final String mainXmlPage = "https://www.metacritic.com/movies.xml";

	private final ScrapingExecutorService scrapingExecutorService;

	public InsertMoviesJob(MovieStoreService movieStoreService, ScrapingExecutorService scrapingExecutorService) {
		this.movieStoreService = movieStoreService;
		this.scrapingExecutorService = scrapingExecutorService;
	}

	public void execute() {

		LOGGER.info("Start insert job");

		Document document = null;

		Set<String> insertUrlSet = new HashSet<>();

		try {

			document = Jsoup.connect(this.mainXmlPage).get();

			Elements mainXmlElements = document.getElementsByTag("loc");
			LOGGER.info("Main xml elements list : ");

			List<String> subXmlList = mainXmlElements.stream().map(Element::text).toList();

			for (String e : subXmlList) {
				
				String xmlUrlString="https://"+e.split(" ")[1];
				
				LOGGER.info(xmlUrlString);

				Document subXmlDoument = null;

				subXmlDoument = Jsoup.connect(xmlUrlString).get();

				Elements subXmlElements = subXmlDoument.getElementsByTag("loc");
				
				List<String> urlList=subXmlElements.stream().map(el->"https://"+el.text().split(" ")[1]).toList();
				

				List<String> newUrlList = movieStoreService
						.filterNewUlr(urlList);

				newUrlList.forEach(u -> {
					LOGGER.info("new url found : {}", u);
				});

				insertUrlSet.addAll(newUrlList);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Cannot get xml : ", e);
		}

		insertUrlSet.forEach(u -> {
			LOGGER.info("url in insert list : {}", u);
		});

		scrapingExecutorService.executeInsert(insertUrlSet.stream().map(s -> new MovieIdAndUrl(null, s)).toList());

	}

}
