package com.luca.moviewebscraper.webscrapercore.command;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

	private final ScrapingExecutorService scrapingExecutorService;

	public InsertMoviesJob(MovieStoreService movieStoreService, ScrapingExecutorService scrapingExecutorService) {
		this.movieStoreService = movieStoreService;
		this.scrapingExecutorService = scrapingExecutorService;
	}

	public void execute() {

		LOGGER.info("Start insert job");

		//Document document = null;

		Set<String> insertUrlSet = new HashSet<>();

		try {
	
			List<String> subXmlList=new ArrayList<>();
			for(int i=1;i<=261;i++) {
				subXmlList.add("https://www.metacritic.com/movies/"+i+".xml");
			}

			for (String e : subXmlList) {
				
				String xmlUrlString=e;
				
				LOGGER.info(xmlUrlString);

				Document subXmlDoument = null;

				subXmlDoument = Jsoup.connect(xmlUrlString).get();

				Elements subXmlElements = subXmlDoument.getElementsByTag("loc");
				
				List<String> urlList=subXmlElements.stream().map(el->el.text()).toList();
				

				List<String> newUrlList = movieStoreService
						.filterNewUlr(urlList);

				newUrlList.forEach(u -> {
					LOGGER.info("new url found : {}", u);
				});

				insertUrlSet.addAll(newUrlList);

			}
			

			try(BufferedReader br=new BufferedReader(new FileReader("C:\\Users\\Utente\\Downloads\\movie_hrefs.txt"))){
				while(br.ready()) {
					
					String url=br.readLine().trim();
					List<String> tmpList=new ArrayList<>();
					tmpList.add(url);
					

					tmpList=movieStoreService
							.filterNewUlr(tmpList);
					
					if(tmpList.size()>0) {
						insertUrlSet.addAll(tmpList);
					}
					
					
				}
			}
			
			LOGGER.info("Searching movie set START");
			
			for(String m:insertUrlSet) {
				LOGGER.info(m);
			}
			LOGGER.info("Searching movie set END");
			
			

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
