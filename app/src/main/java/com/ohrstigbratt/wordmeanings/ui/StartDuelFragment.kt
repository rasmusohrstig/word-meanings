package com.ohrstigbratt.wordmeanings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ohrstigbratt.wordmeanings.R

class StartDuelFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start_duel, container, false)
    }

    companion object {
        fun newInstance(): StartDuelFragment {
            return StartDuelFragment()
        }
    }
}