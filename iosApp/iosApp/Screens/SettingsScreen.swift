//
// Created by Zoran on 02.02.2026..
//

import Foundation
import SwiftUI


struct SettingsScreen: View {

    var onSelectGenres: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            SettingsClickableItem(
                title: "Genres",
                subtitle: "Genres of games used to filter displayed games",
                onClick: onSelectGenres
            )

            Spacer()
        }
        .navigationTitle("Settings")
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct SettingsClickableItem: View {
    let title: String
    let subtitle: String
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            VStack(alignment: .leading, spacing: 0) {
                VStack(alignment: .leading, spacing: 4) {
                    Text(title)
                        .font(.headline)
                        .foregroundColor(.primary)

                    Text(subtitle)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal, 16)
                .padding(.vertical, 20)

                Divider()
                    .padding(.horizontal, 16)
            }
        }
        .buttonStyle(PlainButtonStyle())
        .background(Color(UIColor.systemBackground))
    }
}