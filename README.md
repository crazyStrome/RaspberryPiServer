
# RaspberryPiServer
树莓派小车的控制端，可以接收手机端的指令，控制GPIO的输出，从而使小车移动。
[手机端app代码](https://github.com/crazyStrome/CarControllerApp)

##  文件结构
|--Car.java<br>
|--Client.java<br>
|--CrazyStromeMap.java<br>
|--Main.java<br>
|--Protocol.java<br>
|--Wheel.java<br>

##  [Wheel.java](https://github.com/crazyStrome/RaspberryPiServer/blob/master/src/RaspberryPi/Wheel.java)
在该文件中，用来控制一侧的轮子状态：正转、反转、停止、调速等。
使用[pi4j](https://pi4j.com/)库来实现Java操作树莓派底层的GPIO。
GPIO连接电机驱动模块L298N，进而控制电机。

##  [Car.java](https://github.com/crazyStrome/RaspberryPiServer/blob/master/src/RaspberryPi/Car.java)
Car类是基于Wheel类实现的更顶层的类，可以实现小车的左转、右转以及转速调整等。

##  [Client.java](https://github.com/crazyStrome/RaspberryPiServer/blob/master/src/RaspberryPi/Client.java)
Client.java是基于以手机作为服务器，树莓派小车作为客户端的客户端实现代码，不过为了简便，使用小车作为客户端代替。

##  [Main.java](https://github.com/crazyStrome/RaspberryPiServer/blob/master/src/RaspberryPi/Main.java)
使用树莓派作为服务器，相应连接请求，每收到一个请求，就添加到用户map中，方便以后标识。

##  [CrazyStromeMap.java]()
该类用来储存连接到树莓派的用户ip端口和用户名，使用sychronized关键字实现互斥功能。
