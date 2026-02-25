# Contributing to Jeu de la Vie ANDROID

Thank you for your interest in contributing to Jeu de la Vie ANDROID.
All contributions are welcome.
This project is built with Kotlin Multiplatform and uses Jetpack Compose for UI.

## How to Contribute

### 1. Fork the repository
First create your own fork of the project then clone it locally

### 2. Create a Feature Branch
Please do not work directly on the main branch.
Create a branch using the following naming convention:
feature/your-feature-name

Example:
git checkout -b feature/glider-library
For bug fixes:
fix/short-description

### 3. Development Guidelines
Keep the architecture consistent with the existing design.
Cells must remain independent objects (no array-based refactor unless discussed first).
Keep logic separated from UI.
Follow Kotlin best practices.
Prefer clear and maintainable code over clever optimizations.

If your change affects:
Core logic (evolution rules, adjacency handling)
Performance (rendering or update cycle)
Multiplatform compatibility
-> Please describe your reasoning clearly in the Pull Request.

### 4. Run the Project
To launch on desktop:
gradle :composeApp:run
Make sure the project builds correctly before submitting your Pull Request.

### 5. Commit Style
Use clear and descriptive commit messages.
Recommended format:
type: short description

Optional longer explanation if necessary.
Examples:
feat: add toroidal grid wrapping
fix: correct neighbor evaluation on edges
refactor: simplify cell update logic

### 6. Pull Request Process

1. Push your feature branch to your fork.
2. Open a Pull Request from:

   your-fork:feature/your-feature

   to:

   LucWaw/JeuDeLaVie:main

3. Make sure the base repository is the original project and the base branch is `main`.
4. Describe clearly:
- What the change does
- Why it is needed
- Any side effects

## 💡 Suggestions Welcome

- You may contribute by:
- Adding new patterns (gliders, oscillators, spaceships)
- Improving performance
- Enhancing UI/UX
- Refactoring for better readability
- Adding tests
- Improving documentation
If you want to propose a major change(like options, navigation or base logic change), open an Issue first to discuss it.

## 📜 Code of Conduct
Here full Code Of Conduct [Code of Conduct](https://github.com/LucWaw/JeuDeLaVie/blob/main/CODE_OF_CONDUCT.md)
Be respectful and constructive in discussions.
This project is open to learning and experimentation.
Thank you for contributing.
