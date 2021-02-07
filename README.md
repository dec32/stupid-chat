# Stupid-Chat

A simple P2P instant messaging desktop application relying on stun protocol and UDP.

利用 STUN 协议和 UDP 实现的简单 P2P 聊天客户端。

![](https://github.com/dec32/Image-Storage/blob/master/Initialization.png)

![](https://github.com/dec32/Image-Storage/blob/master/Chatting.png)

## Download | 下载

You can click the link below to download the program:

你可以点击下面的链接，下载来玩一玩：

[DOWNLOAD](https://github.com/dec32/Stupid-Chat/releases/download/v0.1/Stupid.Chat.7z)

## Introduction | 介绍

The program works fine between users behind less strict NAT types since in those cases UDP hole-punching is very efficient. But when symmetric NAT and port restricted NAT are both involved, things get a lot trickier. 

当聊天双方的 NAT 类型不严格时，UDP 打洞十分有效，因此程序往往运行得十分顺利。但当端口限制型 NAT 和对称型 NAT 出现时，事情就麻烦了。

To achieve NAT traversal in those obnoxious situations a user needs to predict or guess the public port number of the other who is behind symmetric NAT and in the world of computer science by guess we usually mean brutal force. The program would simply scan the port numbers close to the one the STUN server returns to the other user. 

当聊天中的一方处在对称型 NAT 后时，另一方需要去预测或者猜测其端口号。而所谓猜测，说白了就是暴力破解。程序在猜测端口时，会在对方通过 STUN 服务器获取的公网端口的附近扫描。

For example, when port number 8000 is shown on the other user's initialization interface and we input that port number into our program, it automatically sends UDP packets to ports like 8000, 8001, 7999, 8002, 7998 until it receives UDP packets from some certain port and mark it as the available port.

譬如说，当对方的初始化界面显示 NAT 分配的公网端口为 8000 时，我方则会尝试往 8000、8001、7999、8002、7998 这些端口发送心跳报文。而当我方收到对方的心跳报文时，就把这个报文的源端口标记为对方实际可用的端口。

The maximum number of ports to be scanned is set to 4000. It works when one user is behind port restricted NAT and the other one is behind symmetric NAT but I am not sure if it does when both users are behind symmetric NAT.

程序最多会猜测 4000 个端口，当双方有一方在端口限制型 NAT 后而另一方在对称型 NAT 后时，这种暴力猜测的方法一般是能成功的。但当双方都在对称型 NAT 后时，这种方法就未必有效了。

There are still a lot of problems waiting to be solved in this pile of terrible code.

这坨糟糕代码里还有许多需要改进的地方。

