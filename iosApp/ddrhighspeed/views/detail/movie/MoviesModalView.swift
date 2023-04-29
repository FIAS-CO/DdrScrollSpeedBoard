import SwiftUI
import YouTubePlayerKit
import shared

struct MoviesModalView: View {
    @Binding var isPresented: Bool
    
    var movies:[Movie]
    var songName:String
    
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
            ScrollView(.vertical) {
                VStack() {
                    ForEach(movies, id: \.self) { movie in
                        MovieAccordionView(movie: movie, songName: songName)
                    }
                    SearchMovieButtonView(searchWord: songName, label: "Youtubeで検索")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.red)
                        .cornerRadius(10)
                }
                .padding()
            }
        }
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct MoviesModalView_Previews: PreviewProvider {
    static var previews: some View {
        let movies =
        [Movie(song_id:559, difficulty: "CSP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "CDP", site:"Youtube", movie_id:"RfoZA2mNPRM"),]
        MoviesModalView(isPresented: .constant(true), movies: movies, songName:"Paranoia Revolution")
    }
}

