# Dictation

This is an exercise to try Google SpeechHelper in English.
It performs with amazing quality on/off line and support multiple languages.
This work extends from the excellent article, “Develoing Android* Application with Voice Recognition Features" by Stanislav of Intel.
https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features

### Google SpeechRecognizer
https://developer.android.com/reference/android/speech/SpeechRecognizer.html?hl=es

### Setup
Apparently offline processing is possible for some devices with Jellybean and later.  
Goto Settings->Language and input->Voice search->Offline speech recognition and install your language packet.  
Inside the Dictation project Manifest file, comment out this line for internet permission.

Manifest: <uses-permission android:name=”android.permission.INTERNET” />

![screen shot 2018-01-07 at 2 24 26 pm](https://user-images.githubusercontent.com/1282659/34653776-a7139062-f3b6-11e7-98b3-cdad1f862deb.png)
![screen shot 2018-01-07 at 2 24 41 pm](https://user-images.githubusercontent.com/1282659/34653771-9d18f7a0-f3b6-11e7-975a-9b9a76b41f69.png)

Rotation is NOT handled in this application which maybe a good thing if you want to start-over.
ASCII text file storage of the dictation content is available.

![screen shot 2018-01-07 at 2 24 47 pm](https://user-images.githubusercontent.com/1282659/34653772-9e86ef7a-f3b6-11e7-9e35-4f6321baad84.png)
![screen shot 2018-01-07 at 2 24 34 pm](https://user-images.githubusercontent.com/1282659/34653773-a03d93e6-f3b6-11e7-9f33-43a347508b2d.png)
![screen shot 2018-01-07 at 2 24 54 pm](https://user-images.githubusercontent.com/1282659/34653775-a1ce899a-f3b6-11e7-93b9-ac56bb3636fa.png)

### Google play
Install it here..
https://play.google.com/store/apps/details?id=com.ctyeung.dictation&hl=en
