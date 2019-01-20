package com.ohrstigbratt.wordmeanings.ui

import android.app.Application
import android.content.Context
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

    fun getViewState(): Observable<DefineWordViewState> {
        return guessSubject.flatMap { guess ->
            definitionFacade.getRandomDefinition()
                    .first(WordDefinition("", emptyList()))
                    .toObservable()
                    .map { definition ->
                        DefineWordViewState.newInstance(app.applicationContext, definition, guess, definition.judgeGuess(guess))
                    }
        }
    }

    fun updateGuess(guess: String) {
        guessSubject.onNext(guess)
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