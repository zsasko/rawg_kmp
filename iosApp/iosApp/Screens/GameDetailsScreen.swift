//
// Created by Zoran on 02.02.2026..
//

import Foundation
import SwiftUI
import Shared
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI

struct GameDetailsScreen: View {

    var gameId: Int

    @StateViewModel private var gameDetailsViewModel: GameDetailsViewModel

    init(gameId: Int) {
        self.gameId = gameId
        _gameDetailsViewModel = StateViewModel(wrappedValue: {
            IosDependencies.shared.getGameDetailsViewModel(gameId: Int32(gameId))
        }())
    }

    var body: some View {
        VStack {
            switch gameDetailsViewModel.gameDetailsUiState {
            case let success as GameDetailsUiState.Loaded:
                GameDetailsLayout(
                    gamesDetailsUiState: success
                )
            case is GameDetailsUiState.Loading:
                LoadingScreen()

            case let error as GameDetailsUiState.Error:
                ErrorScreen(errorText: error.errorMessage ?? "", onReloadDataButtonClicked: {
                    gameDetailsViewModel.handleIntent(
                        intent: GamesDetailsUiIntent.LoadGameDetails()
                    )
                })

            default:
                LoadingScreen()
            }
        }
    }
}

struct GameDetailsLayout: View  {

    let gamesDetailsUiState: GameDetailsUiState.Loaded

    var body: some View {
        GameDetailsView(game: gamesDetailsUiState.data)
    }
}


struct GameDetailsView: View {
    let game: GameDetailsResponse
    @State private var selectedTab = 0

    var body: some View {
        ScrollView {

            LazyVStack( spacing: 16) {

                HeaderImageView(imageUrl: game.backgroundImage)

                LazyVStack(alignment: .leading, spacing: 16) {

                    TitleSection(game: game)

                    RatingMetacriticSection(game: game)

                    StatsRow(game: game)

                    Divider()

                    if !game.description.isEmpty {
                        DescriptionSection(description: game.description)
                    }

                    if let platforms = game.platforms, !platforms.isEmpty {
                        PlatformsSection(platforms: platforms)
                    }

                    if !game.ratings.isEmpty {
                        RatingsDistributionSection(ratings: game.ratings)
                    }

                    AdditionalInfoSection(game: game)

                    SocialLinksSection(game: game)
                }
                .padding(.horizontal, 16)
            }
        }
        .navigationTitle(game.name)
        .navigationBarTitleDisplayMode(.inline)
    }
}

// MARK: - Subviews

struct HeaderImageView: View {
    let imageUrl: String?

    var body: some View {
        AsyncImage(url: URL(string: imageUrl ?? "")) { phase in
            switch phase {
            case .empty:
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .overlay(ProgressView())
            case .success(let image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
            case .failure:
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .overlay(
                        Image(systemName: "photo")
                            .foregroundColor(.gray)
                    )
            @unknown default:
                EmptyView()
            }
        }
        .frame(height: 250)
    }
}

struct TitleSection: View {
    let game: GameDetailsResponse

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(game.name)
                .font(.largeTitle)
                .fontWeight(.bold)

            if game.name != game.nameOriginal {
                Text(game.nameOriginal)
                    .font(.title3)
                    .foregroundColor(.secondary)
            }

            HStack {
                if !game.released.isEmpty {
                    Label(game.released, systemImage: "calendar")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }

                if let esrb = game.esrbRating {
                    Label(esrb.name, systemImage: "e.square")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
            }
        }
    }
}

struct RatingMetacriticSection: View {
    let game: GameDetailsResponse

