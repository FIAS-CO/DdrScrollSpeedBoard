package com.example.ddrscrollspeedboard.model

data class ResultRow(
    private var _minBpm:Int, private var _maxBpm:Int, private val _highSpeed:Double,
    private var _minScrollSpeed:Double, private var _maxScrollSpeed:Double) {

    // TODO 各変数をstringで返す関数が必要？特にDoubleは小数点以下2桁くらいで表示したい
    val bpmRange = "$_minBpm ～ $_maxBpm"
    val highSpeed = _highSpeed.toString()
    val scrollSpeedRange = "$_minScrollSpeed ～ $_maxScrollSpeed"
}
