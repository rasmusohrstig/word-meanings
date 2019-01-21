package com.ohrstigbratt.wordmeanings.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.wordnik.com/v4/"

interface WordnikApi {

    @GET("words.json/randomWord")
    fun getRandomWord(
        @Query("hasDictionaryDef") hasDictionaryDef: Boolean = true,
        @Query("maxCorpusCount") maxCorpusCount: Int = -1,
        @Query("minDictionaryCount") minDictionaryCount: Int = 1,
        @Query("maxDictionaryCount") maxDictionaryCount: Int = -1,
        @Query("minLength") minLength: Int = 3,
        @Query("maxLength") maxLength: Int = -1,
        @Query("api_key") api_key: String
    ): Observable<RandomWordResponse>

    @GET("word.json/{word}/definitions")
    fun getDefinition(
        @Path("word") word: String,
        @Query("limit") limit: Int = 200,
        @Query("includeRelated") includeRelated: Boolean = false,
        @Query("useCanonical") useCanonical: Boolean = true,
        @Query("includeTags") includeTags: Boolean = false,
        @Query("api_key") api_key: String
    ): Observable<List<DefinitionResponse>>
}

data class RandomWordResponse(val id: Int, val word: String)

data class DefinitionResponse(val text: String?)