import SwiftUI

struct SearchMovieButtonView: View {
    var searchWord: String
    var label: String
    
    var body: some View {
        // 空白を"+"に変換したり、ダブルクォーテーションをうまいことしたりする
        let encodedQuery =
        ("DDR " + searchWord).addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        
        let youtubeURL = "https://www.youtube.com/results?search_query=\(encodedQuery)"
        Button(action: {
            guard let url = URL(string: youtubeURL) else { return }
            if UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            }
        }) {
            HStack{
                Image(systemName: "magnifyingglass")
                Text(label)
            }
        }
    }
}

struct SearchMovieButtonView_Previews: PreviewProvider {
    static var previews: some View {
        SearchMovieButtonView(searchWord: "test", label: "label")
    }
}
