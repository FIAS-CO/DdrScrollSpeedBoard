import SwiftUI
import shared

struct SearchCourseView: View {
    @FocusState var isKeyboardVisible: Bool
    @EnvironmentObject var modelData: ModelData
    @Binding var isShowSubView: Bool
    @State private var searchWord: String = ""
    @State private var selectedCourse: Course?
    
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
                            modelData.searchCourse(searchWord: newValue)
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
                    ForEach(modelData.courses, id: \.self) { course in
                        Button(action: {
                            selectedCourse = course
                            isShowSubView = true
                        }, label: {
                            HStack {
                                Text(course.name)
                                Spacer()
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.gray)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        })
                    }
                }
                .navigationTitle("コース検索")
                
                UnderlineBannerView()
                HStack {
                    Spacer()
                    if (modelData.updateAvailable) {
                        Button(action: {
                            modelData.downloadSongData()
                            modelData.searchCourse(searchWord: searchWord)
                        }){ Text("Push to update course data.") }
                    } else {
                        Text(modelData.versionText)
                    }
                }
            }
            .background(
                //                NavigationLink(
                //                    destination: selected != nil ? SongDetailView(song: selectedSong!) : nil,
                //                    isActive: $isShowSubView,
                //                    label: {}
                //                )
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

struct SearchCourseView_Previews: PreviewProvider {
    static var previews: some View {
        // Workaround: リンクがプレビューでうまく動作しない。
        // isShowSubViewでリンク先に遷移するのでそれの再現として２つ配置しています
        
        let courses = [
            Course(id: 0, name:"course1", is_dan: 0, first_song_id: 1, first_song_property_id: -1, second_song_id: 2, second_song_property_id: -1, third_song_id: 3, third_song_property_id: -1, fourth_song_id: 4, fourth_song_property_id: -1, deleted: 0),
            Course(id: 2, name:"dan1", is_dan: 1, first_song_id: 1, first_song_property_id: -1, second_song_id: 2, second_song_property_id: -1, third_song_id: 3, third_song_property_id: -1, fourth_song_id: 4, fourth_song_property_id: -1, deleted: 0),
        ]
        SearchCourseView(isShowSubView: .constant(false))
            .environmentObject(ModelData(isPreviewMode: true, coursesForPreview: courses))
        //
        //        SearchCourseView(isShowSubView: .constant(true))
        //            .environmentObject(ModelData(isPreviewMode: true, songsForPreview: courses))
    }
}
