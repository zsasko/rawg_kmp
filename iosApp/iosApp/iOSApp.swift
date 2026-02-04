import SwiftUI
import Shared

@main
struct iOSApp: App {

    init() {
        IosKoinHelper.shared.initialize { _ in }
    }

    var body: some Scene {
        WindowGroup {
            RouterView()
        }
    }
}