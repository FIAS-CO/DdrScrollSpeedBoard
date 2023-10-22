import SwiftUI
import UIKit

struct InputScrollSpeedView: View {
    @FocusState var isKeyboardVisible: Bool
    
    @EnvironmentObject var modelData: ModelData
    @State private var timer: Timer?
    @State var isLongPressing = false
    
    var body: some View {
        VStack(spacing: -10) {
            Text("スクロールスピード")
                .font(.title2)
                .fontWeight(.bold)
                .frame(maxWidth: .infinity, maxHeight: 50, alignment: .leading)
            
            HStack {
                TextField("希望するBPM×ハイスピの値を入力", text: $modelData.scrollSpeed)
                    .keyboardType(.numberPad)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .frame(height: 50)
                    .focused($isKeyboardVisible)
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
                
                // TODO: 0未満にならないようにする
                LongPushableButton(imageName: "plus.square", action: {
                    // テキストボックスにフォーカスがあっていると＋ーボタンが効かない
                    // フォーカスフラグを変更した直後も効かないので、キーボードが開いているときは少し待機してから値を変更する
                    executeWithCloseKeyboard(action: {
                        modelData.scrollSpeed = addOne(str: modelData.scrollSpeed)
                    })
                })
                LongPushableButton(imageName: "minus.square", action: {
                    executeWithCloseKeyboard(action: {
                        modelData.scrollSpeed = minusOne(str: modelData.scrollSpeed)
                    })
                })
            }
            .padding(.bottom)
            .padding(.leading)
        }
        .padding(.horizontal)
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
    
    func addOne(str: String) -> String {
        // 文字列を数値に変換
        if let intValue = Int(str) {
            // 1を加えて文字列に変換して返す
            return String(intValue + 1)
        } else {
            // 数値に変換できない場合は1を文字列として返す
            return "1"
        }
    }
    
    func minusOne(str: String) -> String {
        // 文字列を数値に変換
        if let intValue = Int(str) {
            // 1を加えて文字列に変換して返す
            return String(intValue - 1)
        } else {
            // 数値に変換できない場合は1を文字列として返す
            return "1"
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
        InputScrollSpeedView(isKeyboardVisible: FocusState())
            .environmentObject(ModelData())
    }
}
