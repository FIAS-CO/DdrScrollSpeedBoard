import SwiftUI
import shared

struct CourseDetailView: View {
    @EnvironmentObject var modelData: ModelData
    var course: CourseData
    
    var body: some View {
        let firstSong: SongData = modelData.getSongData(songId: course.firstSongId, propertyId: course.firstSongPropertyId)
        let secondSong: SongData = modelData.getSongData(songId: course.secondSongId, propertyId: course.secondSongPropertyId)
        let thirdSong: SongData = modelData.getSongData(songId: course.thirdSongId, propertyId: course.thirdSongPropertyId)
        let fourthSong: SongData = modelData.getSongData(songId: course.fourthSongId, propertyId: course.firstSongPropertyId)
        
        VStack() {
            Text(course.getCourseLabel())
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(Color(UIColor.systemGray6))
            
            InputScrollSpeedView()
                .padding(.all, 0)
            
            ScrollView {
                SongView(song: firstSong, index: 1, scrollSpeed: modelData.getScrollSpeedInt())
                SongView(song: secondSong, index: 2, scrollSpeed: modelData.getScrollSpeedInt())
                SongView(song: thirdSong, index: 3, scrollSpeed: modelData.getScrollSpeedInt())
                SongView(song: fourthSong, index: 4, scrollSpeed: modelData.getScrollSpeedInt())
                Spacer(minLength: 50) // 空白を追加してViewが被らないように
            }
            
            UnderlineBannerView()
        }
        .navigationBarTitleDisplayMode(.inline)
        .ignoresSafeArea(.keyboard, edges: .bottom)
    }
}

struct SongView: View {
    var song: SongData
    var index: Int
    var scrollSpeed: Int
    
    var body: some View {
        var title: String {
            switch index {
            case 1 : return "1st: \(song.name)"
            case 2 : return "2nd: \(song.name)"
            case 3 : return "3rd: \(song.name)"
            case 4 : return "4th: \(song.name)"
            default: return song.name
            }
        }
        
        let suggestHighspeed: Double = calculateSuggestHs(song: self.song, scrollSpeed: self.scrollSpeed)
        let bpm: Double = song.baseBpm
        let hasHighspeedArea: Bool = song.hasHighSpeedArea()
        let hasLowSpeedArea: Bool = song.hasLowSpeedArea()
        
        VStack(spacing: 5) {
            HStack {
                Text(title)
                    .font(Font.system(size: 24, weight: .bold))
                Spacer()
            }
            
            VStack() {
                HStack {
                    Text("推奨ハイスピ: \(formatNumber(suggestHighspeed))")
                        .frame(maxWidth: .infinity)
                        .font(Font.system(size: 20, weight: .medium))
                        .padding()
                        .background(Color.gray.opacity(0.9))
                    Spacer()
                    NavigationLink(destination: SongDetailView(song: song.toSong())) {
                        Text("曲詳細>>")
                            .font(Font.system(size: 18, weight: .medium))
                            .padding()
                            .background(Color.gray.opacity(0.5))
                            .cornerRadius(5)
                    }
                }
                
                HStack() {
                    Text("基本BPM: \(bpm, specifier: "%.1f")")
                    
                    Spacer()
                    let highspeedMark = hasHighspeedArea ? "○": "×"
                    Text("高速地帯: \(highspeedMark)")
                    
                    Spacer()
                    let lowspeedMark = hasLowSpeedArea ? "○": "×"
                    Text("低速地帯: \(lowspeedMark)")
                }
                .background(Color.gray.opacity(0.1))
            }
            .background(Color.gray.opacity(0.05))
        }
        .padding(.bottom)
        .padding(.horizontal)
        .background(Color.gray.opacity(0.02))
    }
    
    func formatNumber(_ value: Double) -> String {
        let str = String(format: "%.2f", value)
        if str.hasSuffix(".00") || str.hasSuffix("0") {
            return String(format: "%.1f", value)
        }
        return str
    }
    
    func calculateSuggestHs(song: SongData, scrollSpeed: Int) -> Double {
        return SongDetailUtil().culcateSuggestHighSpeed(bpm: song.baseBpm, scrollSpeed: scrollSpeed)
    }
    
}

// TODO 全体でSongを使わずSongDataを使うようにする
extension SongData {
    func toSong() -> Song {
        return Song(id: id, name: name, composer: composer ?? "", version: version, display_bpm: displayBpm, min_bpm: minBpm, max_bpm: maxBpm, base_bpm: baseBpm, sub_bpm: subBpm, besp: besp, bsp: bsp, dsp: dsp, esp: esp, csp: csp, bdp: bdp, ddp: ddp, edp: edp, cdp: cdp, shock_arrow: shockArrow ?? "", deleted: deleted, difficulty_label: difficultyLabel)
    }
}

struct CourseDetailView_Previews: PreviewProvider {
    static var previews: some View {
        let course = CourseData(id: 0, name: "Course", isDan: true, firstSongId: 1, firstSongPropertyId: -1, secondSongId: 2, secondSongPropertyId: -1, thirdSongId: 3, thirdSongPropertyId: -1, fourthSongId: 4, fourthSongPropertyId: -1, isDeleted: true)
        CourseDetailView(course: course)
            .environmentObject(ModelData(isPreviewMode: true))
    }
}
