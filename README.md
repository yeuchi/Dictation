# Dictation

This is an exercise to try Google SpeechHelper as defined by Stanislav of Intel.
The excellent article is “Develoing Android* Application with Voice Recognition Features".
https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features

### Google SpeechRecognizer
https://developer.android.com/reference/android/speech/SpeechRecognizer.html?hl=es

### Setup
Apparently offline processing is possible for some devices with Jellybean and later.  
Goto Settings->Language and input->Voice search->Offline speech recognition and install your language packet.  
Inside the Dictation project Manifest file, comment out this line for internet permission.

<uses-permission android:name=”android.permission.INTERNET” />


