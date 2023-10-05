package com.fias.ddrhighspeed.data

interface IDataVersionDataStore {
    suspend fun saveDataVersionStore(version: Int)
    suspend fun getDataVersion(): Int
}