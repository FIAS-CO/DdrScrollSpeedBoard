import SwiftUI
import SimpleTable
import shared

struct DetailTableView: View {

    var rows: [ResultRowForDetail]
    
    var body: some View {
        SimpleTableView {
            SimpleTableLayout(columnsCount: 4) {
                columnHeaders()
                
                ForEach(rows, id: \.self) { r in
                    row(r)
                }
            }
        }
    }
    
    @ViewBuilder
    func columnHeaders() -> some View {
        Group {
            Text("")
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
                .padding(8)
                .simpleTableHeader()
                .zIndex(3)
            
            Text("BPM")
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text("ハイスピ")
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text("ｽｸﾛｰﾙｽﾋﾟｰﾄﾞ")
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
        }
        .font(.caption)
        .padding(.horizontal)
        .background(.thinMaterial)
        .overlay(alignment: .top) { VStack { Divider() } }
        .overlay(alignment: .bottom) { VStack { Divider() } }
        .overlay(alignment: .trailing) { HStack { Divider() } }
        .simpleTableHeaderRow()
        .zIndex(2)
    }
    
    @ViewBuilder
    func row(_ row: ResultRowForDetail) -> some View {
        Group {
            Text(row.category)
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
                .padding(8)
                .background(.thinMaterial)
                .overlay(alignment: .leading) { HStack { Divider() } }
                .zIndex(1)
            
            Text(row.bpm)
                .padding(.horizontal)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text(row.highSpeed)
                .padding(.horizontal)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text(row.scrollSpeed)
                .padding(.horizontal)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
        }
        .overlay(alignment: .bottom) { VStack { Divider() } }
        .overlay(alignment: .trailing) { HStack { Divider() } }
    }
}

struct DetailTableView_Previews: PreviewProvider {
    static var previews: some View {
        DetailTableView(rows: [
            .init(category: "最大", bpm: "230",  highSpeed: "1.5", scrollSpeed: "450.6"),
            .init(category: "基本②", bpm: "230",  highSpeed: "1.5", scrollSpeed: "450"),
            .init(category: "基本①", bpm: "230",  highSpeed: "1.5", scrollSpeed: "450"),
            .init(category: "最小", bpm: "230",  highSpeed: "1.5", scrollSpeed: "450"),
        ])
    }
}
