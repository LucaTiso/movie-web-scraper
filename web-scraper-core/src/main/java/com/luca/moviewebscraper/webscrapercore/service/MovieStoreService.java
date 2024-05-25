package com.luca.moviewebscraper.webscrapercore.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;
import com.luca.moviewebscraper.webscraperdata.model.UserReviewDetail;
import com.luca.moviewebscraper.webscraperdata.repository.MovieStoreRepository;

@Service
public class MovieStoreService {

	private MovieStoreRepository movieStoreRepository;

	public MovieStoreService(MovieStoreRepository movieStoreRepository) {
		this.movieStoreRepository = movieStoreRepository;
	}

	@Transactional
	public void storeMovieList(List<MovieDetail> movieDetailList) {
		movieStoreRepository.store(movieDetailList);
	}

	@Transactional
	public void updateMovieList(List<MovieDetail> movieDetailList) {

		movieStoreRepository.updateList(movieDetailList);
	}

	@Transactional(readOnly = true)
	public List<MovieIdAndUrl> retrieveAllUrl() {
		return this.movieStoreRepository.findAllIdAndUrl();
	}

	@Transactional(readOnly = true)
	public List<String> filterNewUlr(List<String> subXmlUrlList) {

		return subXmlUrlList.stream().filter(this.movieStoreRepository::checkUrlIsNew).toList();

	}

	@Transactional(readOnly = true)
	public LocalDate retrieveLastReviewDate(Long movieId) {
		LocalDate lastReviewDate = null;
		if (movieId != null) {
			lastReviewDate = this.movieStoreRepository.retrieveLastReviewDate(movieId);
		}

		return lastReviewDate;
	}

	@Transactional(readOnly = true)
	public Long retrievePreviousReview(Long movieId, String username) {
		Long reviewId = null;
		if (movieId != null) {
			reviewId = this.movieStoreRepository.retrievePreviousReview(movieId, username);
		}

		return reviewId;
	}
	
	@Transactional
	public void insertAllUserReviews(List<UserReviewDetail> userReviewDetail) {
		this.movieStoreRepository.storeReview(userReviewDetail);
	}
	
	@Transactional
	public void updateAllUserReviews(List<UserReviewDetail> userReviewDetail) {
		this.movieStoreRepository.updateReviewList(userReviewDetail);
	}
	
	@Transactional(readOnly=true)
	public void assignMovieIdToReview(List<UserReviewDetail> userReviewDetailList,String href) {
		
		Long movieId=this.movieStoreRepository.selectMovieId(href);
		for(UserReviewDetail d:userReviewDetailList) {
			d.setMovieId(movieId);
		}
	}
}
