import SwiftUI
import YouTubePlayerKit
import shared

struct MoviesModalView: View {
    var movies:[Movie]
    var songName:String
    
    var body: some View {
        VStack {
            ScrollView(.vertical) {
                VStack() {
                    ForEach(movies, id: \.self) { movie in
                        MovieAccordionView(movie: movie)
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
        
        MoviesModalView(movies: movies, songName:"Paranoia Revolution")
    }
}

