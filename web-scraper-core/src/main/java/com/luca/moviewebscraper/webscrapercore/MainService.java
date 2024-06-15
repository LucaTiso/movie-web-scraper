package com.luca.moviewebscraper.webscrapercore;

import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.command.InsertMoviesJob;
import com.luca.moviewebscraper.webscrapercore.command.UpdateMoviesJob;

@Component
public class MainService {

	private final UpdateMoviesJob updateMoviesCommand;
	private final InsertMoviesJob insertMoviesCommand;
	

	public MainService(UpdateMoviesJob updateMoviesCommand, InsertMoviesJob insertMoviesCommand) {

		this.updateMoviesCommand = updateMoviesCommand;
		this.insertMoviesCommand = insertMoviesCommand;

	}

	public void execute() {

		this.updateMoviesCommand.execute();
		this.insertMoviesCommand.execute();
		

	}

}
