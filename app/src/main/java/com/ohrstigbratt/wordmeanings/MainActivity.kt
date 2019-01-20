package com.ohrstigbratt.wordmeanings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.ohrstigbratt.wordmeanings.ui.DefineWordFragment
import com.ohrstigbratt.wordmeanings.ui.StartDuelFragment
import com.ohrstigbratt.wordmeanings.ui.StartPracticeFragment
import com.ohrstigbratt.wordmeanings.ui.WaitForPlayerFragment
import kotlinx.android.synthetic.main.activity_main.playButton
import kotlinx.android.synthetic.main.activity_main.tabs
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

    private val practiceFragmentTag = StartPracticeFragment::class.java.name
    private val duelFragmentTag = StartDuelFragment::class.java.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUpTabs()
        setUpFragments()
        setUpPlayButton()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun setUpTabs() {
        tabs.apply {
            addTab(newTab().apply {
                icon = getDrawable(R.drawable.practice_icon)
                text = getString(R.string.practice)
                tag = practiceFragmentTag
                select()
            })
        }
        tabs.apply {
            addTab(newTab().apply {
                icon = getDrawable(R.drawable.duel_icon)
                text = getString(R.string.duel)
                tag = duelFragmentTag
            })
        }
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.tag) {
                    practiceFragmentTag -> showPracticeFragment()
                    duelFragmentTag -> showDuelFragment()
                }
            }
        })
    }

    private fun setUpFragments() {
        val practiceFragment = StartPracticeFragment.newInstance()
        val duelFragment = StartDuelFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.tabFragmentContainer, practiceFragment, practiceFragmentTag)
                .show(practiceFragment)
                .add(R.id.tabFragmentContainer, duelFragment, duelFragmentTag)
                .hide(duelFragment)
                .commit()
    }

    private fun setUpPlayButton() {
        playButton.setOnClickListener {
            val selectedTabTag = getSelectedTabTag()
            val nextFragment = when (selectedTabTag) {
                practiceFragmentTag -> DefineWordFragment.newInstance()
                duelFragmentTag -> WaitForPlayerFragment.newInstance()
                else -> null
            }
            nextFragment?.let {
                supportFragmentManager.beginTransaction()
                        .add(R.id.screenFragmentContainer, nextFragment)
                        .addToBackStack(selectedTabTag)
                        .commit()
            }
        }
    }

    private fun showDuelFragment() {
        val practiceFragment = supportFragmentManager.findFragmentByTag(practiceFragmentTag)
        val duelFragment = supportFragmentManager.findFragmentByTag(duelFragmentTag)
        supportFragmentManager.beginTransaction().apply {
            practiceFragment?.let {
                hide(it)
            }
            duelFragment?.let {
                show(it)
            }
        }.commit()
    }

    private fun showPracticeFragment() {
        val practiceFragment = supportFragmentManager.findFragmentByTag(practiceFragmentTag)
        val duelFragment = supportFragmentManager.findFragmentByTag(duelFragmentTag)
        supportFragmentManager.beginTransaction().apply {
            duelFragment?.let { hide(it) }
            practiceFragment?.let { show(it) }
        }.commit()
    }

    private fun getSelectedTabTag(): String? {
        val selectedTag = tabs.getTabAt(tabs.selectedTabPosition)?.tag
        return if (selectedTag is String) {
            selectedTag
        } else {
            null
        }
    }
}
