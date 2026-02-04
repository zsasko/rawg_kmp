//
//  NAvigationView.swift
//  test_ui
//
//  Created by Zoran on 01.02.2026..
//

import SwiftUI
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI
import Shared

enum Route: Hashable {
    case selectGenres
    case home
    case settings
    case gameDetails(gameId: Int)
}

@Observable
class Router {
    var path = NavigationPath()
    func navigate(to route: Route) {
        path.append(route)
    }
    func goBack() {
        path.removeLast()
    }
    func goToRoot() {
        path.removeLast(path.count)
    }
}

struct RouterView: View {

    @State private var router = Router()
    @State private var selectedDestination: Route? = nil

    @State private var showStandaloneSplash = true
    @State private var showStandaloneSelectGenres = false

    @State private var showDrawer = false
    @GestureState private var dragOffset = CGSize.zero

    var body: some View {
        if showStandaloneSplash {
            SplashScreen(
                navigateToMainScreen: {
                    withAnimation {
                        showStandaloneSplash = false
                    }
                },
                navigateToSelectGenresScreen: {
                    withAnimation {
                        showStandaloneSplash = false
                        showStandaloneSelectGenres = true
                    }
                }
            )
        } else if (showStandaloneSelectGenres) {
            SelectGeneresScreen(
                showNextButton: true,
                onNextButtonClicked: {
                showStandaloneSelectGenres = false
            })
        }
        else {
            //NavigationStack {
                ZStack {
                    VStack {
                        NavigationStack(path: $router.path) {
                            GamesScreen(onGameClicked: { (gameId: Int) in
                                    router.navigate(to: .gameDetails(gameId: gameId))
                                })
                                .environment(router)
                                .navigationDestination(for: Route.self) { route in
                                    routeView(for: route)
                                }
                            .navigationBarItems(leading: Button(action: {
                                withAnimation {
                                    showDrawer.toggle()
                                }
                            }) {
                                Image(systemName: "line.horizontal.3")
                                    .imageScale(.large)
                            })
                        }
                    }
                }
                .overlay(

                    ZStack {
                        // Drawer overlay
                        if showDrawer {
                            Color.black.opacity(0.3)
                                .ignoresSafeArea()
                                .onTapGesture {
                                    withAnimation {
                                        showDrawer = false
                                    }
                                }
                        }

                        // Drawer
                        HStack(spacing: 0) {
                            DrawerView(selectedDestination: $selectedDestination, showDrawer: $showDrawer)
                                .frame(width: 250)
                                .offset(x: showDrawer ? 0 : -250)
                            Spacer()
                        }
                        .ignoresSafeArea()
                    }
                )
                .animation(.easeInOut, value: showDrawer)
                .gesture(
                    DragGesture()
                        .onEnded { value in
                            if value.startLocation.x < 20 && value.translation.width > 100 {
                                withAnimation {
                                    showDrawer = true
                                }
                            }
                        }
                )
                .onChange(of: selectedDestination) { oldValue, newValue in
                    if let newRoute = newValue {
                        handleDestinationChange(newRoute)
                    }

                }

        }
    }

    private func handleDestinationChange(_ destination: Route) {
        // Your custom logic when destination changes
        switch destination {
        case .settings:
            router.navigate(to: .settings)
        case .home:
            router.goToRoot()
        default:
            break
        }

        // Close drawer after navigation
        withAnimation {
            showDrawer = false
        }
    }

    @ViewBuilder
    private func routeView(for route: Route) -> some View {
        switch route {
        case .selectGenres:
            SelectGeneresScreen(
                showNextButton: false,
                onNextButtonClicked: {
                router.goBack()
            })
        case .home:
            GamesScreen(onGameClicked: { (gameId: Int) in
                router.navigate(to: .gameDetails(gameId: gameId))
            })
        case .gameDetails(let gameId):
            GameDetailsScreen(gameId: gameId)
        case .settings:
            SettingsScreen(onSelectGenres: {
                router.navigate(to: .selectGenres)
            })
        }
    }
}

