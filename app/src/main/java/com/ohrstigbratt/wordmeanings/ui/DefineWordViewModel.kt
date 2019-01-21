package com.ohrstigbratt.wordmeanings.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ohrstigbratt.wordmeanings.R
import com.ohrstigbratt.wordmeanings.data.WordDefinitionFacade
import com.ohrstigbratt.wordmeanings.data.domainmodel.GuessState
import com.ohrstigbratt.wordmeanings.data.domainmodel.WordDefinition
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class DefineWordViewModel(val app: Application, val definitionFacade: WordDefinitionFacade) : AndroidViewModel(app) {

    private var guessState = GuessState.GUESSING
    private var guessSubject = PublishSubject.create<String>()
    private var definitionObservable: Observable<WordDefinition>? = null

    fun newWord() {
        definitionObservable = definitionFacade.getRandomDefinition()
                .firstOrError()
                .toObservable()
                .cache()
    }

    fun getViewState(): Observable<DefineWordViewState> {
        return guessSubject.flatMap { guess ->
            getDefinitionObservable()
                    .map { definition ->
                        Log.d("DefineWord", "definition: ${definition.word}")
                        DefineWordViewState.newInstance(app.applicationContext, definition, guess, definition.judgeGuess(guess))
                    }
        }
    }

    fun updateGuess(guess: String) {
        guessSubject.onNext(guess)
    }

    private fun getDefinitionObservable(): Observable<WordDefinition> {
        definitionObservable?.let { return it }
        val newObservable = definitionFacade.getRandomDefinition()
                .firstOrError()
                .toObservable()
                .cache()
        definitionObservable = newObservable
        return newObservable
    }
}

class DefineWordVmFactory(
    private val app: Application,
    private val definitionFacade: WordDefinitionFacade
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DefineWordViewModel(app, definitionFacade) as T
    }
}

data class DefineWordViewState(val word: String, val guess: String, val bgColor: Int) {
    companion object {
        fun newInstance(context: Context, definition: WordDefinition, guess: String, guessState: GuessState): DefineWordViewState {
            return DefineWordViewState(definition.word, guess, colorForGuessState(context, guessState))
        }

        private fun colorForGuessState(context: Context, guessState: GuessState): Int {
            return when (guessState) {
                GuessState.GUESSING -> context.resources.getColor(R.color.white)
                GuessState.CORRECT -> context.resources.getColor(R.color.green)
                GuessState.PARTIALLY_CORRECT -> context.resources.getColor(R.color.yellow)
                GuessState.FAILURE -> context.resources.getColor(R.color.red)
            }
        }
    }
}