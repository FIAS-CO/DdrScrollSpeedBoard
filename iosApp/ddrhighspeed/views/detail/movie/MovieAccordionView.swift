import SwiftUI
import YouTubePlayerKit

struct MovieAccordionView: View {
    var url: String
    var label: String
    
    @State private var showDetail = false
    
    var body: some View {
        VStack(alignment: .leading, spacing: -10) {
            HStack {
                Text(label)
                    .font(.title)
                    .bold()
                Spacer()
                Button {
                    withAnimation{
                        showDetail.toggle()
                    }
                } label: {
                    Label("Graph", systemImage: "chevron.right.circle")
                        .labelStyle(.iconOnly)
                        .imageScale(.large)
                        .rotationEffect(.degrees(showDetail ? 90 : 0))
                        .scaleEffect(showDetail ? 1.5 : 1)
                        .padding()
                }
            }
            if showDetail {
                let youTubePlayer = YouTubePlayer(
                    source: .url(url))
                HStack {
                    YouTubePlayerView(youTubePlayer) { state in
                        // Overlay ViewBuilder closure to place an overlay View
                        // for the current `YouTubePlayer.State`
                        switch state {
                        case .idle:
                            ProgressView()
                        case .ready:
                            EmptyView()
                        case .error(_):
                            Text(verbatim: "YouTube player couldn't be loaded")
                        }
                    }
                    .frame(height:225)
                    .background(Color(.systemBackground))
                    .transition(.move(edge: .trailing).combined(with: .opacity))
                }
            }
        }
    }
}

struct MovieAccordion_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            MovieAccordionView(url: "https://www.youtube.com/watch?v=djtlC4Ykzpw", label: "Signle Expert")
            MovieAccordionView(url: "https://www.youtube.com/watch?v=djtlC4Ykzpw", label: "Signle Challenge")
        }
    }
}
