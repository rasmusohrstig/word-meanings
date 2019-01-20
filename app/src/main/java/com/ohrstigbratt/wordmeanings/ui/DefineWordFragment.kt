package com.ohrstigbratt.wordmeanings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProviders
import com.ohrstigbratt.wordmeanings.R
import com.ohrstigbratt.wordmeanings.data.WordDefinitionFacade
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_define_word.defineWordRootView
import kotlinx.android.synthetic.main.fragment_define_word.guessField
import kotlinx.android.synthetic.main.fragment_define_word.wordView

class DefineWordFragment : RxFragment() {

    private lateinit var viewModel: DefineWordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_define_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, DefineWordVmFactory(activity!!.application, WordDefinitionFacade()))
                .get(DefineWordViewModel::class.java)
        disposables += viewModel.getViewState()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    updateView(it)
                }
                .subscribe()
        viewModel.updateGuess("")
        guessField.addTextChangedListener { text ->
            viewModel.updateGuess(text.toString())
        }
    }

    companion object {
        fun newInstance(): DefineWordFragment {
            return DefineWordFragment()
        }
    }

    private fun updateView(state: DefineWordViewState) {
        wordView.text = state.word
        defineWordRootView.setBackgroundColor(state.bgColor)
    }
}
