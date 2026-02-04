
import Foundation
import SwiftUI
import Shared
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI

struct GamesScreen: View {

    var onGameClicked: ( Int) -> Void

    @StateViewModel var gamesViewModel: IosGamesViewModel = IosDependencies.shared.getIosGamesViewModel()

    var body: some View {
        VStack {
            switch gamesViewModel.gamesUiState {
                case let success as IosGameScreenState.Success:
                    GamesScreenLayout(
                        gamesUiState: success,
                        onGameClicked: onGameClicked,
                        onLoadMoreInvoked: {
                            gamesViewModel.handleIntent(
                                intent: GamesUiIntent.LoadGames()
                            )
                        }
                    )
                case is IosGameScreenState.Loading:
                    LoadingScreen()

                case let error as IosGameScreenState.Error:
                    ErrorScreen(errorText: error.errorMessage, onReloadDataButtonClicked: {
                        gamesViewModel.handleIntent(
                            intent: GamesUiIntent.LoadGames()
                        )
                    })

                default:
                    LoadingScreen()
            }
        }
    }
}

struct GamesScreenLayout: View {
    let  gamesUiState: IosGameScreenState.Success

    let onGameClicked: (Int) -> Void
    let onLoadMoreInvoked: () -> Void

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(gamesUiState.items, id: \.id) { game in
                    GameListItem(
                        game: game,
                        onTap: {
                            onGameClicked(Int(game.id))
                        }
                    )
                    .onAppear {
                        if game.id == gamesUiState.items.last?.id {
                            onLoadMoreInvoked()
                        }
                    }
                }
            }
            .padding(.horizontal)
        }
        .navigationTitle("RAWG KMP")
        .navigationBarTitleDisplayMode(.inline)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct GameListItem: View {
    let game: GameResponseItem
    let onTap: () -> Void

    var body: some View {
        Button(action: {
            onTap()
        }) {
            ZStack(alignment: .bottomLeading) {
                AsyncImage(url: URL(string: game.backgroundImage ?? "")) { phase in
                    switch phase {
                    case .empty:
                        Color.gray.opacity(0.3)
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFill()
                            .frame(width: UIScreen.main.bounds.width - 32, height: 200)
                            .clipped()
                    case .failure:
                        Color.gray.opacity(0.3)
                            .overlay(
                                Image(systemName: "photo")
                                    .foregroundColor(.white)
                            )
                    @unknown default:
                        EmptyView()
                    }
                }

                VStack {
                    Spacer()
                    HStack {
                        Text(game.name)
                            .font(.headline)
                            .foregroundColor(.white)
                            .lineLimit(2)
                        Spacer()
                    }
                    .padding(20)
                    .background(
                        LinearGradient(
                            colors: [.clear, .black.opacity(0.8)],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
                }
            }
            .frame(maxWidth: .infinity)
            .frame(height: 200)
            .clipShape(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
            )
            .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
            .buttonStyle(.plain)


        }
        .buttonStyle(PlainButtonStyle())
    }
}