package com.fias.ddrhighspeed.data

interface IDataVersionDataStore {
    suspend fun saveDataVersionStore(topRowIndex: Int)
    suspend fun getDataVersion(): Int
}