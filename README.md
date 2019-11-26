# Dictation <img src="https://user-images.githubusercontent.com/1282659/69678209-87414180-106a-11ea-833a-e03f49ba6154.png" width="40"> 
Android dictation exercise 

Google Play : https://play.google.com/store/apps/details?id=com.ctyeung.dictation

## 2013 Version 1
This was originally implemented in Java as an exercise to demonstrate the speech to text feature.
It only supported portrait mode, dictation and file save.  Radiology medical viewers such as GE already had this feature.

## 2019 Version 2 (work in progress)
Update currently includes the following.
- Kotlin rewrite
- Layout: landscape addition
- Add FloatingActionButtons
- Add Share (email/facebook/drive/etc)

At startup, READ/WRITE permission is requested.  Dictation is functional without write permission but no text will be allowed to save or share (email/facebook/drive/etc).

<img src="https://user-images.githubusercontent.com/1282659/69260401-6fa71c00-0b85-11ea-81dc-ff457bf1eba7.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/69260400-6fa71c00-0b85-11ea-8566-a12e69bd1746.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/69261893-0f65a980-0b88-11ea-8f77-805bfa4e737f.jpg" width="200"> 

User dictations are separated into verses.  A versus represents no more than 1 minute of user's speech.  When user stops speaking for about 2 seconds (default EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS), Google speech recognition processes voice recording to text.\
<img src="https://user-images.githubusercontent.com/1282659/69679560-0be18f00-106e-11ea-9a59-d0da0778b470.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/69597008-219d7880-0fca-11ea-845c-19ba59b01393.jpg" width="200"> 

<img src="https://user-images.githubusercontent.com/1282659/69678091-26196e00-106a-11ea-832d-a511bb0e5509.jpg" width="450">  <img src="https://user-images.githubusercontent.com/1282659/69678101-29145e80-106a-11ea-9030-889b9d802c5d.jpg" width="450"> 

Save all to local text file.  Set directory (default: \DICTATION) and file name (default: dictate.txt).\
<img src="https://user-images.githubusercontent.com/1282659/69679849-ca9daf00-106e-11ea-9db8-6d6775da0b4c.jpg" width="200"> 
 
Select N verse(s) to delete selected.  Delete all if none selected.\
<img src="https://user-images.githubusercontent.com/1282659/69597287-15fe8180-0fcb-11ea-8458-e5b3fe8fb6be.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/69679555-084e0800-106e-11ea-9dab-2f09c0a7a52d.jpg" width="200">  <img src="https://user-images.githubusercontent.com/1282659/69679556-08e69e80-106e-11ea-8d3b-33440c9d60d4.jpg" width="200"> 

## Definitions
- Dictation: a voice recording converted to text via Google Speech recognition service.
- Verse: (less than 1 minute) segment of converted text.
- Stanza: collection of verses.

## Test
This application has been tested on Samsung Galaxy9.

# References:

1. Kotlin "Discover the core Android API for Speech Recognition." by Younes Charfaoui, July 30, 2019\
   https://medium.com/@mxcsyounes/discover-the-core-android-api-for-speech-recognition-4591e87fd55b

2. "Anddroid Speech to Text Tutorial" by James, June 1, 2017\
   http://www.coderzheaven.com/2017/06/01/android-speech-to-text-tutorial/

3. Java: "Developing Android* Applications with Voice Recognition Features" by Stanislav P. (Intel), September 26, 2013\
   https://software.intel.com/en-us/articles/developing-android-applications-with-voice-recognition-features

4. Google Play: QuickEdit text editor\
   https://play.google.com/store/apps/details?id=com.rhmsoft.edit&hl=en_US&pli=1

5. "Android kotlin - Request permissions at runtime example", by Saiful Alam, March 16, 2018\
   https://android--code.blogspot.com/2018/03/android-kotlin-request-permissions-at.html

6. Cloud Speech-to-Text Basics\
   https://cloud.google.com/speech-to-text/docs/basics
   
7. Android Room with a View - Kotlin\
   https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#8

