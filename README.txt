1����װjdk 1.6 for 32 bit system,
   ����ϵͳ����������
   JAVA_HOME=E:\Java\jdk1.6.0_19    // ���Ʊ�����JAVA_HOME,����޸ģ����޸� /localchat/build.xml �ж�Ӧ��java.home����
   CLASSPATH=%CLASSPATH%;%JAVA_HOME%\lib;     //�ҵ�classpathʱ������ĩβ��ӣ�������ɾ���ؽ���path����һ����
   PATH=%PATH%;%JAVA_HOME%\bin;

   ����apache-ant-1.8.2����ѹ��E��
   ����ant����������
   ANT_HOME=E:\apache-ant-1.8.2        //���Ʊ�����ANT_HOME,����޸ģ����޸� /localchat/build.xml �ж�Ӧ��ant.home����
   CLASSPATH=%CLASSPATH%;%ANT_HOME%\lib;
   PATH=%PATH%;%ANT_HOME%\bin;

   ���ذ�װJMF2.1.1e,Ĭ�ϰ�װ
   ����JMF��������
   JMF_HOME=C:\Program Files\JMF2.1.1e   //���Ʊ�����JMF_HOME,����޸ģ����޸� /localchat/build.xml �ж�Ӧ��jmf.home����
   CLASSPATH=%CLASSPATH%;%JMF_HOME%\lib;
   PATH=%PATH%;%JMF_HOME%\bin;


2����ѹ�ļ�Ŀ¼�������ļ�Ŀ¼�ṹ
 
  LocalChat --
	     |--chatcommon
	     |--chating
	     |--chatserver
	     |--build.xml

>cd localchat.home                      //ʹ�������н���localchat��Ŀ¼
>ant                                    //ʹ��ant������б���

3������������

   ����localchat.home/chatserver/dist/bin/
   ˫��run.bat                          //Ĭ��������ʹ��javaw�������ӡ����̨��Ϣ;�����Ҫ�鿴����̨���༭run.bat�޸�javaw Ϊjava

4�������ͻ���
   ����localchat.home/chating/dist/bin
   �޸�chat.properties �ж�Ӧ��ѡ��ر��Ƿ�����ip��ַ
   ˫��run.bat   �����ͻ���             //Ĭ��������ʹ��javaw�������ӡ����̨��Ϣ;�����Ҫ�鿴����̨���༭run.bat�޸�javaw Ϊjava

5�������µ�build��dist��src���ֱ���  ����Ŀ¼������Ŀ¼��ԭ�ļ�Ŀ¼����ֻҪ�����ļ�����Ŀ¼��ʽ�������£�
	Chating--
		|--bin
		|--docs
		|--image
		|--lib
	���ͻ���������Ŀ��Ŀ¼���Ʊ�����Chating���ڲ���

6���������
>cd localchat.home                      //ʹ�������н���localchat��Ŀ¼
>ant clean                              //ʹ�� clean ����������

   