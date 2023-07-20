import SwiftUI
import shared

struct DetailHighSpeedView: View {
    @EnvironmentObject var modelData: ModelData
    
    var song: Song
    
    var body: some View {
        VStack {
            InputScrollSpeedView()
                .padding(.all, 0)
            
            DetailTableView(rows: SongDetailUtil().toDetailRows(song: song, highSpeedValue: modelData.getScrollSpeedInt()))
                .fixedSize(horizontal: true, vertical: true)
                .padding(.leading)
                .padding(.horizontal)
            
            Spacer()
        }
    }
}

struct DetailHighSpeedView_Previews: PreviewProvider {
    static var previews: some View {
        let data: ModelData = ModelData()
        data.scrollSpeed = "123"
        
        return DetailHighSpeedView(song: Song(id: 1, name: "TestSongName", composer: "", version: "testVersion", display_bpm: "123-456", min_bpm: 123, max_bpm: 456, base_bpm: 345, sub_bpm: 234, besp: 1, bsp: 2, dsp: 3, esp: 4, csp: 5, bdp: 6, ddp: 7, edp: 8, cdp: 9, shock_arrow: "CSP,CDP", deleted: 0))
            .environmentObject(data)
    }
}
