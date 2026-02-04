package com.zsasko.rawg.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class GameDetailsResponse(
    val id: Int,
    val slug: String,
    val name: String,
    @SerialName("name_original")
    val nameOriginal: String,
    val description: String,
    val metacritic: Int?,
    @SerialName("metacritic_platforms")
    val metacriticPlatforms: List<GameDetailsResponseMetacriticPlatform>,
    val released: String,
    val tba: Boolean,
    val updated: String,
    @SerialName("background_image")
    val backgroundImage: String?,
    @SerialName("background_image_additional")
    val backgroundImageAdditional: String?,
    val website: String,
    val rating: Float?,
    @SerialName("rating_top")
    val ratingTop: Float?,
    val ratings: List<GameResponseItemRating>,
    val reactions: JsonElement?,
    val added: Int?,
    @SerialName("added_by_status")
    val addedByStatus: GameResponseItemAddedByStatus?,
    val playtime: Int?,
    @SerialName("screenshots_count")
    val screenshotsCount: Int?,
    @SerialName("movies_count")
    val moviesCount: Int?,
    @SerialName("creators_count")
    val creatorsCount: Int?,
    @SerialName("achievements_count")
    val achievementsCount: Int?,
    @SerialName("parent_achievements_count")
    val parentAchievementsCount: String?,
    @SerialName("reddit_url")
    val redditUrl: String,
    @SerialName("reddit_name")
    val redditName: String,
    @SerialName("reddit_description")
    val redditDescription: String,
    @SerialName("reddit_logo")
    val redditLogo: String,
    @SerialName("reddit_count")
    val redditCount: Int?,
    @SerialName("twitch_count")
    val twitchCount: Int?,
    @SerialName("youtube_count")
    val youtubeCount: Int?,
    @SerialName("reviews_text_count")
    val reviewsTextCount: String?,
    @SerialName("ratings_count")
    val ratingsCount: Int?,
    @SerialName("suggestions_count")
    val suggestionsCount: Int?,
    @SerialName("alternative_names")
    val alternativeNames: List<String>?,
    @SerialName("metacritic_url")
    val metacriticUrl: String?,
    @SerialName("parents_count")
    val parentsCount: Int?,
    @SerialName("additions_count")
    val additionsCount: Int?,
    @SerialName("game_series_count")
    val gameSeriesCount: Int?,
    @SerialName("esrb_rating")
    val esrbRating: GameResponseItemEsrbRating?,
    val platforms: List<GameDetailsResponseItemPlatform>?,
) {

    companion object {
        fun createMinimalMock(
            id: Int = 1,
            name: String = "Test Game"
        ): GameDetailsResponse {
            return GameDetailsResponse(
                id = id,
                slug = name.lowercase().replace(" ", "-"),
                name = name,
                nameOriginal = name,
                description = "An example game used for testing and previews.",
                metacritic = 82,
                metacriticPlatforms = listOf(
                    GameDetailsResponseMetacriticPlatform(
                        metascore = 82,
                        url = "https://www.metacritic.com/game/example"
                    )
                ),
                released = "2023-09-15",
                tba = false,
                updated = "2024-01-10T12:00:00Z",
                backgroundImage = "https://example.com/background.jpg",
                backgroundImageAdditional = "https://example.com/background_extra.jpg",
                website = "https://examplegame.com",
                rating = 4.3f,
                ratingTop = 5f,
                ratings = listOf(
                    GameResponseItemRating(
                        id = 5,
                        title = "exceptional",
                        count = 120,
                        percent = 68.5f
                    )
                ),
                reactions = null,
                added = 5400,
                addedByStatus = GameResponseItemAddedByStatus(
                    yet = 400,
                    owned = 2500,
                    beaten = 1200,
                    toplay = 800,
                    dropped = 200,
                    playing = 300
                ),
                playtime = 18,
                screenshotsCount = 12,
                moviesCount = 3,
                creatorsCount = 25,
                achievementsCount = 40,
                parentAchievementsCount = "40",
                redditUrl = "https://reddit.com/r/examplegame",
                redditName = "examplegame",
                redditDescription = "Official subreddit for Example Game",
                redditLogo = "https://example.com/reddit_logo.png",
                redditCount = 8700,
                twitchCount = 120,
                youtubeCount = 340,
                reviewsTextCount = "95",
                ratingsCount = 1800,
                suggestionsCount = 420,
                alternativeNames = listOf("Example Game Deluxe", "EG"),
                metacriticUrl = "https://www.metacritic.com/game/example",
                parentsCount = 1,
                additionsCount = 2,
                gameSeriesCount = 1,
                esrbRating = GameResponseItemEsrbRating(
                    id = 4,
                    name = "Mature",
                    slug = "mature"
                ),
                platforms = listOf(
                    GameDetailsResponseItemPlatform(
                        platform = GameDetailsResponseItemPlatformPlatform(
                            id = 1,
                            name = "PC",
                            slug = "pc",
                            image = "https://example.com/platform_pc.png",
                            yearEnd = null,
                            yearStart = 1990,
                            gamesCount = 50000,
                            imageBackground = "https://example.com/platform_pc_bg.jpg"
                        ),
                        releasedAt = "2023-09-15",
                        requirements = GameResponseItemRequirements(
                            minimum = "Intel i5, 8 GB RAM, GTX 1060",
                            recommended = "Intel i7, 16 GB RAM, RTX 2060"
                        )
                    )
                )
            )
        }
    }
}

@Serializable
data class GameDetailsResponseMetacriticPlatform(
    val metascore: Int,
    val url: String
)

@Serializable
data class GameDetailsResponseItemPlatform(
    val platform: GameDetailsResponseItemPlatformPlatform? = null,
    @SerialName("released_at")
    val releasedAt: String? = null,
    val requirements: GameResponseItemRequirements? = null,
)

@Serializable
data class GameDetailsResponseItemPlatformPlatform(
    val id: Int? = null,
    val name: String? = null,
    val slug: String? = null,
    val image: String? = null,
    @SerialName("year_end")
    val yearEnd: Int? = null,
    @SerialName("year_start")
    val yearStart: Int? = null,
    @SerialName("games_count")
    val gamesCount: Int? = null,
    @SerialName("image_background")
    val imageBackground: String? = null
)