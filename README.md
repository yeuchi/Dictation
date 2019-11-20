# Dictation
Android dictation exercise

Google Play : https://play.google.com/store/apps/details?id=com.ctyeung.dictation

## 2013 Version 1
This was originally implemented in Java as an exercise to demonstrate the speech to text feature.
It only supported portrait mode, dictation and file save.

## 2019 Version 2 (work in progress)
Update currently includes the following.
- Kotlin rewrite
- Layout: landscape addition
- Add FloatingActionButtons

At startup, READ/WRITE permission is requested.  Dictation is functional without write permission but no text will be saved or shared.
<img src="https://user-images.githubusercontent.com/1282659/69260401-6fa71c00-0b85-11ea-81dc-ff457bf1eba7.jpg" width="200"> 
<img src="https://user-images.githubusercontent.com/1282659/69260400-6fa71c00-0b85-11ea-8566-a12e69bd1746.jpg" width="200"> 
<img src="https://user-images.githubusercontent.com/1282659/69261893-0f65a980-0b88-11ea-8f77-805bfa4e737f.jpg" width="200"> 

User dictations are separated into verses.  A versus represents no more than 1 minute of user's speech.  When user stops speaking for about 2 seconds (default EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS), Google speech recognition processes voice recording to text.
<img src="https://user-images.githubusercontent.com/1282659/69056365-6aa36a80-09d5-11ea-8a2c-0dbcde47475a.jpg" width="200"> 

## Test
This application has been tested on Samsung Galaxy9.

# References:

1. Kotlin "Discover the core Android API for Speech Recognition." by Younes Charfaoui, July 30, 2019
https://medium.com/@mxcsyounes/discover-the-core-android-api-for-speech-recognition-4591e87fd55b

2. "Anddroid Speech to Text Tutorial" by James, June 1, 2017
http://www.coderzheaven.com/2017/06/01/android-speech-to-text-tutorial/

3. Java: "Developing Android* Applications with Voice Recognition Features" by Stanislav P. (Intel), September 26, 2013
https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features

4. Google Play: QuickEdit text editor
https://play.google.com/store/apps/details?id=com.rhmsoft.edit&hl=en_US&pli=1

5. "Android kotlin - Request permissions at runtime example", by Saiful Alam, March 16, 2018
https://android--code.blogspot.com/2018/03/android-kotlin-request-permissions-at.html

6. Cloud Speech-to-Text Basics
https://cloud.google.com/speech-to-text/docs/basics

