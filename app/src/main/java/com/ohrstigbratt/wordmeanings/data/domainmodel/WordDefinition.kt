package com.ohrstigbratt.wordmeanings.data.domainmodel

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

private fun scoreGuessImplementation(guess: String, definitions: List<String>): Float {
    if (guess.isEmpty()) {
        return 0F
    }
    var maxScore = 0F

    // Look for match on whole definition
    definitions.forEach { definition ->
        val editDistance = levenshtein(guess, definition)
        val numOfCorrect = if (guess.length < definition.length) {
            definition.length - editDistance
        } else {
            guess.length - editDistance
        }
        val score = numOfCorrect.toFloat() / definition.length
        if (score > maxScore) {
            maxScore = score
        }
    }

    // Look for longest perfect partial match
    definitions.forEach { definition ->
        if (definition.contains(guess)) {
            val score = when {
                guess.length < 5 -> 0F
                guess.length == 5 -> 0.1F
                guess.length == 6 -> 0.2F
                guess.length == 7 -> 0.3F
                guess.length == 8 -> 0.4F
                guess.length == 9 -> 0.5F
                guess.length == 10 -> 0.6F
                guess.length == 11 -> 0.8F
                guess.length > 11 -> 1.0F
                else -> 0F
            }
            if (score > maxScore) {
                maxScore = score
            }
        }
    }

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