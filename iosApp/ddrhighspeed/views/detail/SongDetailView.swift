import SwiftUI
import shared

struct SongDetailView: View {
    @EnvironmentObject var modelData: ModelData
    
    var song: Song
    var rows: [ResultRowForDetail] = []
    
    var body: some View {
        VStack {
            InputScrollSpeedView()
            
            Divider()
            
            Text(song.name)
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            DetailTableView(rows: SongDetailUtil().toDetailRows(song: song, highSpeedValue: modelData.scrollSpeed))
            Spacer()
        }
    }
}

struct SongDetailView_Previews: PreviewProvider {
    static var previews: some View {
        SongDetailView(song:  Song(id: 1, name: "TestSongName", composer: nil, version: "testVersion", display_bpm: "123-456", min_bpm: 123, max_bpm: 456, base_bpm: 345, sub_bpm: 234, besp: 1, bsp: 2, dsp: 3, esp: 4, csp: 5, bdp: 6, ddp: 7, edp: 8, cdp: 9, shock_arrow: nil, deleted: nil))
            .environmentObject(ModelData())
    }
}