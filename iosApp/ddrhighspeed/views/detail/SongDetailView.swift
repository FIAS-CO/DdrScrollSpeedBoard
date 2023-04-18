import SwiftUI
import shared
import YouTubePlayerKit

struct SongDetailView: View {
    @EnvironmentObject var modelData: ModelData
    
    var song: Song
    var str: String = "https://www.youtube.com/watch?v=djtlC4Ykzpw"

    let bounds = UIScreen.main.bounds

    var body: some View {
        VStack {
            InputScrollSpeedView()
            
            Divider()
            
            Group {
                Text(song.name)
                    .font(.title)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)

                DetailTableView(rows: SongDetailUtil().toDetailRows(song: song, highSpeedValue: modelData.getScrollSpeedInt()))
                    .fixedSize(horizontal: true, vertical: true)
                    .padding(.leading)
            }
            .padding(.horizontal)

            var youTubePlayer = YouTubePlayer(
                            source: .url(str))
                        GeometryReader { geom in

                            YouTubePlayerView(youTubePlayer) { state in
                                // Overlay ViewBuilder closure to place an overlay View
                                // for the current `YouTubePlayer.State`
                                switch state {
                                case .idle:
                                    ProgressView()
                                case .ready:
                                    EmptyView()
                                case .error(let error):
                                    Text(verbatim: "YouTube player couldn't be loaded")
                                }
                            }
                            .frame(height:220)
                            .padding()
                        }
            Spacer()
        }
    }
}

struct SongDetailView_Previews: PreviewProvider {
    static var previews: some View {
        let data: ModelData = ModelData()
        data.scrollSpeed = "123"

        return SongDetailView(song:  Song(id: 1, name: "TestSongName", composer: nil, version: "testVersion", display_bpm: "123-456", min_bpm: 123, max_bpm: 456, base_bpm: 345, sub_bpm: 234, besp: 1, bsp: 2, dsp: 3, esp: 4, csp: 5, bdp: 6, ddp: 7, edp: 8, cdp: 9, shock_arrow: nil, deleted: nil))
            .environmentObject(data)
    }
}
