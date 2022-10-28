package com.example.ddrscrollspeedboard.model

class ResultRowSetFactory {
    private val highSpeedSet = doubleArrayOf(
        0.25, 0.5, 0.75,
        1.0, 1.25, 1.5, 1.75,
        2.0, 2.25, 2.5, 2.75,
        3.0, 3.25, 3.5, 3.75,
        4.0, 4.5,
        5.0, 5.5,
        6.0, 6.5,
        7.0, 7.5,
        8.0
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
            val minBpm = if (index < highSpeedSet.size - 1) {
                (scrollSpeed.toDouble() / highSpeedSet[index + 1]).toInt() + 1
            } else {
                1
            }
            val minScrollSpeed = minBpm * highSpeed

            val bpmRange = "$minBpm ～ $maxBpm"
            val scrollSpeedRange = "$minScrollSpeed ～ $maxScrollSpeed"

            resultRowSet.add(ResultRow(bpmRange, highSpeed.toString(), scrollSpeedRange))
        }

        return resultRowSet
    }
}
