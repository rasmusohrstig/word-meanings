package com.ohrstigbratt.wordmeanings.data.domainmodel

import android.util.Log

data class WordDefinition(val word: String, val definitions: List<String>) {
    fun judgeGuess(guess: String): GuessState {
        val score = scoreGuess(guess)
        return when {
            score == 1F -> GuessState.CORRECT
            score < 1 && score > 0.5 -> GuessState.PARTIALLY_CORRECT
            else -> GuessState.GUESSING
        }
    }

    fun scoreGuess(guess: String): Float {
        return scoreGuessImplementation(guess, definitions)
    }
}

// Function that attempts to match the guess against the list of definitions and assigns a score
private fun scoreGuessImplementation(guess: String, definitions: List<String>): Float {
    if (guess.isEmpty()) {
        return 0F
    }
    var maxScore = 0F

    val lcGuess = guess.toLowerCase()
    // Look for match on whole definition
    definitions.forEach { definition ->
        val lcDefinition = definition.toLowerCase()
        val editDistance = levenshtein(lcGuess, lcDefinition)
        val numOfCorrect = if (lcGuess.length < lcDefinition.length) {
            lcDefinition.length - editDistance
        } else {
            lcGuess.length - editDistance
        }
        val score = numOfCorrect.toFloat() / lcDefinition.length
        if (score > maxScore) {
            Log.d("DefineWord", "match: $definition")
            maxScore = score
        }
    }

    // Look for longest perfect partial match
    definitions.forEach { definition ->
        if (definition.toLowerCase().contains(lcGuess)) {
            val score = when {
                lcGuess.length < 5 -> 0F
                lcGuess.length == 5 -> 0.1F
                lcGuess.length == 6 -> 0.2F
                lcGuess.length == 7 -> 0.3F
                lcGuess.length == 8 -> 0.4F
                lcGuess.length == 9 -> 0.5F
                lcGuess.length == 10 -> 0.6F
                lcGuess.length == 11 -> 0.8F
                lcGuess.length > 11 -> 1.0F
                else -> 0F
            }
            if (score > maxScore) {
                Log.d("DefineWord", "match: $definition")
                maxScore = score
            }
        }
    }

    definitions.forEach { definition ->
        var matchingChars = 0
        lcGuess.split(" ").forEach { guessPart ->
            if (definition.toLowerCase().contains(guessPart)) {
                matchingChars += guessPart.length
            }
        }
        val score = when {
            matchingChars < 6 -> 0F
            matchingChars == 6 -> 0.1F
            matchingChars == 7 -> 0.2F
            matchingChars == 8 -> 0.3F
            matchingChars == 9 -> 0.4F
            matchingChars == 10 -> 0.5F
            matchingChars == 11 -> 0.6F
            matchingChars == 12 -> 0.7F
            matchingChars == 13 -> 0.8F
            matchingChars > 13 -> 1.0F
            else -> 0F
        }
        if (score > maxScore) {
            Log.d("DefineWord", "match: $definition")
            maxScore = score
        }
    }

    Log.d("DefineWord", "score: $maxScore")

    return maxScore
}

/**
 * Copied from https://rosettacode.org/wiki/Levenshtein_distance#Kotlin
 *
 * version 1.0.6
 * Uses the "iterative with two matrix rows" algorithm referred to in the Wikipedia article.
 */
private fun levenshtein(s: String, t: String): Int {
    // degenerate cases
    if (s == t) return 0
    if (s == "") return t.length
    if (t == "") return s.length

    // create two integer arrays of distances and initialize the first one
    val v0 = IntArray(t.length + 1) { it }  // previous
    val v1 = IntArray(t.length + 1)         // current

    var cost: Int
    for (i in 0 until s.length) {
        // calculate v1 from v0
        v1[0] = i + 1
        for (j in 0 until t.length) {
            cost = if (s[i] == t[j]) 0 else 1
            v1[j + 1] = Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost))
        }
        // copy v1 to v0 for next iteration
        for (j in 0..t.length) v0[j] = v1[j]
    }
    return v1[t.length]
}