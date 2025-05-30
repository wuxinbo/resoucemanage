//
//  resourceApp.swift
//  resource

//  Created by wuxinbo on 2023/2/26.
//

import SwiftUI

@main
struct resourceApp: App {
    let persistenceController = PersistenceController.shared
    /**
     * 照片
     */
    var photo = Photo();
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
                .environmentObject(photo)
        }
    }
}
