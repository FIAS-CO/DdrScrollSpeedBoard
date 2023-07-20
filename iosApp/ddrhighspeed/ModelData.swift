import Foundation
import Combine
import shared
import SwiftUI

// TODO:複数のクラスに分割する
final class ModelData: ObservableObject {
    var db: Database = connectDb()
    let backgroundQueue = DispatchQueue(label: "com.fias.ddrhighspeed", qos: .background)
    var sourceDataVersion: Int = 0 {
        didSet {
            setUpdateAvailable()
        }
    }
    
    let localVersionKey = "localVersion"
    let service = SpreadSheetService()
    
    @Published var scrollSpeed: String
    
    // SearchSongView
    @Published var songs: [Song] = load()
    @Published var updateAvailable: Bool = false
    @Published var isLoading: Bool = false
    @Published var showingAlert: Bool = false
    @Published var localDataVersion: Int
    {
        didSet {
            setUpdateAvailable()
            UserDefaults.standard.set(localDataVersion, forKey: localVersionKey)
            main { [self] in
                versionText = "version: \(String(localDataVersion))"
            }
        }
    }
    @Published var versionText: String = "version: checking..."
    
    init() {
        let savedSpeed = UserDefaults.standard.string(forKey: "scrollSpeed")
        _scrollSpeed = Published(initialValue: savedSpeed ?? "")
        
        let localVersion = UserDefaults.standard.integer(forKey: localVersionKey)
        _localDataVersion = Published(initialValue: localVersion)
        
        if localVersion == 0 {
            self.downloadSongData()
        } else {
            self.checkNewDataVersionAvailable()
        }
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
    
    func getMovies(id: Int64) -> [Movie] {
        return db.getMovies(songId: id)
    }
    
    func checkNewDataVersionAvailable() {
        // TODO UserDefaults関連をメソッド化（クラス化？）
        localDataVersion = UserDefaults.standard.integer(forKey: localVersionKey)
        
        service.getNewDataVersion() {version, _ in
            self.sourceDataVersion = Int(truncating: version ?? 0)
        }
    }
    
    func downloadSongData() {
        isLoading = true
        
        // Downloading表示にするために表示をTextに切り替える
        updateAvailable = false
        versionText = "Now downloading..."
        
        service.createAllData() { result,_  in
            
            defer {
                self.main { [self] in
                    checkNewDataVersionAvailable()
                    isLoading = false
                    songs = self.db.getNewSongs()
                }
            }
            
            if result is FailureResult {
                print((result as! FailureResult))
                self.main { [self] in
                    showingAlert = true
                    versionText = "version: \(String(localDataVersion))"
                }
                return
            }
            
            if let r = result as? SuccessResult {
                if r.songNames.count != r.musicProperties.count || r.songNames.count != r.shockArrowExists.count ||
                    r.songNames.count != r.webMusicIds.count {
                    self.main { [self] in
                        showingAlert = true
                        versionText = "version: \(String(localDataVersion))"
                    }
                    return
                }
                
                self.db.reinitializeSongNames(songNames: r.songNames)
                self.db.reinitializeSongProperties(songProperties: r.musicProperties)
                self.db.reinitializeShockArrowExists(shockArrowExists: r.shockArrowExists)
                self.db.reinitializeWebMusicIds(webMusicIds: r.webMusicIds)
                
                self.sourceDataVersion = Int(r.version)
                self.main { [self] in
                    localDataVersion = Int(r.version)
                }
            } else {
                // 何らかの理由でダウンキャストが失敗した場合のエラーハンドリング
                print("Unexpected error: allDataResult should be SuccessResult")
            }
        }
    }
    
    private func setUpdateAvailable() {
        main { [self] in
            updateAvailable = sourceDataVersion > localDataVersion
        }
    }
    
    private func main(_ block: @escaping () -> Void) {
        DispatchQueue.main.async {
            block()
        }
    }
}

func connectDb() -> Database {
    return Database(databaseDriverFactory: DatabaseDriverFactory())
}

func load() -> [Song] {
    let db = connectDb()
    let newSongs = db.getNewSongs()
    
    return newSongs
}
