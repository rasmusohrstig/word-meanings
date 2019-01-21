package com.ohrstigbratt.wordmeanings.data

import android.util.Log
import com.ohrstigbratt.wordmeanings.R
import com.ohrstigbratt.wordmeanings.WordMeaningsApp
import com.ohrstigbratt.wordmeanings.data.domainmodel.WordDefinition
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class WordDefinitionFacade(private val app: WordMeaningsApp) {
    fun getRandomDefinition(): Observable<WordDefinition> {
        return app.wordnikApi.getRandomWord(api_key = app.getString(R.string.wordnik_key))
                .subscribeOn(Schedulers.io())
                .flatMap { randomWordResponse ->
                    Log.d("DefineWord", "randomWordResponse: ${randomWordResponse.word}")
                    app.wordnikApi.getDefinition(randomWordResponse.word, api_key = app.getString(R.string.wordnik_key))
                            .map { definitionResponse ->
                                Log.d("DefineWord", "definitionResponse: ${definitionResponse.size}")
                                WordDefinition(randomWordResponse.word, definitionResponse.map { it.text.orEmpty() })
                            }
                }
    }
}