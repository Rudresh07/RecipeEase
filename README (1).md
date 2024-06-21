
# RecipeEase


RecipeEase is an Android application that allows users to discover and share recipes. Users can create an account, log in, and explore a variety of recipes fetched from an API. They can also upload their own recipes, which are stored in Firestore for others to see. Additionally, the app features a unique functionality powered by Gemini, which generates recipes based on the ingredients provided by the user.

## Features

1. User Authentication: Create and log in to an account using Firebase Authentication.

2. Recipe Discovery: Fetch and display recipes from an external API.
3. Recipe Upload: Users can upload their own recipes to Firestore.
4. Community Recipes: View recipes uploaded by other users.

5. Recipe Generation: Use Gemini to generate recipes based on provided ingredients.

6. Built with Kotlin and XML: Leveraging Kotlin for logic and XML for UI design.

## Installation

To install and run RecipeEase on your local machine, follow these steps:

1. Clone the repository:
git clone https://github.com/Rudresh07/RecipeEase.git
cd RecipeEase

2. Open the project in Android Studio.

3. Sync the project with Gradle files.

4. Run the app on an emulator or a physical device.
## Usage

1. Launch the app.
2. Create an account or log in using Firebase Authentication.
3. Explore recipes fetched from the API.
4. Upload your own recipes through the app interface.
5. Use the ingredient-based recipe generator powered by Gemini.
## Screenshots

![App Screenshot](https://via.placeholder.com/468x300?text=App+Screenshot+Here)


## Dependencies

1. Firebase Authentication: For user account creation and login.
2. Firestore: For storing and retrieving user-uploaded recipes.
3. Gemini: For generating recipes based on user-provided ingredients.
4. Kotlin and XML: For app development and UI design
## Troubleshooting

1. Authentication Issues: Ensure your Firebase configuration is correct and the google-services.json file is properly placed.
2. API Errors: Check your network connection and ensure your API keys are valid and properly configured.
## License

[MIT](https://choosealicense.com/licenses/mit/)

