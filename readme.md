FlipFlop
=====================

### Contributors  
* Michael Lombardo
* Rishabh Patel
* Danny To

#### Current Release : FlipFlip V1.5
##### Compiled Android API Version : Min 25 - Max 28 - Target 28

1 &nbsp;&nbsp;&nbsp;&nbsp;Introduction
============

FlipFlip is an app created to merge the gap between Reddit and YikYak. An anonymous location based discussion board, our app allows users to create an account which they can create posts, and be involved in discussion. The app has a real-time database hosted by Firebase, as well as a personal local database by SQLite. The app contains six activities which contain all of the interaction, which ranges from registering to showing post discussion boards.

2 &nbsp;&nbsp;&nbsp;&nbsp;Data Structure
============

### &nbsp;&nbsp;&nbsp;&nbsp;Firebase
  * **Posts** : Contains the parent of each Comment element
    * child_id
      * title
      * contents
      * location
      * reputation
      * user_id
  * **Comments** : Contains tree of Comments for each Post element
    * child_id
      * contents
      * user_id
  * **Users** : Contains all Users
    * child_id
      * username
      * password
      * dateCreated
      * salt : Randomly generated string appended to password to improve encryption.

### &nbsp;&nbsp;&nbsp;&nbsp;SQLite
  * **Likes** : Contains local users posts which were liked.
    * id
      * post_id
      * user_id
  * **Dislikes** : Contains local users posts which were disliked.
    * id
      * post_id
      * user_id

3 &nbsp;&nbsp;&nbsp;&nbsp;Activities
============
* **Show Posts (Main Activity)** : Display all posts.
* **Register** : Register a new user to the database.
* **Login** : Login using an existing user to post and comment functions.
* **Profile** : Display user details and display user's likes and dislikes.
* **Add Post** : Create a new post to the database.
* **Show Discussion Board** : Display the post displaying all contents, allows logged in users to comment.
* **License Agreement** : Shown on registration activity, clickable link to GNU license. (From Lab Assignment)

4 &nbsp;&nbsp;&nbsp;&nbsp;Appendix
============
This project was completed in equal contribution for CSCI 4100U - Mobile Devices.
