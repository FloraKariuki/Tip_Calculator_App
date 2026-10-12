# Restaurant Tip Calculator App

A simple, user-friendly Android app built with Jetpack Compose to quickly calculate restaurant tips and total bill split amounts.

## Overview
Calculating tips at the end of a meal can sometimes be confusing or awkward. This app was built as a practical dining companion to solve that—you just enter your bill total, slide to choose your preferred tip percentage, and instantly get the final tip and total amount.

## Key Features
* **Interactive Tip Slider:** Adjust tip percentages smoothly from 0% up to 50%.
* **State Preservation:** Keeps your entered bill amount and slider position intact even if you rotate your phone screen.
* **Multi-Screen Navigation:** Uses a bottom navigation bar to switch easily between three main screens:
    * **Home:** The main calculator screen.
    * **List:** Highlights and features of the app built using `LazyColumn`.
    * **About:** Background info on why the app was built and developer details.
* **Custom UI Design:** Styled with rounded cards and vibrant background gradients to keep the interface clean and modern.

## Tech Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose & Material 3
* **Navigation:** Jetpack Navigation Compose (`2.7.7`)
* **IDE:** Android Studio

## How to Run
1. Clone or download this project.
2. Open the project in Android Studio and let Gradle finish syncing.
3. Connect an Android phone via USB (or start an emulator).
4. Hit **Run** to launch the app.

---
**Developer:** Flora  
**Coursework:** Information Technology, JKUAT