package com.wongnai.interview.movie.sync;

import com.wongnai.interview.movie.Movie;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class MovieInvertedIndex {

    Map<String, List<Long>> movieTermToMoviesId = new HashMap<>();

    public void addMovieTermToMovieIds( Movie movie ){
        /*  This method will add movie term to movie id list
            Note: term is word of name separated by spaces
        */

        // split name by space
        String[] terms = movie.getName().split("\\s+");

        for( int i = 0; i <= terms.length - 1; i++) {
            String term = terms[i].toLowerCase();

            // get value by term
            List movieIds = this.movieTermToMoviesId.get(term);

            // if not found this term in map
            // we will map this term to empty list
            if (movieIds == null) {
                List<Long> ids = new ArrayList<Long>();
                this.movieTermToMoviesId.put(term, ids);
                movieIds = ids;
            }

            // add movie id to list of this term
            movieIds.add( movie.getId() );
            this.movieTermToMoviesId.put(term, movieIds);

        }
    }

    public Map<String, List<Long>> getMovieTermToMoviesId(){
         return this.movieTermToMoviesId;
    }

    private List<List> getMoviesIdListByTerms( String[] terms ){
        /*  This method will get movies id list from map
        */

        // loop terms for get movies id and add it to list.
        List<List> movieIdsByTerm = new ArrayList<List>();
        for( int i = 0; i <= terms.length - 1; i++) {

            String term = terms[i];

            // if not found movies id will skip
            List moviesId = this.movieTermToMoviesId.get(term.toLowerCase());
            if( moviesId == null ) {
                continue;
            }

            movieIdsByTerm.add(moviesId);
        }
        return movieIdsByTerm;
    }

    private List<Long> intersectionMoviesId( List<List> allMoviesIdList ){
        /*  get intersection of movies Id
        */

        List firstMovieId = allMoviesIdList.get(0);
        Set<Long> intersectionSet = new HashSet<>(firstMovieId);

        for( int i=1; i<allMoviesIdList.size();i++ ){
            Set<Long> setForCheck = new HashSet<>(allMoviesIdList.get(i));
            intersectionSet.retainAll( setForCheck );
        }

        List<Long> intersectionMoviesId = new ArrayList<>(intersectionSet);

        return intersectionMoviesId;

    }

    public List<Long> findMoviesIdByQueryText( String queryText ){

        // Spilt query text with space
        String[] term = queryText.split( "\\s+");

        // Get list of movies id by queryText split
        List<List> allMoviesIdList = this.getMoviesIdListByTerms( term );

        // Not found movies id of given query text will return empty list
        List<Long> moviesId = new ArrayList<>();
        if( allMoviesIdList.size() == 0 ){
            return moviesId;
        }

        // get intersection of movies id for each term
        moviesId = this.intersectionMoviesId( allMoviesIdList );

        return moviesId;
    }


}
