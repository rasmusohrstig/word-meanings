package com.ohrstigbratt.wordmeanings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ohrstigbratt.wordmeanings.R

class WaitForPlayerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wait_for_player, container, false)
    }

    companion object {
        fun newInstance(): WaitForPlayerFragment {
            return WaitForPlayerFragment()
        }
    }
}