# kiosk-android
Android Kiosk Application

The goal of this project is to create an android application running on tablets and acting as a kiosk to display promotional videos.
The video is fetched from a HTML5 webpage and displayed in a WebView.

The application covers the following features :

- Kiosk mode (disable back/home/recent activities/power, etc buttons). 
- Enter kiosk mode from the application
- Exit kiosk mode with an OTP (One-time password) from Google Authenticator.
- Modify the URL of the website hosting the video

Setup

1. Open Android Studio
2. Click on File -> New -> Project from version control -> GitHub
3. Enter Git Repository URL : https://github.com/coderbunker/kiosk-android
4. Click on "Clone"
5. Build and Run !

Kiosk mode 

- The default password is : 1234
- The default URL is : https://naibaben.github.io/

Both can be changed in the "settings" activity/screen
