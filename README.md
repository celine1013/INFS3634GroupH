# INFS3634GroupH
# App Name: MyQuiz
# Author: Celine Liu, Yifei Sun
# Last Edit: 22/oct/2017

#Description:
  This mobile application provides the quiz game with different game modes as well as record system.
  The aim of this application is to help students of INFS3634 to do revision while having fun.

  ## Outstanding Features / Design:
    1.	Quiz System
      •	Users can customize quizzes with different speed and difficulties using pop-up dialog
      •	Quick Start mode allows the users to have endless quiz (stops when answer incorrectly)
      •	Have different background music based on the difficulty of the quiz taken
      •	Devices vibrates when user answer incorrectly
      •	The answer options would change their colours based on correctness
      
    2.	Record System
      •	The past records would be recorded in history page
      •	When user reach new highest score, notification would be made
      
    3.	Database System
      •	SQLite
      •	Content Provider  
        
  ## Assumption
    1. For categorized quiz, the number of questions is limited to 5 (for revision usage);
    2. For quick start mode, the number of questions is unlimited (for quizing usage);
    3. All the questions displayed in quizzes are randomised picked
    
  ## Future extension
    1. multi-player
      * cloud database
      * user ranking
      * online competition
    2. implement explanation of the answer when answer questions
    3. categorized records based on different game mode
