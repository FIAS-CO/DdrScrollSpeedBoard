import SwiftUI
import shared

struct SearchCourseView: View {
    @FocusState var isKeyboardVisible: Bool
    @EnvironmentObject var modelData: ModelData
    @Binding var isShowSubView: Bool
    @State private var searchWord: String = ""
    
    // Workaround: Preview用。画面遷移時の表示テストのために設定している。
    @State private var selectedCourse: CourseData? = Course(id: 0, name:"course1", is_dan: 0, first_song_id: 1, first_song_property_id: -1, second_song_id: 2, second_song_property_id: -1, third_song_id: 3, third_song_property_id: -1, fourth_song_id: 4, fourth_song_property_id: -1, deleted: 0).convertToCourseData()
    
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
                    Section {
                        if modelData.courses.isEmpty {
                            Text("上の\"PUSH TO UPDATE COURSE DATA.\"をタップしてデータを更新してください")
                            Text("Now Loadingと表示されている場合は少しお待ちください")
                        } else {
                            ForEach(modelData.courses, id: \.self) { course in
                                Button(action: {
                                    selectedCourse = course
                                    isShowSubView = true
                                }, label: {
                                    HStack {
                                        Text(course.getCourseLabel())
                                            .foregroundColor(.primary)
                                        Spacer()
                                        Image(systemName: "chevron.right")
                                            .foregroundColor(.gray)
                                    }
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                })
                            }
                        }
                    } header: {
                        HStack {
                            if (modelData.isLoading || (!modelData.updateAvailable && !modelData.courses.isEmpty)) {
                                Text(modelData.versionText)
                            } else {
                                Button(action: {
                                    modelData.downloadSongData()
                                    modelData.searchSong(searchWord: searchWord)
                                }){ Text("Push to update song data.") }
                            }
                            
                            Spacer()
                        }
                    }
                }
                .listStyle(InsetGroupedListStyle())
                
                UnderlineBannerView()
            }
            .navigationTitle("コース検索")
            .background(
                NavigationLink(
                    destination: selectedCourse != nil ? CourseDetailView(course: selectedCourse!) : nil,
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

struct SearchCourseView_Previews: PreviewProvider {
    static var previews: some View {
        // Workaround: リンクがプレビューでうまく動作しない。
        // isShowSubViewでリンク先に遷移するのでそれの再現として２つ配置しています
        let course = CourseData(id: 0, name:"course1", isDan: false, firstSongId: 1, firstSongPropertyId: -1, secondSongId: 2, secondSongPropertyId: -1, thirdSongId: 3, thirdSongPropertyId: -1, fourthSongId: 4, fourthSongPropertyId: -1, isDeleted: false)
        let courses = [
            course,
            CourseData(id: 2, name:"dan1", isDan: true, firstSongId: 1, firstSongPropertyId: -1, secondSongId: 2, secondSongPropertyId: -1, thirdSongId: 3, thirdSongPropertyId: -1, fourthSongId: 4, fourthSongPropertyId: -1, isDeleted: false),
        ]
        SearchCourseView(isShowSubView: .constant(false))
            .environmentObject(ModelData(isPreviewMode: true, coursesForPreview: courses))
        
        SearchCourseView(isShowSubView: .constant(true))
            .environmentObject(ModelData(isPreviewMode: true, coursesForPreview: courses))
    }
}
