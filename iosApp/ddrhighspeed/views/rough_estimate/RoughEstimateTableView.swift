import SwiftUI
import shared

struct RoughEstimateTableView: View {

    var items: [ResultRow]
    var body: some View {
        VStack(spacing: 0) {
            HStack(spacing: 0) {
                Text("BPM")
                    .frame(maxWidth: .infinity)
                    .frame(width: UIScreen.main.bounds.width * 2 / 6)
                Rectangle()
                    .fill(Color.gray.opacity(0.5))
                    .frame(width: 1)
                Text("ハイスピ")
                    .frame(maxWidth: .infinity)
                    .frame(width: UIScreen.main.bounds.width * 1.5 / 6)
                Rectangle()
                    .fill(Color.gray.opacity(0.5))
                    .frame(width: 1)
                Text("ｽｸﾛｰﾙｽﾋﾟｰﾄﾞ")
                    .frame(maxWidth: .infinity)
                    .frame(width: UIScreen.main.bounds.width * 2.5 / 6)
            }
            .font(.headline)
            .padding()
            .frame(maxWidth: .infinity)
            .background(Color(UIColor.systemGray6))
            .fixedSize(horizontal: false, vertical: true) // make header same height as data row
            .overlay(
                Rectangle()
                    .fill(Color.gray.opacity(0.5))
                    .frame(height: 1),
                alignment: .bottom
            )
            .overlay(
                Rectangle()
                    .fill(Color.gray.opacity(0.5))
                    .frame(height: 1),
                alignment: .top
            )
            
            List {
                ForEach(items, id: \.self) { item in
                    HStack(spacing: 0) {
                        Text(item.bpmRange)
                            .frame(maxWidth: .infinity)
                            .frame(width: UIScreen.main.bounds.width * 2 / 6)
                        Rectangle()
                            .fill(Color.gray.opacity(0.5))
                            .frame(width: 1)
                        Text(String(item.highSpeed))
                            .frame(maxWidth: .infinity)
                            .frame(width: UIScreen.main.bounds.width * 1.5 / 6)
                        Rectangle()
                            .fill(Color.gray.opacity(0.5))
                            .frame(width: 1)
                        Text(String(item.scrollSpeedRange))
                            .frame(maxWidth: .infinity)
                            .frame(width: UIScreen.main.bounds.width * 2.5 / 6)
                    }
                    .padding(.vertical, 8)
                    .overlay(
                        Rectangle()
                            .fill(Color.gray.opacity(0.5))
                            .frame(height: 1),
                        alignment: .bottom
                    )
                }
                .listRowSeparator(.hidden)
            }
            .listStyle(.plain)
            .padding(.horizontal, -16)
        }
    }
}


struct RoughEstimateTableView_Previews: PreviewProvider {
    static var previews: some View {
        RoughEstimateTableView(items: [
            .init(bpmRange: "2300 ~ 3400",  highSpeed: "1.5", scrollSpeedRange: "4500.6 ~ 5600.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
            .init(bpmRange: "230 ~ 340",  highSpeed: "1.5", scrollSpeedRange: "450.6 ~ 560.6"),
        ])
        RoughEstimateTableView(items: [
            .init(bpmRange: "-",  highSpeed: "1.5", scrollSpeedRange: "-"),
            .init(bpmRange: "-",  highSpeed: "1.5", scrollSpeedRange: "-"),
            .init(bpmRange: "-",  highSpeed: "1.5", scrollSpeedRange: "-"),
        ])
    }
}
