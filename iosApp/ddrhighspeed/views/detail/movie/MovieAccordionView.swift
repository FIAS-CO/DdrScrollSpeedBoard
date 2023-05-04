import SwiftUI
import shared

struct MovieAccordionView: View {
    var movie: Movie
    
    @State private var showDetail = true
    
    var body: some View {
        let (label, color) = MovieUtil().getLabel(difficulty: movie.difficulty)
        
        VStack(alignment: .leading, spacing: 2) {
            AccordionHeaderView(showDetail: $showDetail, label: label, color: color)
            
            if showDetail {
                YoutubePlayerView(movieId: movie.movie_id)
                    .background(Color(.systemBackground))
                    .transition(.move(edge: .trailing).combined(with: .opacity))
                    .padding(.leading)
                    .padding(.bottom)
            }
        }
    }
}

struct MovieAccordion_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            ScrollView {
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "BESP", site:"Youtube", movie_id: "lJWmSeEbAkM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "BSP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "DSP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "ESP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "CSP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "BDP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "DDP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "EDP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
                MovieAccordionView(movie: Movie(song_id:559, difficulty: "CDP", site:"Youtube", movie_id:"RfoZA2mNPRM"))
            }
        }
    }
}
