//
//  ContentView.swift
//  ddrhighspeed
//
//  Created by apple on 2023/03/15.
//

import SwiftUI
import shared


struct ContentView: View {
    @EnvironmentObject var modelData: ModelData

    var body: some View {
        NavigationView {
            List{
                ForEach(modelData.songs, id: \.self) { song in
                    NavigationLink {
                        SongDetail()
                    } label: {
                        Text(song.name)
                    }
                }
            }
            .navigationTitle("Landmarks")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
            .environmentObject(ModelData())
    }
}
