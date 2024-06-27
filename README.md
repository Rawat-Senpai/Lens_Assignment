# Task Application

This repository contains a task application developed as part of a company assignment. The application is designed following modern Android development practices and includes various advanced features.

## **Features**

- **MVVM Architecture**: Ensures a clean separation of concerns and facilitates easier testing and maintenance.
- **Room Database**: Used for efficient and persistent data storage.
- **SharedPreferences**: Utilized for storing simple key-value pairs.
- **Dagger Hilt**: Dependency injection to manage dependencies and improve code modularity.
- **Biometric Authentication**: Added fingerprint authentication for enhanced security (Note: Fingerprint authentication is not visible during screen recording).
- **XML Layouts with Navigation Graph**: All layouts are designed in XML, utilizing the Navigation Graph for fragment transitions.
- **MotionLayout**: Applied to the settings screens for smooth animations.
- **Multilingual Support**: The application supports both Hindi and English languages.
- **Location Saving**: Implements functionality to save location data as described in the assignment.
- **Theme Support**: Supports both dark and light themes, with a smooth transition similar to the Telegram application.

## **Pending Tasks**
- **Notification Reminders**: Implementation of notification reminders from Firebase is pending.
- **Test Cases**: Test cases are not included in this build but will be added later.

**Features**
## ![code structure](https://github.com/Rawat-Senpai/Lens_Assignment/assets/88794531/57490a81-d68c-4f17-b37b-56f7784fccf8)
## **Code Structure**

This application follows the MVVM architecture with Dagger Hilt for dependency injection. It currently supports a local database using Room.

## **Home Screen **
![home](https://github.com/Rawat-Senpai/Lens_Assignment/assets/88794531/aae1d728-df7a-4b9f-8dce-2dbaaa39fed4)

On the home screen, users can view all pending and completed tasks. Users can also search for tasks. A floating action button allows users to navigate to the add task section.


## **Dashboard Activity**
![dashboard screen ](https://github.com/Rawat-Senpai/Lens_Assignment/assets/88794531/13b60ad5-f2f3-4130-aaa7-e210eb538d45)

In the dashboard activity, users can see all pending tasks and view the data in a pie chart that categorizes tasks by high, mid, or low priority. Users can also see the number of tasks completed and the number of tasks not completed. 


- For the pie chart, the application uses the `MPAndroidChart` library:

- For the progress bar, the application uses the Skydoves library:



https://github.com/Rawat-Senpai/Lens_Assignment/assets/88794531/5bef8740-cadc-4732-9a38-9ffe2ee71615
## **Settings Screen **
MotionLayout: Used for smooth animations, replicating the transitions found in Telegram.
Theme Change: Allows users to switch between dark and light themes with a transition effect similar to Telegram.

