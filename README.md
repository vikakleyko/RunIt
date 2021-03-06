# RunIt

Android application for running, cycling, walking tracking, which allows the user to record activity data (distance, time, etc.), save the route on the map, compare your performance in real time with the best result using voice messages

![IMG_20200317_200114](https://user-images.githubusercontent.com/24653549/76985321-5732ec80-6940-11ea-9911-e758f3f4b908.jpg)
![test3](https://user-images.githubusercontent.com/24653549/76985318-569a5600-6940-11ea-9622-dca247cad3f3.jpg)
![test4](https://user-images.githubusercontent.com/24653549/76985319-5732ec80-6940-11ea-8fe4-39e80aee7957.jpg)
![test2](https://user-images.githubusercontent.com/24653549/76985323-5732ec80-6940-11ea-8384-8c08058f8b9b.jpg)

### Android Studio (Recommended)

(These instructions were tested with Android Studio version 2.2.2, 2.2.3, 2.3, and 2.3.2)

* Open Android Studio and select `File->Open...` or from the Android Launcher select `Import project (Eclipse ADT, Gradle, etc.)` and navigate to the root directory of your project.
* Select the directory or drill in and select the file `build.gradle` in the cloned repo.
* Click 'OK' to open the the project in Android Studio.
* A Gradle sync should start, but you can force a sync and build the 'app' module as needed.

### Gradle (command line)

* Build the APK: `./gradlew build`

## Running the Sample App

Connect an Android device to your development machine.

### Android Studio

* Select `Run -> Run 'app'` (or `Debug 'app'`) from the menu bar
* Select the device you wish to run the app on and click 'OK'


### Gradle

* Install the debug APK on your device `./gradlew installDebug`
* Start the APK: `<path to Android SDK>/platform-tools/adb -d shell am start io.keen.client.android.example/io.keen.client.android.example.MyActivity`

### Permissions

- Full Network Access.
- View Network Connections.
- Location.
- Run at startup.
- Read and write access to external storage.




