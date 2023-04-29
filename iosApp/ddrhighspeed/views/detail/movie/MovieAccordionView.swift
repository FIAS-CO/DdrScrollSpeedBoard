import SwiftUI
import shared

struct MovieAccordionView: View {
    var movie: Movie
    var songName: String
    
    @State private var showDetail = false
    
    var body: some View {
        let (label, color) = MovieUtil().getLabel(difficulty: movie.difficulty)
        
        VStack(alignment: .leading, spacing: 2) {
            AccordionHeaderView(showDetail: $showDetail, label: label, color: color)
            
            if showDetail {
                YoutubePlayerView(movieId: movie.movie_id)
                    .background(Color(.systemBackground))
                    .transition(.move(edge: .trailing).combined(with: .opacity))
            }
        }
    }
}

struct MovieAccordion_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "BESP", site:"Youtube", movie_id: "lJWmSeEbAkM"), songName: "over the \"period\"")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "BSP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "DSP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "ESP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "CSP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "BDP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "DDP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "EDP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
            MovieAccordionView(movie: Movie(song_id:559, difficulty: "CDP", site:"Youtube", movie_id:"RfoZA2mNPRM"), songName: "灼熱Beach side bunny")
        }
    }
}
