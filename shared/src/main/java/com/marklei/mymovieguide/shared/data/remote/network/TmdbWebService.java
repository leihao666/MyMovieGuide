package com.marklei.mymovieguide.shared.data.remote.network;

import com.marklei.mymovieguide.shared.data.remote.network.wrapper.MoviesWrapper;
import com.marklei.mymovieguide.shared.data.remote.network.wrapper.ReviewsWrapper;
import com.marklei.mymovieguide.shared.data.remote.network.wrapper.VideoWrapper;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbWebService {

    @GET("3/discover/movie?language=zh&sort_by=popularity.desc")
    Flowable<MoviesWrapper> popularMovies(@Query("page") int page);

    @GET("3/discover/movie?vote_count.gte=500&language=zh&sort_by=vote_average.desc")
    Flowable<MoviesWrapper> highestRatedMovies(@Query("page") int page);

    @GET("3/movie/{movieId}/videos")
    Flowable<VideoWrapper> trailers(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Flowable<ReviewsWrapper> reviews(@Path("movieId") String movieId);

}
