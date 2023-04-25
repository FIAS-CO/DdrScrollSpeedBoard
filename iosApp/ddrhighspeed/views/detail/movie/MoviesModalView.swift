import SwiftUI
import YouTubePlayerKit

struct MoviesModalView: View {
    @Binding var isPresented: Bool
    let urls: [String] = ["https://www.youtube.com/watch?v=djtlC4Ykzpw", "https://www.youtube.com/watch?v=ue80QwXMRHg", "https://www.youtube.com/watch?v=7C2z4GqqS5E", "https://www.youtube.com/watch?v=5oW7rNAIiLs", "https://www.youtube.com/watch?v=JX9YmI6tVvM"]
    
    let labels: [String] = ["Single Difficult",
    "Single Expert",
    "Single Challenge",
    "Double Expert",
    "Double Challenge"]
    
    let topViewId = "top"
    
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
            ScrollViewReader { reader in
                ScrollView(.vertical) {
                    VStack() {
                        ForEach(0..<urls.count) { index in
                            MovieAccordionView(url: urls[index], label: labels[index])
                                .id(index)
                        }
                    }
                    .padding()
                }
            }
        }
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct MoviesModalView_Previews: PreviewProvider {
    static var previews: some View {
        MoviesModalView(isPresented: .constant(true))
    }
}

