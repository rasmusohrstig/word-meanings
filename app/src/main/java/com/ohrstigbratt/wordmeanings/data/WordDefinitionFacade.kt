package com.ohrstigbratt.wordmeanings.data

import android.util.Log
import com.ohrstigbratt.wordmeanings.data.domainmodel.WordDefinition
import io.reactivex.Observable

class WordDefinitionFacade {
    fun getRandomDefinition(): Observable<WordDefinition> {
        Log.d("DefineWord", "getRandomDefinition")
        return Observable.just(WordDefinition(
                "cross-reference",
                listOf("a reference from one part of a book, index, or the like, to related material, as a word or illustration, in another part."))
        )
    }
}