import Foundation
import shared

class SongDetailUtil {
    var factory: ResultRowSetFactory = ResultRowSetFactory()
    
    func toDetailRows(song: Song, highSpeedValue: Int) -> [ResultRowForDetail] {
        var result: [ResultRowForDetail] = []
        let highSpeed: Int32 = Int32(highSpeedValue)
        // TODO: if節を1メソッドにまとめる
        if let bpm = song.max_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "最大", bpm: bpm))
        }
        if let bpm = song.sub_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "基本②", bpm: bpm))
        }
        if let bpm = song.base_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "基本①", bpm: bpm))
        }
        if let bpm = song.min_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "最小", bpm: bpm))
        }
        
        return result
    }
}
