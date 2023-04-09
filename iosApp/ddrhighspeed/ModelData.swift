import Foundation
import Combine
import shared
import SwiftUI

final class ModelData: ObservableObject {
    var db: Database = connectDb()
    @Published var songs: [Song] = load()
    @Published var scrollSpeed: String
    
    init() {
        let savedSpeed = UserDefaults.standard.string(forKey: "scrollSpeed")
        _scrollSpeed = Published(initialValue: savedSpeed ?? "")
    }
    
    func searchSong(searchWord: String) {
        if (searchWord=="") {
            songs = db.getNewSongs()
            return
        }
        songs = db.searchSongsByName(searchWord: searchWord)
    }
    
    func getScrollSpeedInt() -> Int {
        if let result = Int(scrollSpeed) {
            return result
        } else {
            return 0
        }
    }
}

func connectDb() -> Database {
    return Database(databaseDriverFactory: DatabaseDriverFactory())
}

func load() -> [Song] {
    let db = connectDb()
    let newSongs = db.getNewSongs()
    if(newSongs.isEmpty) {
        fatalError("Couldn't find new songs.")
    }
    
    return newSongs
}
