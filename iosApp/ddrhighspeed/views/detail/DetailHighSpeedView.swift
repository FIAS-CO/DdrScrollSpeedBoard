import SwiftUI
import shared

struct DetailHighSpeedView: View {
    @EnvironmentObject var modelData: ModelData
    
    var song: SongData
    
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
        let data: ModelData = ModelData(isPreviewMode: true)
        data.scrollSpeed = "123"
        
        return DetailHighSpeedView(song: SongData(id: 1, name: "TestSongName", composer: "", version: "testVersion", displayBpm: "123-456", baseBpm: 123, subBpm: 456, minBpm: 345, maxBpm: 234, besp: 1, bsp: 2, dsp: 3, esp: 4, csp: 5, bdp: 6, ddp: 7, edp: 8, cdp: 9, shockArrow: "CSP,CDP", deleted: 0, difficultyLabel: "test"))
            .environmentObject(data)
    }
}
