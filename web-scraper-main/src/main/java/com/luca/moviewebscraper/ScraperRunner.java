package com.luca.moviewebscraper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.MainService;

@Component
public class ScraperRunner implements CommandLineRunner {

	private final MainService mainService;

	public ScraperRunner(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	public void run(String... args) throws Exception {
		
		mainService.execute();

	}

}
