package com.wongnai.interview.movie.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wongnai.interview.movie.external.MovieData;
import com.wongnai.interview.movie.external.MoviesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wongnai.interview.movie.Movie;
import com.wongnai.interview.movie.MovieSearchService;
import com.wongnai.interview.movie.external.MovieDataService;

@Component("simpleMovieSearchService")
public class SimpleMovieSearchService implements MovieSearchService {
	@Autowired
	private MovieDataService movieDataService;

	@Override
	public List<Movie> search(String queryText) {
		//TODO: Step 2 => Implement this method by using data from MovieDataService
		// All test in SimpleMovieSearchServiceIntegrationTest must pass.
		// Please do not change @Component annotation on this class

		// read json data from URL and convert to object of MoviesResponse
		MoviesResponse moviesResponse = movieDataService.fetchAll();

		List<Movie> movies = new ArrayList<Movie>();

		// declare pattern for search movies
		String pattern = "\\b" + queryText + "\\b";

		Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);

		// Search movies by given query text
		for(MovieData movieData: moviesResponse){
			String movieTitle = movieData.getTitle();
			Matcher matcher = p.matcher(movieTitle);

			// Search movies by query word pattern and
			// check full movies name must not equal to given query text
			if( matcher.find() && (!movieTitle.equalsIgnoreCase(queryText) ) ){

				// Construct Movie object and set data of movie found
				Movie movie = new Movie( movieTitle );
				movie.setActors( movieData.getCast() );

				// Add movie found to list for return
				movies.add(movie);
			}
		}

		return movies;
	}
}
