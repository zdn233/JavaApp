Please do not change the directory structure.

Java "Breakout" Game -- Dainan Zhang

The game is written in Mac OS.

Additional Features implemented:

(1) Adding powers up that can be earned. Hitting the Center block of the 5x5 blocks (i.e. the row 2 column 2 block with color Cyan where row is 0-4, column is 0-4) will result the paddle double its length. The effect will be remained until all the blocks are cleared. Once all the blocks are cleared, the paddle will change back to its original length and the new 5x5 blocks are generated. Hitting the center block of the next generated 5x5 blocks will result the same effect as described before.

(2) Adding multiple User-friendly game flow control. For details, please look at "2. Game Description, section (3)" below. 

Useful notes and instructions:

1.Command line parameters:

Theres's 2 options provided: either enter 0 parameter or enter 2 parameters

If 0 parameter is entered: uses default FPS and ball speed
default FPS = 30, default ball speed = 20

If 2 parameters is entered: 

(1) The first parameter represents FPS
Recommend integers between 25 - 50

(2) The second parameter represents ball speed
Three choices provided: 1 - slow, 2 - med, 3 - fast

Notice: There's 2 timers implemented, one controls the FPS and one controls the ball speed, so changing the FPS or the ball speed will not affect the other one.
Also, for ball speed, there's only three choices provided as mentioned above, entering other integer will cause an undefined behavior, so is entering 1 parameter or more than 2 parameters.

2.Game description:

(1) Game state: The game is separated into 3 states. 

The first one is the "splash screen state". In this state, you can view the general description of the game and read through some of the important playing guidelines. Hitting START will allow you to enter the next state - "game playing state".

The second state is the "game playing state". In this state, you can play the game as described. Scores will be awarded for the player once some actions are achieved. When the ball goes to the bottom of the game window and disappear, the game is over, then you will enter the "game over state".

The last state is the "game over state". In this state, you can view your final score. Hitting the EXIT will allow you to quit the game.

For each state: You can stay in the "splash screen state" or the "game over state" for as long as you want. For "game playing state, you can stay in this state as long as you are alive.

(2) Paddle control: You can use either the Mouse or LEFT/RIGHT key to control the paddle movement.

For Mouse control, the paddle's top left x coordinates will be determined by the Cursor's x coordinates. Hence, to move the paddle with the mouse, just simply move the Cursor left and right inside the game window, and the paddle will follow the Cursor's movement.

For Key controls, pressing the Left/Right key will move the paddle left/right respectively. The paddle's moving speed is determined by the game ball's moving speed. In general, the faster the ball is moving during the game, the faster the paddle is moving when you press the LEFT/RIGHT key.

(3) Game flow control:

When the player first enter the "game playing state" from the "splash screen state", there will be a short pause so that the player can have a little amount of time to reflect. This can help with the situation of having an immediate game over. 

When the player is in "game playing state", pressed the mouse at any position inside the game playing window will allow the game to pause. This pause can be forever as well. To resume the game, just pressed the mouse at any position inside the game playing window again and the game will resume immediately. This allows the player to play the game with freedom. 

There's multiple way to exit the game. When you are in "splash screen state" or "game over state", you can use the ESC key to exit the game immediately. When you are in "game playing state", you can use the ESC key to quickly enter the "game over state". In any state, pressing the close button will allow you to quit the game.

When you are resizing the window, after each resize event finished, there's a short pause. This helps the player to quickly moving the cursor from the edge back to the paddle so that situation like quick game over after resizing the window will not happen frequently. The recommendation for resizing the game window is first pause the game, resize the window and then resume the game.
