# Book-Listing-App

Android app which takes in user's search query and displays a list of books matching the same. 

This App uses the Google Books API to display the list of books matching the user's search keywords.

The Book name is dispayed along with the author(s)'s name(s) and a thumbnail of the cover of the book.

When the user clicks on a specific book, the Google Books Store link is opened in a browser.

# UI/UX tweaks have been made like:
  1. Handling device rotation.
  2. Handling no internet connection/invalid requests.
  3. Handling a query for which no books exist.
  4. Enabling onKeyListener so the search input is also taken when the user presses "Enter" on the keyboard, instead of having to press the      "GO" button everytime.

# Minimum API level : 15

# Test devices:
  1. Nexus 5X (Android 8.0) - Emulator
  2. Samsung Galaxy S7 Edge - Physical Device.
