import Foundation
import Combine
import shared

final class ModelData: ObservableObject {
    @Published var songs: [Song] = load()
    
}

func load() -> [Song] {
    var db = Database(databaseDriverFactory: DatabaseDriverFactory())
    db.migrate()

    var newSongs = db.getNewSongs()
    if(newSongs.isEmpty) {
        fatalError("Couldn't find new songs.")
    }
    
    return newSongs
}
