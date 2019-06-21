1、安装jdk 1.6 for 32 bit system,
   配置系统环境变量：
   JAVA_HOME=E:\Java\jdk1.6.0_19    // 名称必须是JAVA_HOME,如果修改，请修改 /localchat/build.xml 中对应的java.home配置
   CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib;     //找到classpath时，请在末尾添加，而不是删除重建。path方法一样。
   PATH=%PATH%;%JAVA_HOME%\bin;

   下载apache-ant-1.8.2，解压至E盘
   配置ant环境变量：
   ANT_HOME=E:\apache-ant-1.8.2        //名称必须是ANT_HOME,如果修改，请修改 /localchat/build.xml 中对应的ant.home配置
   CLASSPATH=%CLASSPATH%;%ANT_HOME%\lib;
   PATH=%PATH%;%ANT_HOME%\bin;

   下载安装JMF2.1.1e,默认安装
   配置JMF环境变量
   JMF_HOME=C:\Program Files\JMF2.1.1e   //名称必须是JMF_HOME,如果修改，请修改 /localchat/build.xml 中对应的jmf.home配置
   CLASSPATH=%CLASSPATH%;%JMF_HOME%\lib;
   PATH=%PATH%;%JMF_HOME%\bin;


2、解压文件目录，保持文件目录结构
 
  LocalChat --
	     |--chatcommon
	     |--chating
	     |--chatserver
	     |--build.xml

>cd localchat.home                      //使用命令行进入localchat根目录
>ant                                    //使用ant命令进行编译

3、启动服务器

   进入localchat.home/chatserver/dist/bin/
   双击run.bat                          //默认启动是使用javaw，不会打印控制台信息;如果需要查看控制台，编辑run.bat修改javaw 为java

4、启动客户端
   进入localchat.home/chating/dist/bin
   修改chat.properties 中对应的选项，特别是服务器ip地址
   双击run.bat   启动客户端             //默认启动是使用javaw，不会打印控制台信息;如果需要查看控制台，编辑run.bat修改javaw 为java

5、各项下的build、dist、src，分别是  编译目录、运行目录、原文件目录，如只要运行文件，则目录格式建议如下：
	Chating--
		|--bin
		|--docs
		|--image
		|--lib
	即客户端字样项目根目录名称必须是Chating，内部的

6、清除编译
>cd localchat.home                      //使用命令行进入localchat根目录
>ant clean                              //使用 clean 任务进行清除

   