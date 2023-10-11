package com.fias.ddrhighspeed.data

class TestDataVersionDataStore: IDataVersionDataStore {
    var actualInputVersion: Int = -1
    var expectedOutputVersion: Int = 0

    override suspend fun saveDataVersionStore(version: Int) {
        actualInputVersion = version
        expectedOutputVersion = version
    }

    override suspend fun getDataVersion(): Int {
        return expectedOutputVersion
    }
}