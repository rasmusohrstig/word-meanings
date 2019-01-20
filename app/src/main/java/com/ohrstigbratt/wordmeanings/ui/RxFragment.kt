package com.ohrstigbratt.wordmeanings.ui

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class RxFragment : Fragment() {
        protected val disposables: CompositeDisposable = CompositeDisposable()

        override fun onDestroyView() {
            disposables.clear()
            super.onDestroyView()
        }
}