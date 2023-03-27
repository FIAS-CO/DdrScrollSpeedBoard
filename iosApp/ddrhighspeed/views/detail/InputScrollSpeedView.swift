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
                Button(action: {
                    print("tap")
                    if(self.isLongPressing){
                        //this tap was caused by the end of a longpress gesture, so stop our fastforwarding
                        self.isLongPressing.toggle()
                        self.timer?.invalidate()
                    } else {
                        //just a regular tap
                        modelData.scrollSpeed += 1
                    }
                }, label: {
                    // TODO: アイコンを変える
                    Image(systemName: self.isLongPressing ? "chevron.right.2": "chevron.right")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 30, height: 30)
                })
                .simultaneousGesture(LongPressGesture(minimumDuration: 0.2).onEnded { _ in
                    print("long press")
                    self.isLongPressing = true
                    //or fastforward has started to start the timer
                    self.timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { _ in
                        modelData.scrollSpeed += 1
                    })
                })
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
