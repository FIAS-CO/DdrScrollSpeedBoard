//
//  UIDevice.swift
//  ddrhighspeed
//
//  Created by apple on 2023/08/07.
//
import UIKit

public extension UIDevice {
    
    // Revisit 高さを画面サイズ比例にしたいがGeometryReader使ってもダメだったのでどうにかする
    var adSize: CGFloat {
        // iPadは広告がロードできないので出さない
        if UIDevice.current.model != "iPhone" {
            return -1
        }
        
        let defaultBannerHeight = 50
        let defaultBannerWidth = 320
        let ratio = defaultBannerHeight / defaultBannerWidth
        
    
        let displayWidth: Int
        switch getIdentifier() {
        case "iPhone8,1":                                 displayWidth = 375  // "iPhone 6s"
        case "iPhone8,2":                                 displayWidth = 414 // "iPhone 6s Plus"
        case "iPhone8,4":                                 displayWidth = 320 // "iPhone SE"
        case "iPhone9,1", "iPhone9,3":                    displayWidth = 375 // "iPhone 7"
        case "iPhone9,2", "iPhone9,4":                    displayWidth = 414 // "iPhone 7 Plus"
        case "iPhone10,1", "iPhone10,4":                  displayWidth = 375 // "iPhone 8"
        case "iPhone10,2", "iPhone10,5":                  displayWidth = 414 // "iPhone 8 Plus"
            // 他の機種に関する情報もこちらに追加...
        default:                                          displayWidth = 428 // X 以降は大きめ
        }
        
        return CGFloat(displayWidth * ratio)
    }
    
    // ipadまたは画面が小さい端末に対しては一部画面で広告を出さないようにするための判定
    var adAvailable: Bool {
        // iPadは広告がロードできないので出さない
        if UIDevice.current.model != "iPhone" {
            return false
        }
        
        return true
    }
    
    private func getIdentifier() -> String {
        var systemInfo = utsname()
        uname(&systemInfo)
        let machineMirror = Mirror(reflecting: systemInfo.machine)
        let identifier = machineMirror.children.reduce("") { identifier, element in
            guard let value = element.value as? Int8, value != 0 else { return identifier }
            return identifier + String(UnicodeScalar(UInt8(value)))
        }
        
        return identifier
    }
}
