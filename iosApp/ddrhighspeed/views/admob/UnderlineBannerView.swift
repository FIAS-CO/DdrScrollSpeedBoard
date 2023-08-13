//
//  UnderlineBannerView.swift
//  ddrhighspeed
//
//  Created by apple on 2023/08/13.
//

import SwiftUI

// Revisit: 画面サイズによって広告の横幅を調整できるようにする
struct UnderlineBannerView: View {
    var body: some View {
        // Workaround iPadだと広告ロードが失敗するらしいので出さない
        if UIDevice.current.adAvailable {
            BannerView()
                .frame(height: UIDevice.current.adSize)
                .overlay( // 枠線のない広告が画面下部のバーとつながって見えるので広告下部に線を引く
                    Rectangle().fill(Color.gray.opacity(0.3))
                        .frame(height: 1)
                        .padding(.top, -1),
                    alignment: .bottom
                )
        }
    }
}

struct UnderlineBannerView_Previews: PreviewProvider {
    static var previews: some View {
        UnderlineBannerView()
    }
}
