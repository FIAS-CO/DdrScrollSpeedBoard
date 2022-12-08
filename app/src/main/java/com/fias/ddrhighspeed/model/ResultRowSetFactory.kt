package com.fias.ddrhighspeed.model

class ResultRowSetFactory {
    companion object {
        private val highSpeedSet = doubleArrayOf(
            8.0,
            7.5, 7.0,
            6.5, 6.0,
            5.5, 5.0,
            4.5, 4.0,
            3.75, 3.5, 3.25, 3.0,
            2.75, 2.5, 2.25, 2.0,
            1.75, 1.5, 1.25, 1.0,
            0.75, 0.5, 0.25,
        )
    }

    fun create(scrollSpeed: Int): List<ResultRow> {
        val resultRowSet: MutableList<ResultRow> = mutableListOf()

        if (scrollSpeed < 30 || 2000 < scrollSpeed) {
            for (highSpeed in highSpeedSet) {
                resultRowSet.add(ResultRow("-", highSpeed.toString(), "-"))
            }
            return resultRowSet
        }

        for ((index, highSpeed) in highSpeedSet.withIndex()) {
            val maxBpm = (scrollSpeed.toDouble() / highSpeed).toInt()
            val maxScrollSpeed = maxBpm * highSpeed

            // minBpm = 次の行の maxBpm + 1
            val minBpm = if (index == 0) {
                1
            } else {
                (scrollSpeed.toDouble() / highSpeedSet[index - 1]).toInt() + 1
            }
            val minScrollSpeed = minBpm * highSpeed

            val bpmRange = "$minBpm ～ $maxBpm"
            val scrollSpeedRange = "$minScrollSpeed ～ $maxScrollSpeed"

            resultRowSet.add(ResultRow(bpmRange, highSpeed.toString(), scrollSpeedRange))
        }

        return resultRowSet
    }

    fun createForSongDetail(
        scrollSpeed: Int,
        minBpm: Double,
        freqBpm: Double,
        maxBpm: Double
    ): List<ResultRow> {

        val resultRowSet: MutableList<ResultRow> = mutableListOf()
        if (scrollSpeed < 30 || 2000 < scrollSpeed) {
            resultRowSet.apply {
                add(ResultRow(minBpm.toString(), "-", "-"))
                add(ResultRow(freqBpm.toString(), "-", "-"))
                add(ResultRow(maxBpm.toString(), "-", "-"))

            }
            return resultRowSet
        }

        // TODO -1になる場合があるのでハイフンに置き換え
        var hsForMinBpm: Double = -1.0
        for (hs in highSpeedSet) {
            if ((hs * minBpm) <= scrollSpeed) {
                hsForMinBpm = hs
                break
            }
        }
        var hsForFreqBpm: Double = -1.0
        for (hs in highSpeedSet) {
            if ((hs * freqBpm) <= scrollSpeed) {
                hsForFreqBpm = hs
                break
            }
        }
        var hsForMaxBpm: Double = -1.0
        for (hs in highSpeedSet) {
            if ((hs * maxBpm) <= scrollSpeed) {
                hsForMaxBpm = hs
                break
            }
        }

        resultRowSet.apply {
            add(
                ResultRow(
                    minBpm.toString(), hsForMinBpm.toString(),
                    (minBpm * hsForMinBpm).toString()
                )
            )
            add(
                ResultRow(
                    freqBpm.toString(), hsForFreqBpm.toString(),
                    (freqBpm * hsForFreqBpm).toString()
                )
            )
            add(
                ResultRow(
                    maxBpm.toString(), hsForMaxBpm.toString(),
                    (maxBpm * hsForMaxBpm).toString()
                )
            )
        }

        return resultRowSet
    }
}
