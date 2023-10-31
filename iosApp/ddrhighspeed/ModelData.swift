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
    var baseSongs: [Song] = loadSongs()
    @Published var songs: [Song] = []
    
    var baseCourses: [CourseData] = loadCourses()
    @Published var courses: [CourseData] = []
    
    @Published var updateAvailable: Bool = false
    @Published var isLoading: Bool = false
    @Published var showingAlert: Bool = false
    @Published var alertMessage: String = ""
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
    
    init(isPreviewMode: Bool = false, songsForPreview: [Song] = [], coursesForPreview: [CourseData] = []) {
        let savedSpeed = UserDefaults.standard.string(forKey: "scrollSpeed")
        _scrollSpeed = Published(initialValue: savedSpeed ?? "")
        
        let localVersion = UserDefaults.standard.integer(forKey: localVersionKey)
        _localDataVersion = Published(initialValue: localVersion)
        
        // Workaround: プレビュー時にdownloadSongDataが動くとタイムアウトでクラッシュする
        if isPreviewMode {
            songs = songsForPreview
            courses = coursesForPreview
            return
        }
        
        if localVersion == 0 {
            self.downloadSongData()
        } else {
            self.checkNewDataVersionAvailable()
        }
        songs = baseSongs
        courses = baseCourses
    }
    
    func searchSong(searchWord: String) {
        if (searchWord=="") {
            songs = baseSongs
            return
        }
        songs = baseSongs.filter{song in
            return song.nameWithDifficultyLabel().localizedCaseInsensitiveContains(searchWord)
        }
    }
    
    func searchCourse(searchWord: String) {
        if (searchWord=="") {
            courses = baseCourses
            return
        }
        courses = baseCourses.filter{course in
            return course.getCourseLabel().localizedCaseInsensitiveContains(searchWord)
        }
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
        if (isLoading) {
            return
        }
        
        isLoading = true
        
        // Downloading表示にするために表示をTextに切り替える
        updateAvailable = false
        versionText = "Now downloading..."
        
        service.createAllData() { result,_  in
            
            defer {
                self.main { [self] in
                    checkNewDataVersionAvailable()
                    isLoading = false
                    
                    baseSongs = db.getNewSongs()
                    baseCourses = loadCourses()
                    songs = baseSongs
                    courses = baseCourses
                }
            }
            
            if result is FailureResult {
                print(result as! FailureResult)
                self.main { [self] in
                    showingAlert = true
                    versionText = "version: \(String(localDataVersion))"
                    alertMessage = "更新データのロードに失敗しました。再度実施していただき、それでも失敗した時はX(Twitter)の@sig_reに連絡をお願いします。"
                }
                return
            }
            
            if let r = result as? SuccessResult {
                if r.songNames.count > r.musicProperties.count ||
                    r.songNames.count != r.shockArrowExists.count ||
                    r.songNames.count != r.webMusicIds.count {
                    self.main { [self] in
                        showingAlert = true
                        versionText = "version: \(String(localDataVersion))"
                        alertMessage = "更新データの保存に失敗しました。X(Twitter)の@sig_reに連絡をお願いします。(version:\(r.version))"
                    }
                    return
                }
                
                self.db.reinitializeSongNames(songNames: r.songNames)
                self.db.reinitializeSongProperties(songProperties: r.musicProperties)
                self.db.reinitializeShockArrowExists(shockArrowExists: r.shockArrowExists)
                self.db.reinitializeWebMusicIds(webMusicIds: r.webMusicIds)
                self.db.reinitializeMovies(movies: r.movies)
                self.db.reinitializeCourses(courses: r.courses)
                
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
    
    public func getSongData(songId: Int64, propertyId: Int64) -> SongData {
        if (propertyId == -1) {
            return (db.getSongsById(songId: songId).first ?? createDummySong()).convertToSongData()
        }
        
        let songNameData = db.getSongNameById(songId: songId)
        let propertyData = db.getSongPropertyById(songId: propertyId)
        
        return songNameData.convertToSongData(prop: propertyData)
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
    
    private func createDummySong() -> Song {
        return Song(id: -1, name: "NoData", composer: "NoData", version: "NoData", display_bpm: "NoData", min_bpm: nil, max_bpm: nil, base_bpm: 100.0, sub_bpm: nil, besp: 1, bsp: 1, dsp: 1, esp: 1, csp: 1, bdp: 1, ddp: 1, edp: 1, cdp: 1, shock_arrow: "1", deleted: 1, difficulty_label: "NoData")
    }
}

func connectDb() -> Database {
    return Database(databaseDriverFactory: DatabaseDriverFactory())
}

func loadSongs() -> [Song] {
    let db = connectDb()
    let newSongs = db.getNewSongs()
    
    return newSongs
}

func loadCourses() -> [CourseData] {
    let db = connectDb()
    let newCourses = db.getNewCourses().map{$0.convertToCourseData()}
    
    return newCourses
}

extension Song {
    func nameWithDifficultyLabel() -> String {
        var result = name
        let label = difficulty_label ?? ""
        if (!label.isEmpty) {
            result += "(\(label))"
        }
        return result
    }
}
