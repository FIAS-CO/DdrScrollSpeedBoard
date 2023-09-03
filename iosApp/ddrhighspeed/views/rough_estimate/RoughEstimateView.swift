import SwiftUI
import shared

struct RoughEstimateView: View {
    @EnvironmentObject var modelData: ModelData
    var rows: [ResultRow] = []
    
    var body: some View {
        VStack {
            InputScrollSpeedView()
            
            Divider()
            RoughEstimateTableView(
                items:SongDetailUtil().toRoughEstimateRows(highSpeedValue: modelData.getScrollSpeedInt()))
            
            UnderlineBannerView()
        }
        .ignoresSafeArea(.keyboard, edges: .bottom)
    }
}

struct RoughEstimateView_Previews: PreviewProvider {
    static var previews: some View {
        RoughEstimateView()
            .environmentObject(ModelData(isPreviewMode: true))
    }
}
