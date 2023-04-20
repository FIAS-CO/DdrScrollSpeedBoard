import SwiftUI
import YouTubePlayerKit

struct MovieModalView: View {
    @Binding var isPresented: Bool
    var str: String = "https://www.youtube.com/watch?v=djtlC4Ykzpw"
    
    var body: some View {
        VStack {
            HStack {
                Spacer()
                Button(action: {
                    self.isPresented.toggle()
                }) {
                    Image(systemName: "xmark")
                        .font(.title)
                        .foregroundColor(.gray)
                }
                .padding()
            }
            Spacer()
            
            VStack {
                GeometryReader { geom in
                    YouTubePlayerView(
                        YouTubePlayer(
                            source: .url(str)
                        )
                    ) { state in
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
                    .frame(width: geom.size.width)
                }
            }
            .frame(width: UIScreen.main.bounds.height*0.85, height: UIScreen.main.bounds.width)
            .rotationEffect(.degrees(90), anchor: .center)
            
            Spacer()
        }
        .background(Color.white)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct MovieModalView_Previews: PreviewProvider {
    static var previews: some View {
        MovieModalView(isPresented: .constant(true))
    }
}
