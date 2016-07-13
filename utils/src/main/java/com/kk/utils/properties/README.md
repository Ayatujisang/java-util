
1.使用相对路径，如：
InputStream in = MessageService.class.getResourceAsStream("statue.txt");
order_status.txt 和   WeixinMessageService在一个目录下。


2.使用不同的classloader，如   Resources类
InputStream in = Resources.getResourceAsStream(url);

此外还有 getUrlAsStream等方法。

Resources 默认在当前工程中找文件，然后再到对应的jar包中找。

url文件 可以放到resources目录下。
url  不用已 '/' 开头。

3.1

ClassLoader.getResourceAsStream()  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。

RU.class.getClassLoader()  或者  this.getClass()  都默认是 系统类加载器。

InputStream in = RU.class.getClassLoader().getResourceAsStream("1.txt");

或者

InputStream in = this.getClass().getClassLoader().getResourceAsStream("11.txt");

RU和this 在jar包或者当前工程均可。
​
默认在当前工程中找文件，然后再到对应的jar包中找。
1.txt  放在resources目录下。

3.2
InputStream in = RU.class.getResourceAsStream("/1.txt");

RU类在jar包中，1.txt默认从当前工程中找，然后再到对应的jar包中找。
不能在当前工程 使用此写法找jar包中的文件资源。



