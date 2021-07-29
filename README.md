# Spotify Karaoke (Draft)
##

## Concept
The goal of this project is to develop a mobile and web application that allows users to link their Spotify accounts, search for a desired song, and sing along to the lyrics as they appear on the screen.

## Curent Working Demo:
https://youtu.be/CEyYxzh0lBI

### App Screens
- Sign In Screen
- Song Selection Screen
- Graphic Lyric Display Screen

#### Register / Sign In
- Authentication will be set up using firebase. 
- Users will be able to sign in or register for the app using their email and a created password
- Once signed in to the app, users will be able to sign in to their spotify and connect it to the application 
- This will not store or use user data other than to deliver the data needed for the app to deliver its functionality

#### Song Selection
- Once users log in, they will be placed in this screen where they can search for songs
- Search results will be displayed in a scrolling list below the search box and will show all available songs that contain the search key-words
- Once the user selects a song, they will be transitioned to the graphic lyric display screen

#### Graphic Lyric Display
- This screen will be the karaoke screen that plays music and display lyrics for the current song being played by the user 
- The user will be able to play, pause, and skip to anywhere in the song being played 
- When the user ends the song or hits the back button, they will be taken back to the song selection screen where they can choose another song or log out of the application


## Figma Prototype 
#### [UI/UX Implementation](https://www.figma.com/file/HyXK3TU3Dv6faWwYbkN3v8/Spotify-Karaoke-App)
- The prototype shows a mockup of the different screens the project aims to display
- Click play on the top right to navigate through

## Proposed APIs
#### [Spotify API](https://developer.spotify.com/documentation/web-api/)
#### [Genius API](https://docs.genius.com/)
#### [LyricsGenius API](https://lyricsgenius.readthedocs.io/en/master/)
#### [Audio DB API](https://rapidapi.com/theaudiodb/api/theaudiodb) 
