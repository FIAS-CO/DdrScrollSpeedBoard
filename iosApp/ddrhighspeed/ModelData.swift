import Foundation
import Combine
import shared

final class ModelData: ObservableObject {
    var db: Database = connectDb()
    @Published var songs: [Song] = load()
    @Published var scrollSpeed: Int32 = 500
    
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
