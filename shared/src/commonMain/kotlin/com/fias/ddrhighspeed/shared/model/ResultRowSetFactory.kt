package com.fias.ddrhighspeed.shared.model

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

        for ((index, highSpeedValue) in highSpeedSet.withIndex()) {
            val maxBpm = (scrollSpeed.toDouble() / highSpeedValue).toInt()
            val maxScrollSpeed = maxBpm * highSpeedValue

            // minBpm = 次の行の maxBpm + 1
            val minBpm = if (index == 0) {
                1
            } else {
                (scrollSpeed.toDouble() / highSpeedSet[index - 1]).toInt() + 1
            }
            val minScrollSpeed = minBpm * highSpeedValue

            val bpmRange = "$minBpm ～ $maxBpm"
            val highSpeed = "× $highSpeedValue"
            val scrollSpeedRange = "= $minScrollSpeed ～ $maxScrollSpeed"

            resultRowSet.add(ResultRow(bpmRange, highSpeed, scrollSpeedRange))
        }

        return resultRowSet
    }

    fun createForDetail(scrollSpeed: Int, category: String, bpm: Double): ResultRowForDetail {
        if (scrollSpeed < 30 || 2000 < scrollSpeed) {
            return ResultRowForDetail(category, bpm.toString(), "-", "-")
        }

        var matchedHs = calculateHighSpeed(bpm, scrollSpeed)

        return ResultRowForDetail(
            category,
            bpm.toString(),
            "× $matchedHs",
            "= " + (bpm * matchedHs)
        )
    }

    fun calculateHighSpeed(bpm: Double, scrollSpeed: Int): Double {
        var matchedHs = 0.25
        for (hs in highSpeedSet) {
            if ((hs * bpm) <= scrollSpeed) {
                matchedHs = hs
                break
            }
        }
        return matchedHs
    }
}
