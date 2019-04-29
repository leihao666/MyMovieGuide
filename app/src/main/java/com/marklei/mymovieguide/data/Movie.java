package com.marklei.mymovieguide.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.moshi.Json;

@Entity(tableName = "movies")
public final class Movie implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private String id;
    /* 概述 */
    @Nullable
    @ColumnInfo(name = "overview")
    private String overview;
    /* 发布日期 */
    @Nullable
    @ColumnInfo(name = "release_date")
    @Json(name = "release_date")
    private String releaseDate;
    /* 海报路径 */
    @Nullable
    @ColumnInfo(name = "poster_path")
    @Json(name = "poster_path")
    private String posterPath;
    /* 背景图片路径 */
    @Nullable
    @ColumnInfo(name = "backdrop_path")
    @Json(name = "backdrop_path")
    private String backdropPath;
    @Nullable
    @ColumnInfo(name = "title")
    private String title;
    /* 平均评分 */
    @ColumnInfo(name = "vote_average")
    @Json(name = "vote_average")
    private double voteAverage;
    @ColumnInfo(name = "popularity")
    private double popularity;
    @ColumnInfo(name = "is_favorite")
    private int isFavorite;

    private Movie(Parcel in) {
        id = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        isFavorite = in.readInt();
    }

    public Movie(@NonNull String id, @Nullable String overview,
                 @Nullable String releaseDate, @Nullable String posterPath,
                 @Nullable String backdropPath, @Nullable String title, double voteAverage, double popularity, int isFavorite) {
        this.id = id;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.title = title;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.isFavorite = isFavorite;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeInt(isFavorite);
    }
}
