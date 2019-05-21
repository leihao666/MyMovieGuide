package com.marklei.mymovieguide.shared;

/**
 * @author arun
 */
public class Api {
    public static final String BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w342";
    public static final String BASR_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780";
    public static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=%1$s";
    public static final String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%1$s/0.jpg";

    private Api() {
        // hide implicit public constructor
    }

    public static String getPosterPath(String posterPath) {
        return BASE_POSTER_PATH + posterPath;
    }

    public static String getBackdropPath(String backdropPath) {
        return BASR_BACKDROP_PATH + backdropPath;
    }
}
