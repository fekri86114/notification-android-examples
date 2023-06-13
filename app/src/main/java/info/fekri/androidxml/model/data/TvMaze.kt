package info.fekri.androidxml.model.data

import com.google.gson.annotations.SerializedName

class TvMaze : ArrayList<TvMazeItem>()

data class TvMazeItem(
    @SerializedName("character")
    val character: Character,
    @SerializedName("person")
    val person: Person,
) {
    data class Character(
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: Image,
        @SerializedName("_links")
        val links: Links,
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    ) {
        data class Image(
            @SerializedName("medium")
            val medium: String,
            @SerializedName("original")
            val original: String
        )

        data class Links(
            @SerializedName("self")
            val self: Self
        ) {
            data class Self(
                @SerializedName("href")
                val href: String
            )
        }
    }
}

data class Person(
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("country")
    val country: Country,
    @SerializedName("deathday")
    val deathday: Any,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Image,
    @SerializedName("_links")
    val links: Links,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated")
    val updated: Int,
    @SerializedName("url")
    val url: String
) {
    data class Country(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("timezone")
        val timezone: String
    )

    data class Image(
        @SerializedName("medium")
        val medium: String,
        @SerializedName("original")
        val original: String
    )

    data class Links(
        @SerializedName("self")
        val self: Self
    ) {
        data class Self(
            @SerializedName("href")
            val href: String
        )
    }
}
