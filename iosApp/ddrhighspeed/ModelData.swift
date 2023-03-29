import Foundation
import Combine
import shared
import SwiftUI

final class ModelData: ObservableObject {
    var db: Database = connectDb()
    @Published var songs: [Song] = load()
    @Published var scrollSpeed: Int
    
    init() {
        let savedSpeed = UserDefaults.standard.integer(forKey: "scrollSpeed")
        _scrollSpeed = Published(initialValue: savedSpeed)
        
    }
    
    func searchSong(searchWord: String) {
        if (searchWord=="") {
            songs = db.getNewSongs()
            return
        }
        songs = db.searchSongsByName(searchWord: searchWord)
    }
}

func connectDb() -> Database {
    return Database(databaseDriverFactory: DatabaseDriverFactory())
}

func load() -> [Song] {
    var db = connectDb()
    var newSongs = db.getNewSongs()
    if(newSongs.isEmpty) {
        fatalError("Couldn't find new songs.")
    }
    
    return newSongs
}
