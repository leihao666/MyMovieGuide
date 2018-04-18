package com.marklei.mymovieguide.network;

import com.marklei.mymovieguide.network.wrapper.MoviesWraper;
import com.marklei.mymovieguide.network.wrapper.ReviewsWrapper;
import com.marklei.mymovieguide.network.wrapper.VideoWrapper;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ivan on 8/20/2017.
 */

public interface TmdbWebService {

    @GET("3/discover/movie?language=zh&sort_by=popularity.desc")
    Flowable<MoviesWraper> popularMovies();

    @GET("3/discover/movie?vote_count.gte=500&language=zh&sort_by=vote_average.desc")
    Flowable<MoviesWraper> highestRatedMovies();

    @GET("3/movie/{movieId}/videos")
    Observable<VideoWrapper> trailers(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewsWrapper> reviews(@Path("movieId") String movieId);

}