    var body: some View {
        HStack(spacing: 20) {
            if let rating = game.rating {
                VStack(alignment: .leading, spacing: 4) {
                    HStack(spacing: 4) {
                        Image(systemName: "star.fill")
                            .foregroundColor(.yellow)
                        Text(String(format: "%.1f", rating))
                            .font(.title2)
                            .fontWeight(.bold)
                    }
                    if let count = game.ratingsCount {
                        Text("\(count) ratings")
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }
                }
            }

            if let metacritic = game.metacritic?.intValue {
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(metacritic)")
                        .font(.title2)
                        .fontWeight(.bold)
                        .foregroundColor(metacriticColor(score: metacritic ))
                    Text("Metacritic")
                        .font(.caption)
                        .foregroundColor(.secondary)
                }
                .padding(8)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(metacriticColor(score: metacritic).opacity(0.2))
                )
            }
        }
    }

    func metacriticColor(score: Int?) -> Color {
        guard let score = score else {
            // If score is nil, return a default color
            return .gray
        }
        switch score {
        case 75...100: return .green
        case 50..<75: return .yellow
        default: return .red
        }
    }
}

struct StatsRow: View {
    let game: GameDetailsResponse

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 16) {
                if let playtime = game.playtime?.intValue, playtime > 0 {
                    StatItem(icon: "clock", value: "\(playtime)h", label: "Playtime")
                }

                if let achievements = game.achievementsCount?.intValue, achievements > 0 {
                    StatItem(icon: "trophy", value: "\(achievements)", label: "Achievements")
                }

                if let screenshots = game.screenshotsCount?.intValue, screenshots > 0 {
                    StatItem(icon: "photo", value: "\(screenshots)", label: "Screenshots")
                }

                if let movies = game.moviesCount?.intValue, movies > 0 {
                    StatItem(icon: "film", value: "\(movies)", label: "Videos")
                }
            }
        }
    }
}

struct StatItem: View {
    let icon: String
    let value: String
    let label: String

    var body: some View {
        VStack(spacing: 4) {
            HStack(spacing: 4) {
                Image(systemName: icon)
                    .font(.caption)
                Text(value)
                    .fontWeight(.semibold)
            }
            Text(label)
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.gray.opacity(0.1))
        )
    }
}

struct DescriptionSection: View {
    let description: String
    @State private var isExpanded = false


    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("About")
                .font(.headline)

            Text(stripHTML(description))
                .font(.body)
                .lineLimit(isExpanded ? nil : 4)
                .foregroundColor(.secondary)

            if description.count > 200 {
                Button(action: { isExpanded.toggle() }) {
                    Text(isExpanded ? "Show less" : "Read more")
                        .font(.subheadline)
                        .fontWeight(.medium)
                }
            }
        }
    }

    func stripHTML(_ html: String) -> String {
        html.replacingOccurrences(of: "<[^>]+>", with: "", options: .regularExpression)
    }
}

struct PlatformsSection: View {
    let platforms: [GameDetailsResponseItemPlatform]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Platforms")
                .font(.headline)

            FlowLayout(spacing: 8) {
                ForEach(platforms, id: \.platform?.id) { platformInfo in
                    if let platform = platformInfo.platform {
                        Text(platform.name ?? "")
                            .font(.subheadline)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 6)
                            .background(
                                Capsule()
                                    .fill(Color.blue.opacity(0.2))
                            )
                            .foregroundColor(.blue)
                    }
                }
            }
        }
    }
}

struct RatingsDistributionSection: View {
    let ratings: [GameResponseItemRating]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Ratings Distribution")
                .font(.headline)

            ForEach(ratings,  id: \.id) { rating in
                HStack {
                    Text(rating.title)
                        .font(.subheadline)
                        .frame(width: 100, alignment: .leading)

                    GeometryReader { geometry in
                        ZStack(alignment: .leading) {
                            Rectangle()
                                .fill(Color.gray.opacity(0.2))

                            Rectangle()
                                .fill(ratingColor(for: rating.title))
                                .frame(width: geometry.size.width * (CGFloat(rating.percent) / 100))
                        }
                    }
                    .frame(height: 20)
                    .cornerRadius(4)

                    Text("\(Int(rating.percent))%")
                        .font(.caption)
                        .fontWeight(.medium)
                        .frame(width: 40, alignment: .trailing)
                }
            }
        }
    }

    func ratingColor(for title: String) -> Color {
        switch title.lowercased() {
        case "exceptional": return .green
        case "recommended": return .blue
        case "meh": return .orange
        case "skip": return .red
        default: return .gray
        }
    }
}

