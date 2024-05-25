package com.luca.moviewebscraper.webscrapercore;

import org.springframework.stereotype.Component;

import com.luca.moviewebscraper.webscrapercore.command.InsertMoviesJob;
import com.luca.moviewebscraper.webscrapercore.command.UpdateMoviesJob;
import com.luca.moviewebscraper.webscrapercore.command.UserReviewCommand;

@Component
public class MainService {

	private final UpdateMoviesJob updateMoviesCommand;
	private final InsertMoviesJob insertMoviesCommand;
	private final UserReviewCommand userReviewCommand;

	public MainService(UpdateMoviesJob updateMoviesCommand, InsertMoviesJob insertMoviesCommand,UserReviewCommand userReviewCommand) {

		this.updateMoviesCommand = updateMoviesCommand;
		this.insertMoviesCommand = insertMoviesCommand;

		this.userReviewCommand = userReviewCommand;

	}

	public void execute() {

		//MovieIdAndUrl m = new MovieIdAndUrl();
		//m.setId(36762l);
		//m.setUrl("https://www.metacritic.com/movie/music-of-the-heart/");
		//userReviewCommand.execute(m);

		//this.updateMoviesCommand.execute();
		this.insertMoviesCommand.execute();
		

	}

}
