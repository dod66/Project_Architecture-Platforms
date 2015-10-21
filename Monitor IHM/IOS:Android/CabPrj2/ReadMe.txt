######################
########README########
######################
######iOS CLIENT######
######################

In this program there are many parts to understand.

First we have a HTTP Server wich send to the client a JSON frame.
This frame contains the WS Server's adress. For instance we have : ws://172.30.0.190:4000

After we get this information, we can connect the WS client to the WS Server.

When it's done, the server send us a JSON frame, which is parsed and analyzed by our program.

When this step is done, we can draw on the map, the points.