struct AdditionalInfoSection: View {
    let game: GameDetailsResponse

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Additional Information")
                .font(.headline)

            if !game.website.isEmpty {
                InfoRow(label: "Website", value: game.website, isLink: true)
            }

            if let added = game.added {
                InfoRow(label: "Added by", value: "\(added) users")
            }

            if let suggestions = game.suggestionsCount {
                InfoRow(label: "Suggestions", value: "\(suggestions)")
            }

            if let alternativeNames = game.alternativeNames, !alternativeNames.isEmpty {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Alternative Names")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    Text(alternativeNames.joined(separator: ", "))
                        .font(.body)
                }
            }
        }
    }
}

struct InfoRow: View {
    let label: String
    let value: String
    var isLink: Bool = false

    var body: some View {
        HStack {
            Text(label)
                .font(.subheadline)
                .foregroundColor(.secondary)
            Spacer()
            if isLink, let url = URL(string: value) {
                Link(value, destination: url)
                    .font(.body)
                    .lineLimit(1)
            } else {
                Text(value)
                    .font(.body)
            }
        }
    }
}

struct SocialLinksSection: View {
    let game: GameDetailsResponse

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Community")
                .font(.headline)

            HStack(spacing: 16) {
                if let reddit = game.redditCount?.intValue, reddit > 0 {
                    SocialButton(icon: "bubble.left.and.bubble.right", count: reddit, label: "Reddit")
                }

                if let twitch = game.twitchCount?.intValue, twitch > 0 {
                    SocialButton(icon: "play.rectangle", count: twitch, label: "Twitch")
                }

                if let youtube = game.youtubeCount?.intValue, youtube > 0 {
                    SocialButton(icon: "play.circle", count: youtube, label: "YouTube")
                }
            }
        }
    }
}

struct SocialButton: View {
    let icon: String
    let count: Int
    let label: String

    var body: some View {
        VStack(spacing: 4) {
            Image(systemName: icon)
                .font(.title2)
            Text("\(count)")
                .font(.caption)
                .fontWeight(.semibold)
            Text(label)
                .font(.caption2)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(
            RoundedRectangle(cornerRadius: 12)
                .fill(Color.gray.opacity(0.1))
        )
    }
}

// MARK: - Flow Layout for Tags

struct FlowLayout: Layout {
    var spacing: CGFloat = 8

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let result = FlowResult(
            in: proposal.replacingUnspecifiedDimensions().width,
            subviews: subviews,
            spacing: spacing
        )
        return result.size
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let result = FlowResult(
            in: bounds.width,
            subviews: subviews,
            spacing: spacing
        )
        for (index, subview) in subviews.enumerated() {
            subview.place(at: CGPoint(x: bounds.minX + result.positions[index].x,
                                      y: bounds.minY + result.positions[index].y),
                          proposal: .unspecified)
        }
    }

    struct FlowResult {
        var size: CGSize = .zero
        var positions: [CGPoint] = []

        init(in maxWidth: CGFloat, subviews: Subviews, spacing: CGFloat) {
            var x: CGFloat = 0
            var y: CGFloat = 0
            var lineHeight: CGFloat = 0

            for subview in subviews {
                let size = subview.sizeThatFits(.unspecified)

                if x + size.width > maxWidth && x > 0 {
                    x = 0
                    y += lineHeight + spacing
                    lineHeight = 0
                }

                positions.append(CGPoint(x: x, y: y))
                lineHeight = max(lineHeight, size.height)
                x += size.width + spacing
            }

            self.size = CGSize(width: maxWidth, height: y + lineHeight)
        }
    }
}

