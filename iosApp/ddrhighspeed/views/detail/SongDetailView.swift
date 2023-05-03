import SwiftUI
import shared
import YouTubePlayerKit

struct SongDetailView: View {
    @EnvironmentObject var modelData: ModelData
    @State var selectedTab = 0
    
    var song: Song
    
    var body: some View {
        VStack {
            Text(song.name)
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(Color(UIColor.systemGray6))
            DetailTabView(list: ["ハイスピ", "動画"], selectedTab: $selectedTab)
            TabView(selection: $selectedTab, content: {
                DetailHighSpeedView(song: song)
                    .tag(0)
                MoviesModalView(movies: modelData.getMovies(id: song.id), songName: song.name)
                .tag(1)
            })
        }
        // Revisit:検索画面からNavigationView経由で開くと画面上部に空白ができる対策
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct SongDetailView_Previews: PreviewProvider {
    static var previews: some View {
        let data: ModelData = ModelData()
        data.scrollSpeed = "123"
        
        return SongDetailView(song: Song(id: 1, name: "TestSongName", composer: nil, version: "testVersion", display_bpm: "123-456", min_bpm: 123, max_bpm: 456, base_bpm: 345, sub_bpm: 234, besp: 1, bsp: 2, dsp: 3, esp: 4, csp: 5, bdp: 6, ddp: 7, edp: 8, cdp: 9, shock_arrow: nil, deleted: nil))
            .environmentObject(data)
    }
}
