//
// Created by Zoran on 02.02.2026..
//

import Foundation
import SwiftUI
import Shared
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI

struct SelectGeneresScreen: View {

    @StateViewModel var selectGenreViewModel: SelectGenreViewModel = IosDependencies.shared.getSelectGenreViewModel()

    var showNextButton: Bool
    var onNextButtonClicked: () -> Void

    var body: some View {
        VStack {
            switch selectGenreViewModel.genresAll {
            case is SelectGenreUiState.Loading:
                LoadingScreen()

            case let success as SelectGenreUiState.Success:
                SelectGenreScreenLayout(
                    showNextButton: showNextButton,
                    successState: success,
                    onGenreToggled: { genre in
                        if let genreId = genre.data.id {
                            selectGenreViewModel.handleIntent(
                                intent: SelectGenreUiIntent.ToggleSelectedGenre(genreId: Int32(genreId))
                            )
                        }
                    },
                    onNextButtonClicked: onNextButtonClicked
                )

            case let error as SelectGenreUiState.Error:
                ErrorScreen(errorText: error.errorMessage, onReloadDataButtonClicked: {
                    selectGenreViewModel.handleIntent(
                        intent: SelectGenreUiIntent.ReloadData()
                    )
                })

            default:
                LoadingScreen()
            }
        }   .navigationTitle("Select Genres")
    }

}

// Layout View
struct SelectGenreScreenLayout: View {
    let showNextButton: Bool
    let successState: SelectGenreUiState.Success
    let onGenreToggled: (GenreResponseItemChecked) -> Void
    let onNextButtonClicked: () -> Void

    var body: some View {
        VStack {
            ScrollView {
                LazyVStack(spacing: 0) {
                    ForEach(successState.genres, id: \.self) { genre in
                        SelectGenreListItem(
                            genre: genre,
                            onItemChecked: { checkedGenre in
                                onGenreToggled(checkedGenre)
                            }
                        )
                    }
                }
            }

            Spacer()

            if showNextButton {
                Button(action: onNextButtonClicked) {
                    Text("Next")
                        .font(.title3)
                        .fontWeight(.semibold)
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.blue)
                        .cornerRadius(10)
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 20)
            }
        }
        .padding(.horizontal, 16)
    }
}


struct SelectGenreListItem: View {
    let genre: GenreResponseItemChecked
    let onItemChecked: (GenreResponseItemChecked) -> Void

    var body: some View {
        VStack(spacing: 0) {
            Button(action: {
                onItemChecked(genre)
            }) {
                HStack(alignment: .center, spacing: 0) {
                    Text(genre.data.name)
                        .padding(16)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .font(.headline)
                        .foregroundColor(.primary)

                    Spacer()
                        .frame(width: 16)

                    SelectGenreButton(selected: genre.isChecked)
                }
            }
            .buttonStyle(PlainButtonStyle())

            Divider()
                .padding(.top, 8)
                .padding(.bottom, 8)
                .opacity(0.1)
        }
        .padding(.horizontal, 16)
    }
}

struct SelectGenreButton: View {
    var selected: Bool = false

    var body: some View {
        ZStack {
            Circle()
                .fill(selected ? Color.blue : Color.white)
                .frame(width: 36, height: 36)
                .overlay(
                    Circle()
                        .stroke(selected ? Color.blue : Color.gray.opacity(0.1), lineWidth: 1)
                )

            Image(systemName: selected ? "checkmark" : "plus")
                .foregroundColor(selected ? .white : .blue)
                .font(.system(size: 16))
                .padding(8)
        }
    }
}

// Helper function to handle Kotlin sealed classes in Swift
func onEnum<T>(of obj: T) -> T {
    return obj
}