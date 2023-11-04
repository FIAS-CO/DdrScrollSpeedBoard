import Foundation
import shared

// TODO 名前を変える
class SongDetailUtil {
    var factory: ResultRowSetFactory = ResultRowSetFactory()
    
    func toRoughEstimateRows(highSpeedValue: Int) -> [ResultRow] {
        return factory.create(scrollSpeed: Int32(highSpeedValue))
    }
    
    // TODO このメソッドをKotlin側で実装する
    func toDetailRows(song: SongData, highSpeedValue: Int) -> [ResultRowForDetail] {
        var result: [ResultRowForDetail] = []
        let highSpeed: Int32 = Int32(highSpeedValue)
        // TODO: if節を1メソッドにまとめる
        if let bpm = song.maxBpm?.doubleValue, bpm != 0.0 {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "最大", bpm: bpm))
        }
        if let bpm = song.subBpm?.doubleValue, bpm != 0.0 {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "基本②", bpm: bpm))
        }
        if song.baseBpm != 0.0 {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "基本①", bpm: song.baseBpm))
        }
        if let bpm = song.minBpm?.doubleValue, bpm != 0.0 {
            result.append(factory.createForDetail(scrollSpeed: highSpeed, category: "最小", bpm: bpm))
        }
        
        return result
    }
    
    func culcateSuggestHighSpeed(bpm:Double, scrollSpeed: Int) -> Double {
        return factory.calculateHighSpeed(bpm: bpm, scrollSpeed: Int32(scrollSpeed))
    }
}
