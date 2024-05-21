# City Guesser Game - CS2063 Fall 2023

Welcome to the City Guesser game developed for the CS2063 Fall 2023 course at UNB. This repository contains the code for city guesser game associated with the course.

Initial ui mockup:
https://www.figma.com/board/DQQYmh5FUAQYgvUcoCcNtE/mobile-app-project-sketch?node-id=0-1

Final in-app images:
https://imgur.com/a/lX40Mj1

Contact me via email in case of URL failure(s): tomjtait@outlook.com

## Android Studio

The code examples are built against and run in Android Studio Giraffe (2022.3.1 Patch 1). If you are using a different version, you may be prompted to update the Gradle plugin used

## Android SDK
Google Pixel 4a running API level 34 (Android 14) have been used for testing

## City Guesser Game Overview

The City Guesser game is a lab project developed for the CS2063 course. It is a mobile app where users can guess the city based on provided hints and images. The game keeps track of completed challenges, even if the user logs out and logs back in.

## Features

- **User Authentication:** Users can log in and out of the app.
- **City Challenges:** Users can guess cities based on hints and images.
- **Challenge Progress:** Completed challenges are remembered, allowing users to resume from where they left off.
- **Leaderboard:** A leaderboard displays points earned by users, creating a competitive environment among friends.

## Testing
Test Case (Best done in the following order) with Pixel 4a device: 
 

1. User Registration 

2. User login (following registration) 

3. User completion of a level (without hint) 

4. Check leaderboard to see new point balance update 

5. User completion of a level (with hint) 

6. Check leaderboard to see new point balance update 

7. User change username 

8. Check leaderboard to see new username 

9. User finish all remaining levels (with or without hints) 

10. User congratulations screen upon finishing all possible levels within the game 

11. User logout 

12. Repeat with different variation of steps 

## Known bugs,problems, and temporary fixes:
1. NAV Bar is not properly configured, use device’s back button after navigating to ‘Leaderboard’ or ‘Settings’ Views. 

2. Closing and Re-opening the app when mid-level can de-sync the Current Hint, Solution, and City Image fields yielding a relatively broken game for the user. Do not close the app while levels are available to play. (If you do, create a new account and restart the test case) 

3. Main screen’s layout can display improperly depending on the device’s screen size. Smaller screens cut off the ‘Submit’ and ‘Show hint’ buttons, and Larger screen display the City image improperly. 

