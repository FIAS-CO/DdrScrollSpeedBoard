import SwiftUI
import UIKit

struct InputScrollSpeedView: View {
    @FocusState var isKeyboardVisible: Bool
    
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
                    .focused($isKeyboardVisible)
                
                // TODO: 0未満にならないようにする
                LongPushableButton(imageName: "plus.square", action: {
                    // テキストボックスにフォーカスがあっていると＋ーボタンが効かない
                    // フォーカスフラグを変更した直後も効かないので、キーボードが開いているときは少し待機してから値を変更する
                    executeWithCloseKeyboard(action: {
                        modelData.scrollSpeed += 1
                    })
                })
                LongPushableButton(imageName: "minus.square", action: {
                    executeWithCloseKeyboard(action: {
                        modelData.scrollSpeed -= 1
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
    
    func executeWithCloseKeyboard(action: @escaping ()->Void) {
        if (isKeyboardVisible) {
            isKeyboardVisible = false
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                // 0.1秒待機
                print("close keyboard")
                action()
            }
        }
        action()
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
        InputScrollSpeedView(isKeyboardVisible: FocusState())
            .environmentObject(ModelData())
    }
}
