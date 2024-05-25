package com.luca.moviewebscraper.webscraperdata.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.luca.moviewebscraper.webscraperdata.model.MovieDetail;
import com.luca.moviewebscraper.webscraperdata.model.MovieIdAndUrl;
import com.luca.moviewebscraper.webscraperdata.model.UserReviewDetail;

@Repository
public class MovieStoreRepository {

	private final JdbcTemplate jdbcTemplate;

	public MovieStoreRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void store(List<MovieDetail> movieDetailList) {

		String query = "SELECT nextval('MOVIE_NEW_SEQ') AS sequence_value FROM generate_series(1, ?)";

		List<Long> idList = jdbcTemplate.queryForList(query, Long.class, movieDetailList.size());

		for (int i = 0; i < movieDetailList.size(); i++) {
			movieDetailList.get(i).setId(idList.get(i));
		}

		jdbcTemplate.batchUpdate(
				"INSERT INTO MOVIE_NEW (ID, DURATION, METASCORE, HREF, METASCORENUMRATINGS, YEAR, USERRATING, USERNUMRATINGS, MOVIECAST, TITLE, PLOT, REGIA, PRODUCTION, GENRE, MOVIERATINGCATEGORY ) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				movieDetailList, movieDetailList.size(), (PreparedStatement ps, MovieDetail m) -> {
					ps.setLong(1, m.getId());
					ps.setObject(2, m.getDuration(), java.sql.Types.INTEGER);
					ps.setObject(3, m.getMetascore(), java.sql.Types.INTEGER);
					ps.setString(4, m.getHref());
					ps.setInt(5, m.getMetascoreNumRatings());
					ps.setObject(6, m.getYear(), java.sql.Types.INTEGER);
					ps.setObject(7, m.getUserRating(), java.sql.Types.FLOAT);
					ps.setInt(8, m.getUserNumRatings());
					ps.setString(9, m.getCast());
					ps.setString(10, m.getTitle());
					ps.setString(11, m.getPlot());
					ps.setString(12, m.getRegia());
					ps.setString(13, m.getProduction());
					ps.setString(14, m.getGenre());
					ps.setString(15, m.getMovieRatingCategory());
				});

	}

	public void storeReview(List<UserReviewDetail> userReviewDetailList) {

		String query = "SELECT nextval('metacritic_user_review_seq') AS sequence_value FROM generate_series(1, ?)";

		List<Long> idList = jdbcTemplate.queryForList(query, Long.class, userReviewDetailList.size());

		for (int i = 0; i < userReviewDetailList.size(); i++) {
			userReviewDetailList.get(i).setId(idList.get(i));
		}

		jdbcTemplate.batchUpdate(
				"INSERT INTO metacritic_user_review (ID, RATING, REVIEWDATE, MOVIE_ID, TEXT, USERNAME) VALUES (?,?,?,?,?,?)",
				userReviewDetailList, userReviewDetailList.size(), (PreparedStatement ps, UserReviewDetail m) -> {
					ps.setLong(1, m.getId());
					ps.setInt(2, m.getRating());
					ps.setObject(3, m.getReviewDate(), java.sql.Types.DATE);
					ps.setLong(4, m.getMovieId());
					ps.setString(5, m.getText());
					ps.setString(6, m.getUsername());

				});

	}
	
	

	public void updateReviewList(List<UserReviewDetail> userReviewDetailList) {

		String sql = "UPDATE metacritic_user_review SET RATING =?, REVIEWDATE =?, TEXT =? WHERE ID =?";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UserReviewDetail m = userReviewDetailList.get(i);
				ps.setInt(1, m.getRating());
				ps.setObject(2, m.getReviewDate(), java.sql.Types.DATE);
				ps.setString(3, m.getText());
				ps.setLong(4, m.getId());
			}

			@Override
			public int getBatchSize() {
				return userReviewDetailList.size();
			}
		});

	}

	public void updateList(List<MovieDetail> movieDetailList) {
		String sql = "UPDATE MOVIE_NEW SET DURATION =?, METASCORE =?, METASCORENUMRATINGS =?, YEAR =?, USERRATING =?, USERNUMRATINGS =?, "
				+ "TITLE =?, PRODUCTION =?, REGIA =?, GENRE =?, MOVIERATINGCATEGORY =? WHERE HREF =?";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				MovieDetail m = movieDetailList.get(i);

				ps.setObject(1, m.getDuration(), java.sql.Types.INTEGER);
				ps.setObject(2, m.getMetascore(), java.sql.Types.INTEGER);
				ps.setInt(3, m.getMetascoreNumRatings());
				ps.setObject(4, m.getYear(), java.sql.Types.INTEGER);
				ps.setObject(5, m.getUserRating(), java.sql.Types.FLOAT);
				ps.setInt(6, m.getUserNumRatings());
				ps.setString(7, m.getTitle());
				ps.setString(8, m.getProduction());
				ps.setString(9, m.getRegia());
				ps.setString(10, m.getGenre());
				ps.setString(11, m.getMovieRatingCategory());
				ps.setString(12, m.getHref());

			}

			@Override
			public int getBatchSize() {
				return movieDetailList.size();
			}

		});

	}

	public List<MovieIdAndUrl> findAllIdAndUrl() {
		String sql = "SELECT ID, HREF FROM MOVIE_NEW";
		return jdbcTemplate.query(sql, (resultSet, numRow) -> {
			MovieIdAndUrl obj = new MovieIdAndUrl();
			obj.setId(resultSet.getLong("ID"));
			obj.setUrl(resultSet.getString("HREF"));
			return obj;
		});
	}

	public boolean checkUrlIsNew(String movieHref) {
		String sql = "SELECT ID FROM MOVIE_NEW WHERE HREF =?";

		return jdbcTemplate.queryForList(sql, Long.class, movieHref).isEmpty();
	}
	
	public Long selectMovieId(String movieHref) {
		String sql = "SELECT ID FROM MOVIE_NEW WHERE HREF =?";

		return jdbcTemplate.queryForList(sql, Long.class, movieHref).get(0);
	}

	public LocalDate retrieveLastReviewDate(Long movieId) {

		String sql = "SELECT max(REVIEWDATE) FROM METACRITIC_USER_REVIEW WHERE MOVIE_ID =?";

		List<LocalDate> localDateList = jdbcTemplate.queryForList(sql, LocalDate.class, movieId);
		LocalDate selectedDate = null;

		if (localDateList != null && localDateList.size() > 0) {
			selectedDate = localDateList.get(0);
		}

		return selectedDate;
	}

	public Long retrievePreviousReview(Long movieId, String username) {
		String sql = "SELECT ID FROM METACRITIC_USER_REVIEW WHERE MOVIE_ID =? AND USERNAME =?";

		List<Long> idList = jdbcTemplate.queryForList(sql, Long.class, movieId, username);

		Long retrievedId = null;

		if (idList != null && idList.size() > 0) {
			retrievedId = idList.get(0);
		}

		return retrievedId;
	}

}
