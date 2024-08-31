# Assigment

## Description
* An Android application designed to manage user profiles with functionalities like adding, editing, and viewing user details. The app also allows users to upload and update profile photos and displays a list of users efficiently using a RecyclerView.
* The app fetches user data from the local database or API and updates the list dynamically. If there are existing users in the database, they are shown in a scrollable list format.
* Each user is displayed with their profile picture, first name, last name, and email address in a simple and organized layout.
* At the top right corner or a floating button, you’ll find an Add User icon. This button is used to add new users to the list.

### Features
* Add new users with first name, last name, email, and a profile photo.
* Edit existing user details, including updating the profile photo.
* View detailed user information in a dialog.
* Delete users from the list.
* Efficient user list display using RecyclerView with automatic data updates.
* We ensured the output image maintained the same dimensions as the input image. After implementing the paddings, we performed convolution on the padded image and stored the results in the output image.

### How to Use the Application
* the app transitions to the Main Screen, which displays a list of users using a RecyclerView.
  <p align="center">
    <img src="images/home screen.jpg" width="270px">
    </p>

### Adding a New User:
* Click on the Add User button.
* Fill in the user's first name, last name, and email.
* Click on Add Photo to upload a profile picture from your gallery.
* Press Save to add the user to the list.
    <p align="center">
    <img src="images/add user.jpg" width="270px">
    </p>
### Editing a User:

* Click long press on any user's profile in the list to open dialog with the option update and delete.
* Choose the update option from the dialog.
* Modify the user details and click on Change Photo if needed to update the profile picture.
* Press Save to apply the changes.    
    <p align="center">
    <img src="images/edit user.jpg" width="270px">
    </p>

 ### Deleting a User:

* Long-press on a user's profile in the list, Select the Delete option to remove the user from the database.
 
    <p align="center">
    <img src="images/option dialog.jpg" width="270px">
    </p>

    



