//
// Created by Zoran on 01.02.2026..
//

import Foundation
import SwiftUI
import Shared
import KMPObservableViewModelCore
import KMPObservableViewModelSwiftUI


struct SplashScreen: View {

    @StateViewModel var splashViewModel: SplashViewModel = IosDependencies.shared.getSplashViewModel()

    var navigateToMainScreen: () -> Void
    var navigateToSelectGenresScreen: () -> Void

    var body: some View {
        LoadingScreen()
            .onChange(of: splashViewModel.isGameCategoriesLoaded) { oldValue, newValue in
                guard let genresSelected = newValue?.boolValue else { return }
                if genresSelected {
                    navigateToMainScreen()
                } else {
                    navigateToSelectGenresScreen()
                }
            }
    }
}
