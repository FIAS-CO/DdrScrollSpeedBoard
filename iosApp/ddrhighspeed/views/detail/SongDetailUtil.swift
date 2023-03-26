import Foundation
import shared

class SongDetailUtil {
    var factory: ResultRowSetFactory = ResultRowSetFactory()
    
    func toDetailRows(song: Song, highSpeedValue: Int32) -> [ResultRowForDetail] {
        var result: [ResultRowForDetail] = []
        // TODO: if節を1メソッドにまとめる
        if let bpm = song.max_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeedValue, category: "最大", bpm: bpm))
        }
        if let bpm = song.sub_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeedValue, category: "基本②", bpm: bpm))
        }
        if let bpm = song.base_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeedValue, category: "基本①", bpm: bpm))
        }
        if let bpm = song.min_bpm?.doubleValue {
            result.append(factory.createForDetail(scrollSpeed: highSpeedValue, category: "最小", bpm: bpm))
        }
        
        return result
    }
}
