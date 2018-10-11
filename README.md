# kiosk-android
Android Kiosk Application

The goal of this project is to create an android application running on tablets and acting as a kiosk to display promotional videos.
The video is fetched from a HTML5 webpage and displayed in a WebView.

The application covers the following features :

- Kiosk mode (disable back/home/recent activities/power, etc buttons). 
- Enter kiosk mode from the application
- Exit kiosk mode with an OTP (One-time password) from Google Authenticator.
- Modify the URL of the website hosting the video
- Secret touch command to open OTP pin (touch screen 4 times in the right pace)

Setup

1. Open Android Studio
2. Click on File -> New -> Project from version control -> GitHub
3. Enter Git Repository URL : https://github.com/coderbunker/kiosk-android
4. Click on "Clone"
5. Build and Run!
6. Allow camera and display overlay
6. Scan OTP password
8. Set app as home

Config video

1. Setup website with [kiosk-web](https://github.com/coderbunker/kiosk-web)
2. Set custom video in index.html
3. Open settings in app and enter url to website

Backend configuration concept
![swimlanes](https://github.com/coderbunker/kiosk-android/blob/documentation/files/swimlanes.png)
