import SwiftUI
import shared

class MovieUtil {
    let cyan = Color(UIColor(red: 0, green: 0.79, blue: 0.79, alpha: 1.0))
    let orange = Color(UIColor(red: 0.98, green: 0.63, blue: 0, alpha: 1.0))
    let red = Color(UIColor(red: 1, green: 0, blue: 0, alpha: 0.75))
    let purple = Color(UIColor(red: 0.5, green: 0, blue: 0.5, alpha: 1.0))
    let green = Color(UIColor(red: 0, green: 0.5, blue: 0, alpha: 1.0))
    
    func getLabel(difficulty: String)->(String, Color) {
        switch difficulty {
        case "BESP":
            return ("Single Beginner", cyan)
        case "BSP":
            return ("Single Basic", orange)
        case "DSP":
            return ("Single Difficult", red)
        case "ESP":
            return ("Single Expert", green)
        case "CSP":
            return ("Single Challenge", purple)
        case "BDP":
            return ("Double Basic", orange)
        case "DDP":
            return ("Double Difficult", red)
        case "EDP":
            return ("Double Expert", green)
        case "CDP":
            return ("Double Challenge", purple)
        default:
            return ("Undefined Difficulty", .gray)
        }
    }
    
    let diffOrder: [String] = ["BESP", "BSP", "DSP", "ESP", "CSP", "BDP", "DDP", "EDP", "CDP"]
    func sortByDifficulty(movies: [Movie])-> [Movie] {
        return movies.sorted(by: {
            guard let index1 = diffOrder.firstIndex(of: $0.difficulty)
            else {
                // $0のdifficultyはdiffOrderに含まれないので、$0が後ろになる
                return false
            }
            
            guard let index2 = diffOrder.firstIndex(of: $1.difficulty)
            else {
                // $1のdifficultyはdiffOrderに含まれないので、$0が後ろになる
                return true
            }
            
            return index1 < index2
        })
    }
}
