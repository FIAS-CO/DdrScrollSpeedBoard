import SwiftUI

struct AccordionHeaderView: View {
    @Binding var showDetail:Bool
    var label: String
    var color: Color
    
    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            HStack {
                HStack {
                    Text("■")
                        .foregroundColor(color)
                    Text(label)
                        .font(.title)
                        .bold()
                    Spacer()
                }
                
                Button {
                    withAnimation{
                        showDetail.toggle()
                    }
                } label: {
                    Label("Graph", systemImage: "chevron.right.circle")
                        .labelStyle(.iconOnly)
                        .imageScale(.large)
                        .rotationEffect(.degrees(showDetail ? 90 : 0))
                        .scaleEffect(showDetail ? 1.5 : 1)
                }
            }
            Divider()
                .background(color)
        }
    }
}

struct AccordionView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            AccordionHeaderView(showDetail: .constant(false), label: "String", color: .red)
            AccordionHeaderView(showDetail: .constant(true), label: "String", color: .red)
        }
    }
}
