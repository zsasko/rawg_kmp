//
// Created by Zoran on 01.02.2026..
//

import Foundation
import SwiftUI
import Shared

struct ErrorScreen: View {
    let errorText: String
    let onReloadDataButtonClicked: () -> Void

    var body: some View {
        ZStack {
            VStack(spacing: 10) {
                Button(action: onReloadDataButtonClicked) {
                    Text("Reload Data")
                }

                Text(errorText)
                    .font(.footnote)
                    .multilineTextAlignment(.center)
            }
            .padding(18)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
