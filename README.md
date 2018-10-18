# kiosk-android
Android Kiosk Application

The goal of this project is to create an android application running on tablets and acting as a kiosk to display promotional videos.
The video is fetched from a HTML5 webpage and displayed in a WebView.

The application covers the following features :

- Kiosk mode (disable back/home/recent activities/power, etc buttons). 
- Enter kiosk mode from the application
- Exit kiosk mode with an OTP (One-time password) from Google Authenticator.
- Modify the URL of the website hosting the video
- Claim the tablet and modify the website of the tablet online
- Secret touch command to open OTP pin (touch screen 4 times in the right pace)
- Viewer statistics

Setup:

1. Open Android Studio
2. Click on File -> New -> Project from version control -> GitHub
3. Enter Git Repository URL : https://github.com/coderbunker/kiosk-android
4. Click on "Clone"
5. Build and Run!
6. Allow camera and display overlay
7. Scan OTP password
8. Deactivate Google assistant
9. Disable automatic system and software update in the settings
10. Set app as home

Config video:

1. Setup website with [kiosk-web](https://github.com/coderbunker/kiosk-web)
2. Set custom video in index.html
3. Open settings in app and enter url to website

Claiming a tablet:

1. Open settings view
2. Select claim tablet
3. Enter a group name, a device name and a passphrase

Edit url of clamimed tablet:

1. Open https://kiosk-app-210502.firebaseapp.com ([Repository](https://github.com/coderbunker/kiosk-admin))
2. Search by group name
3. Select the tablet by device name
4. Enter passphrase
5. Edit url and save

The tablet applys the url almost instantly.

Backend configuration concept
![swimlanes](https://github.com/coderbunker/kiosk-android/blob/documentation/files/swimlanes.png)
