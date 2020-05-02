Please do not change the directory structure.

Dainan Zhang --- Ragdoll Project

Completed in Mac OS

Additional Features: Multiple dolls

An additional model -- snake model is implemented. 

1. The horizontal body part in the middle of the model is the torso. Click on it and drag it will move the whole model as a whole.

2. On the right of the torso is the "Butt". User can rotate it based on its pivot. The degree constraint for that body part is 30 degree.

3. On the left of the torso is the "Neck". User can rotate it based on its pivot. Initially, the Neck is perpendicular to the torso. The degree constraint for the body part is 45 degree.

4. On the Top of the Neck is the "Head". User can rotate it based on its pivot. Initially, the Head is perpendicular to the Neck. The degree constraint for the body part is 45 degree.


Important instructions:

!!! Please be patient, the first time when you try to run this ragdoll app, it might be a little bit slow. However every functionality is correctly implemented, just wait a little bit if it is not reacting !!!

1. There's three buttons in the Toolbar. "Reset" reset the doll model or the snake model. "About" will show a dialog presenting the information mentioned in the assignment. "Change" will change the current model to other model. When you clicked change, all the data related to the current model will disappear. (i.e. when you clicked change and then clicked change again to switch back, the doll is reset).

2. All the Rotation and Drag is implemented the way the assignments described. When Performing rotation on the body part with degree constraints, the body part will not rotate when the cursor is out of the body part even if the finger is not up. So this means when you clicked on a body part and try to rotate it based on its pivot, you have to let your finger remained inside that body part.

3. For scale, the program is implemented using the focal point to detect which part should be scaled. Thus, to scale a body part, (In Mac) one should first press "shift " and then move the cursor to the location where you want to locate your focal point. Then press "cmd", the "shift" should still be pressed. Next you will see a scale gesture on your screen. with the focal point at the previous location you choose. Now you move the cursor, the cursor is where your finger should be placed. For simple scale, this should not matter as long as you placed the focal point inside the body part you want to scale correctly before. clicked on the screen and drag the cursor will scale the body part (It is recommended to put 2 fingers on somewhere on the screen where no body parts are involved just for testing simplicity). The program also support scale and rotate at the same time. All the scaling related behavior is implemented the way the assignment specified as long as the user pick the focal point and use the scale gesture provided correctly.


References:
Jeff's sample code: SceneGraph and DrawShapes