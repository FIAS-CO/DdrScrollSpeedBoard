package com.fias.ddrhighspeed.model

class ResultRowSetFactory {
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
}
