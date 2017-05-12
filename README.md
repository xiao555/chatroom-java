# Java聊天室

这学期刚开始学Java，并不是很了解，实现上参考了大神们的代码，各取所长：

1. 通信服务上，参考 https://github.com/luohaha/LightComm4J 实现了一个专门为聊天室打造的异步通信库，相关代码在 `src/com` 文件夹下。
2. UI，以及一些逻辑 参考的 https://github.com/ThugKd/ChattingRoom , 不得不说我大软院的学长们写的还是很不错的。

使用这么一个异步通信库的好处就是， 开发者只需要通过`param` 来定义好相关参数以及事件回调即可。具体可以看看[LightComm4J](https://github.com/luohaha/LightComm4J)上面的例子。
简洁而优雅，唯一美中不足的是前后端通信只能传字符串！！！！我写了半天其他地方都写好了，才发现这个只能传字符串！！！

好吧，痛定思痛，我自己仿照着写一个吧(因为这个实现太优雅了)。事件处理采用LightComm4J这种异步回调的模式，前后端通信通过ObjectOutputStream、ObjectInputStream来传输ChatBean这个对象，通信信息都保存在这个对象里。
通信库的接口与LightComm4基本相同，我又加了几个：OnClientRead,OnFileRead 以及 OnFileRecive, 其他的参考文档：[LightComm4J](https://github.com/luohaha/LightComm4J)

## 实例

### Simple

我自己也写了个简单的例子，可以看一下`src/simple`目录下的两个文件，命令行版的聊天室，输入`exit`下线，输入其他群发，带上下线提醒

### ChatRoom

完成的ChatRoom实现，服务端入口`src/ChatRoomServer`, 客户端入口`src/ChatLogin`, 
1. 没有数据库，用户信息保存在`Users.properties`目录里。
2. 发送消息时要在右面的在线用户里选择要发送的对象，可多选。
3. 双击用户激活文件传输功能

## LICENSE

<a href="http://www.wtfpl.net/"><img
       src="http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png"
       width="80" height="15" alt="WTFPL" /></a>

🌝 You just DO WHAT THE FUCK YOU WANT TO.






