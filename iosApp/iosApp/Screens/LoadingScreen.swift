//
// Created by Zoran on 01.02.2026..
//

import Foundation
import SwiftUI

struct LoadingScreen: View {
    var body: some View {
        ZStack {
            ProgressView()
                .accessibilityIdentifier("loadingIndicator")
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}