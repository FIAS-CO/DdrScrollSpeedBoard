import SwiftUI
import shared

struct ContentView: View {
    @EnvironmentObject var modelData: ModelData
    @State private var searchWord: String = ""
    
    var body: some View {
        NavigationView {
            VStack {
                TextField("曲名を入力してください", text: $searchWord)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding(.horizontal)
                    .onChange(of: searchWord) { newValue in
                        modelData.searchSong(searchWord: newValue)
                    }
                List{
                    ForEach(modelData.songs, id: \.self) { song in
                        NavigationLink {
                            SongDetailView(song: song)
                        } label: {
                            Text(song.name)
                        }
                    }
                }
                .navigationTitle("曲名検索")
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(ModelData())
    }
}
