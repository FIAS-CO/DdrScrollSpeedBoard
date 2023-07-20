import SwiftUI
import shared

struct SearchSongView: View {
    @EnvironmentObject var modelData: ModelData
    @Binding var isShowSubView: Bool
    @State private var searchWord: String = ""
    @State private var selectedSong: Song?

    var body: some View {
        NavigationView {
            VStack {
                HStack {
                    TextField("曲名を入力してください", text: $searchWord)
                        .modifier(TextFieldClearButton(text: $searchWord))
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .padding(.horizontal)
                        .onChange(of: searchWord) { newValue in
                            modelData.searchSong(searchWord: newValue)
                        }
                }
                List{
                    ForEach(modelData.songs, id: \.self) { song in
                        Button(action: {
                            selectedSong = song
                            isShowSubView = true
                        }, label: {
                            HStack {
                                Text(song.name)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.gray)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        })
                    }
                }
                .navigationTitle("曲名検索")

                HStack {
                    Spacer()
                    if (modelData.updateAvailable) {
                        Button(action: {
                            modelData.downloadSongData()
                        }){ Text("Push to update song data.") }
                    } else {
                        Text(modelData.versionText)
                    }
                }

                UnderlineBannerView()
            }
            .background(
                NavigationLink(
                    destination: selectedSong != nil ? SongDetailView(song: selectedSong!) : nil,
                    isActive: $isShowSubView,
                    label: {}
                )
            )
        }
        .alert(isPresented: $modelData.showingAlert) {
            Alert(
                title: Text("Error"),
                message: Text("An error occurred."),
                dismissButton: .default(Text("OK")) {
                    // ここでは単にアラートを非表示にする
                    modelData.showingAlert = false
                }
            )
        }
    }
}

struct SearchSongView_Previews: PreviewProvider {
    static var previews: some View {
        // Workaround: リンクがプレビューでうまく動作しない。
        // isShowSubViewでリンク先に遷移するのでそれの再現として２つ配置しています
        SearchSongView(isShowSubView: .constant(false))
            .environmentObject(ModelData())
        
        SearchSongView(isShowSubView: .constant(true))
            .environmentObject(ModelData())
    }
}
