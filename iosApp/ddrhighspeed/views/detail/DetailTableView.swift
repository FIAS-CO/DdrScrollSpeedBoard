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
        Text("")
            .padding(.trailing)
            .frame(
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .leading
            )
            .font(.caption)
            .padding(8)
            .background(.thinMaterial)
            .overlay(alignment: .top) { VStack { Divider() } }
            .overlay(alignment: .bottom) { VStack { Divider() } }
            .overlay(alignment: .leading) { HStack { Divider() } }
            .overlay(alignment: .trailing) { HStack { Divider() } }
            .simpleTableHeader()
            .zIndex(3)
        
        Group {
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
        Text(row.category)
            .padding(.trailing)
            .frame(
                maxWidth: .infinity,
                maxHeight: .infinity,
                alignment: .leading
            )
            .padding(8)
            .background(.thinMaterial)
            .overlay(alignment: .bottom) { VStack { Divider() } }
            .overlay(alignment: .leading) { HStack { Divider() } }
            .overlay(alignment: .trailing) { HStack { Divider() } }
            .simpleTableHeaderColumn()
            .zIndex(1)
        
        Group {
            Text(row.bpm)
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text(row.highSpeed)
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
            
            Text(row.scrollSpeed)
                .padding(.trailing)
                .frame(
                    maxWidth: .infinity,
                    maxHeight: .infinity,
                    alignment: .leading
                )
        }
        .padding(.horizontal)
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
