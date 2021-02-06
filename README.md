# Stupid-Chat

[DOWNLOAD](https://github.com/dec32/Stupid-Chat/releases/download/v0.1/Stupid.Chat.7z)

A simple P2P instant messaging desktop application relying on stun protocol and UDP.

![](https://github.com/dec32/Image-Storage/blob/master/Initialization.png)

![](https://github.com/dec32/Image-Storage/blob/master/Chatting.png)

The application works fine between users behind less strict NAT types since in those cases UDP hole-punching is very efficient. But when symmetric NAT and port restricted NAT are both involved, things get a lot trickier. 

To achieve NAT traversal in those obnoxious situations a user needs to predict or guess the public port number of the other who is behind symmetric NAT and in the world of computer science by guess we usually mean brutal force. The program would simply scan the port numbers close to the one the STUN server returns to the other user. 

For example, when port number 8000 is shown on the other user's initialization interface and we input that port number into our program, it automatically sends UDP packets to ports like 8000, 8001, 7999, 8002, 7998 until it receives UDP packets from some certain port and mark it as the available port. 

The maximum number of ports to be scanned is set to 4000. It works when one user is behind port restricted NAT and the other one is behind symmetric NAT but I am not sure if it does when both users are behind symmetric NAT.

There are still a lot of problems waiting to be solved in this pile of terrible code.
