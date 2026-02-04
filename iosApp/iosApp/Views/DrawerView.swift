//
//  DrawerView.swift
//  test_ui
//
//  Created by Zoran on 01.02.2026..
//


import SwiftUI

struct DrawerView: View {
    @Binding var selectedDestination: Route?
    @Binding var showDrawer: Bool


    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Color.clear
                .frame(height: 60)

            Image("AppIconColorful")
                .resizable()
                .scaledToFit()
                .frame(maxWidth: .infinity)
                .padding(.vertical, 40)
                .padding(.horizontal, 40)
            
            Button {
                selectedDestination = .home
                closeDrawer()
            } label: {
                Label("Home", systemImage: "house")
                    .foregroundColor(.primary)
                    .padding(.vertical, 12)
                    .padding(.horizontal, 20)
            }

            
            Button {
                selectedDestination = .settings
                closeDrawer()
            } label: {
                Label("Settings", systemImage: "gear")
                    .foregroundColor(.primary)
                    .padding(.vertical, 12)
                    .padding(.horizontal, 20)
            }
            
            Spacer()
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color(UIColor.systemBackground))
        .ignoresSafeArea()
    }
    
    private func closeDrawer() {
        withAnimation {
            showDrawer = false
        }
    }
}
