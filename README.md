# Software Design Final Project
## Vrije Universiteit Amsterdam, Spring 2019
### By Lanie Preston, Floor Kouwenberg, Lucien Martijn, Sophie Vos, and Chuyi Tong

In our system, the user can specify colors of boxes that need to be found. For each color, a specific rover 
is being built in the rover factory that searches for that specific color. Once the Rover has found the specific color, 
it updates the coordinate of the box into the Task class. Afterwards, the Rover keeps on searching as the program description 
specifies that the whole environment needs to be covered by the Rovers. Whenever new blocks of the specified color have been 
found, they are added to the Task class. As our Rovers are of different types, each Rover needs to cover the whole area 
before the mission stops. The error situation would be that the timer has reached its limit and the rovers were not able to 
cover the whole area.

The execution of our system can be seen on YouTube using the following link:
https://www.youtube.com/watch?v=HFfYeaq8G8M&feature=youtu.be
