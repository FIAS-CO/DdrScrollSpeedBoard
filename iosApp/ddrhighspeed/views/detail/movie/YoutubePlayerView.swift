import SwiftUI
import YouTubePlayerKit

struct YoutubePlayerView: View {
    var movieId: String
    var body: some View {
        let url = "https://www.youtube.com/watch?v=\(movieId)"
        let youTubePlayer = YouTubePlayer(
            source: .url(url))
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
    }
}

struct YoutubePlayerView_Previews: PreviewProvider {
    static var previews: some View {
        YoutubePlayerView(movieId: "lJWmSeEbAkM")
    }
}
