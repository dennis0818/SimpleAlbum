# SimpleAlbum
本人小白自学编程，把这段时间学到的和查到的所有知识点攒起来写了这个小程序，当作总结吧。第一次自己从搭架子到动手实现，写了这个小程序，期间各种查资料各种改觉得很有意思，最后终于算是能把想法实现了，虽说还是个雏形，还有很多地方需要改进，但是发现越来越喜欢编程这件事了。
首先介绍一下“SimpleAlbum”，Android端完成了登录、图片的仿个人相册浏览、图片的上传和更新的功能，服务器端用Shiro+SSM+MySQL搭的。

## 一、程序描述
### 登录模块
  服务器端用Shrio完成认证过程（Shrio用Subject.login(token)方法实现认证，用户名密码的realm用.ini文件配置），认证是否通过用Json传回手机端，通过则返回true跳转AlbumActivity，不通过返回false，留在LoginActivity。
 手机端的MainActivity访问“/check”路径，首次登陆与服务器建立会话，服务器返回false，跳转至登陆面页LoginActivity，登陆成功后再次登录，由于手机端已经与客户端建立会话，服务器保存有相应的会话ID，所以返回true，进入相册面页AlbumActivity。
 
 **用户名和密码的缓存**
 
 用okHttp的cookieJar来实现对cookie的存取和携带，需要自己实现CookieManager类，其中用PersistentCookieStore类将cookie存入sharedPreference，用SerializableOkHttpCookies类将cookie进行序列化。
 
 如果用户未登录的话，服务器返回false，手机端跳转至loginActivity。我这里用post明文传输用户名和密码。访问“/login”路径。
 
 这其中用户名和密码的传输可以用加密的方式传输，比如加盐并且多次单向散列，再安全一点就用Https链接传输，涉及到服务器端的自签名证书和手机端处理Https请求的方法。
### 相册模块
AlbumActivity用来展示相册，用一个listview套自定义的NineGridLayout。NineGridLayout的原理和源码是参考的：https://blog.csdn.net/hmyang314/article/details/51415396 ，我在那位大哥的基础上简化了一些。
AlbumActivity向服务器“/showMessages”路径请求主页数据，服务器读取数据库后用Json返回分页信息，其中图片以图片名形式传回，在手机端进行“服务器图片路径+图片名=图片url”的拼接再传给NineGridLayout的adapter。
AlbumActivity还初步实现了下拉加载，但是有时会出bug，没想到怎么铲，具体解决办法只能慢慢思考了。
### 上传模块
上传模块分为上传和本机图片浏览，这部分用了挺长一段时间琢磨，本人写这个应用也是从这部分开始写的。

### 总结
这个小程序还有很多功能没有实现，比如：多用户登录，相册的评论功能，图片的点击放大浏览等等。

## 展示

![image](https://github.com/dennis0818/SimpleAlbum/blob/master/other/LoginOperation2.gif) ![image](https://github.com/dennis0818/SimpleAlbum/blob/master/other/UploadOperation2.gif)


