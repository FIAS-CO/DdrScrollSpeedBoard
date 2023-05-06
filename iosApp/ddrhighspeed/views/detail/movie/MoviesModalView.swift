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
                    ForEach(MovieUtil().sortByDifficulty(movies: movies), id: \.self) { movie in
                        MovieAccordionView(movie: movie)
                    }
                    SearchMovieButtonView(searchWord: songName, label: "Youtubeで検索")
                        .font(.headline)
                        .padding()
                        .frame(maxWidth: .infinity) // 横幅を画面いっぱいに広げる
                        .background(
                            RoundedRectangle(cornerRadius: 5)
                                .foregroundStyle(.ultraThinMaterial)
                                .shadow(color: .init(white: 0.4, opacity: 0.4), radius: 3, x: 0, y: 0)
                        )
                        .overlay(
                            RoundedRectangle(cornerRadius: 30)
                                .stroke(Color.init(white: 1, opacity: 0.5), lineWidth: 1)
                        )
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
        [Movie(song_id:559, difficulty: "DDP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "BSP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "DSP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "ESP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "CSP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "BESP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "CSP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "CDP", site:"Youtube", movie_id: "RfoZA2mNPRM"),
         Movie(song_id:559, difficulty: "BDP", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "test", site:"Youtube", movie_id: "lJWmSeEbAkM"),
         Movie(song_id:559, difficulty: "EDP", site:"Youtube", movie_id: "lJWmSeEbAkM")]
        
        MoviesModalView(movies: movies, songName:"Paranoia Revolution")
    }
}
