import SwiftUI
import UIKit

struct InputScrollSpeedView: View {
    
    @EnvironmentObject var modelData: ModelData
    @State private var timer: Timer?
    @State var isLongPressing = false
    
    var body: some View {
        VStack {
            Text("スクロールスピード")
                .font(.title)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, maxHeight: 50, alignment: .leading)
            HStack {
                TextField("Enter high speed value", value: $modelData.scrollSpeed, formatter: NumberFormatter())
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding(.horizontal)
                    .frame(height: 50)

                // TODO: 0未満にならないようにする
                LongPushableButton(imageName: "plus.square", action: { modelData.scrollSpeed += 1})
                LongPushableButton(imageName: "minus.square", action: { modelData.scrollSpeed -= 1})
            }
            .padding(.bottom)
        }
        .background(Color(UIColor.systemGray6))
        .onDisappear {
            UserDefaults.standard.set(modelData.scrollSpeed, forKey: "scrollSpeed")
        }
    }
}

struct CustomButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(.white)
            .frame(minWidth: 0, maxWidth: .infinity)
            .padding()
            .background(Color.blue)
            .cornerRadius(10)
            .scaleEffect(configuration.isPressed ? 0.95 : 1.0)
    }
}

struct InputScrollSpeedView_Previews: PreviewProvider {
    static var previews: some View {
        InputScrollSpeedView()
            .environmentObject(ModelData())
    }
}
