import SwiftUI
import shared

struct ContentView: View {
    @State private var isShowSongSubView = false
    @State private var isShowCourseSubView = false
    
    @State private var selection: Tab = .song_search
    
    enum Tab {
        case rough_calc
        case song_search
        case course_search
    }
    
    init() {
        // Workaround iOS15でTabViewの背景が透明になった。Tabアイコンが見辛いので背景色を設定する
        // https://shtnkgm.com/blog/2021-08-18-ios15.html#%E3%82%BF%E3%83%95%E3%82%99%E3%83%8F%E3%82%99%E3%83%BC%E3%81%8B%E3%82%99%E9%80%8F%E6%98%8E%E3%81%AB%E3%81%AA%E3%82%8B%E3%80%81%E8%83%8C%E6%99%AF%E8%89%B2%E3%81%8B%E3%82%99%E9%81%A9%E7%94%A8%E3%81%95%E3%82%8C%E3%81%AA%E3%81%84
        if #available(iOS 15.0, *) {
            let appearance = UITabBarAppearance()
            appearance.backgroundColor = .white
            UITabBar.appearance().scrollEdgeAppearance = appearance
        }
    }
    
    // TODO アイコンとタブ名を検討
    var body: some View {
        let selectedTab = Binding<Tab>(get: {
            selection
        }, set: {
            if $0 == selection {
                if (selection == Tab.song_search) {
                    isShowSongSubView = false
                } else if (selection == Tab.course_search) {
                    isShowCourseSubView = false
                }
            }
            self.selection = $0
        })
        
        TabView(selection: selectedTab) {
            SearchSongView(isShowSubView: $isShowSongSubView)
                .tabItem {
                    Label("曲名検索", systemImage: "magnifyingglass")
                }
                .tag(Tab.song_search)
            
            SearchCourseView(isShowSubView: $isShowCourseSubView)
                .tabItem {
                    Label("コース検索", systemImage: "magnifyingglass")
                }
                .tag(Tab.course_search)
            
            RoughEstimateView()
                .tabItem {
                    Label("BPM簡易計算", systemImage: "list.bullet")
                }
                .tag(Tab.rough_calc)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(ModelData())
    }
}
