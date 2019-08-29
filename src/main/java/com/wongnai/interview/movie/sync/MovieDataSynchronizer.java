package com.wongnai.interview.movie.sync;

import javax.transaction.Transactional;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.MovieRepository;
import com.wongnai.interview.movie.external.MovieDataService;

@Component
public class MovieDataSynchronizer {
	@Autowired
	private MovieDataService movieDataService;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private MovieInvertedIndex movieInvertedIndex;

	@Transactional
	public void forceSync() {
		//TODO: implement this to sync movie into repository

		// Get movies data response
		MoviesResponse moviesResponse = movieDataService.fetchAll();

		// Loop for sync movies into repository and make movie inverted index
		for (MovieData movieData : moviesResponse) {

			// Construct movie and set data
			Movie movie = new Movie(movieData.getTitle());
			movie.setActors(movieData.getCast());

			// sync each movie into repository
			Movie saved = movieRepository.save(movie);

			// make movie inverted index
			movieInvertedIndex.addMovieTermToMovieIds(saved);
		}
	}
}
