import SwiftUI

struct LongPushableButton: View {
    var imageName: String
    var action: () -> Void
    
    @State private var timer: Timer?
    @State var isLongPressing = false
    
    var body: some View {
        Button(action: {
            print("tap")
            if(self.isLongPressing){
                //this tap was caused by the end of a longpress gesture, so stop our fastforwarding
                self.isLongPressing.toggle()
                self.timer?.invalidate()
            } else {
                //just a regular tap
                action()
            }
        }, label: {
            // TODO: アイコンを変える
            Image(systemName: self.isLongPressing ? imageName: imageName)
                .resizable()
                .scaledToFit()
                .frame(width: 30, height: 30)
        })
        .simultaneousGesture(LongPressGesture(minimumDuration: 0.2).onEnded { _ in
            print("long press")
            self.isLongPressing = true
            //or fastforward has started to start the timer
            self.timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { _ in
                action()
            })
        })
    }
}

struct LongPushableButton_Previews: PreviewProvider {
    static var previews: some View {
        @State var count:Int = 0
        return VStack {
            Text("Count: \(count)")
            // REVISIT: アクションがここでは動かないので、ボタン呼び出し先のプレビューで動作確認すること
            LongPushableButton(imageName: "plus.square", action: {count += 1})
        }
    }
}
