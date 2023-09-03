import SwiftUI
import shared

struct SearchSongView: View {
    @FocusState var isKeyboardVisible: Bool
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
                        .focused($isKeyboardVisible)
                        .padding(.horizontal)
                        .onChange(of: searchWord) { newValue in
                            modelData.searchSong(searchWord: newValue)
                        }
                        .toolbar {
                            ToolbarItemGroup(placement: .keyboard) {
                                Spacer()
                                Button(action: {
                                    isKeyboardVisible = false
                                }, label: {
                                    Text("Close")
                                })
                            }
                        }
                }
                List{
                    ForEach(modelData.songs, id: \.self) { song in
                        Button(action: {
                            selectedSong = song
                            isShowSubView = true
                        }, label: {
                            HStack {
                                Text(song.nameWithDifficultyLabel())
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.gray)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        })
                    }
                }
                .navigationTitle("曲名検索")
                
                UnderlineBannerView()
                HStack {
                    Spacer()
                    if (modelData.updateAvailable) {
                        Button(action: {
                            modelData.downloadSongData()
                            modelData.searchSong(searchWord: searchWord)
                        }){ Text("Push to update song data.") }
                    } else {
                        Text(modelData.versionText)
                    }
                }
            }
            .background(
                NavigationLink(
                    destination: selectedSong != nil ? SongDetailView(song: selectedSong!) : nil,
                    isActive: $isShowSubView,
                    label: {}
                )
            )
            .ignoresSafeArea(.keyboard, edges: .bottom)
        }
        .alert(isPresented: $modelData.showingAlert) {
            Alert(
                title: Text("データ更新に失敗しました"),
                message: Text(modelData.alertMessage),
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
        
        let songs = [
            Song(id: 0, name:"name1", composer:"comp", version: "A3", display_bpm: "123-345", min_bpm: 123.0, max_bpm: 345.0, base_bpm: 234.0, sub_bpm: 345.0, besp: 1, bsp: 1, dsp: 2, esp: 3, csp: 4, bdp: 5, ddp: 6, edp: 7, cdp: 8, shock_arrow: "", deleted: 0, difficulty_label: "test"),
            Song(id: 1, name:"name2", composer:"comp", version: "A3", display_bpm: "123-345", min_bpm: 123.0, max_bpm: 345.0, base_bpm: 234.0, sub_bpm: 345.0, besp: 1, bsp: 1, dsp: 2, esp: 3, csp: 4, bdp: 5, ddp: 6, edp: 7, cdp: 8, shock_arrow: "", deleted: 0, difficulty_label: nil),
            Song(id: 2, name:"name3", composer:"comp", version: "A3", display_bpm: "123-345", min_bpm: 123.0, max_bpm: 345.0, base_bpm: 234.0, sub_bpm: 345.0, besp: 1, bsp: 1, dsp: 2, esp: 3, csp: 4, bdp: 5, ddp: 6, edp: 7, cdp: 8, shock_arrow: "", deleted: 1, difficulty_label: "test")
        ]
        SearchSongView(isShowSubView: .constant(false))
            .environmentObject(ModelData(isPreviewMode: true, songsForPreview: songs))
        
        SearchSongView(isShowSubView: .constant(true))
            .environmentObject(ModelData(isPreviewMode: true, songsForPreview: songs))
    }
}
