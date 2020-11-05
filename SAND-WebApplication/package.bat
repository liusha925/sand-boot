@echo off
:begin
echo =====================请选择您需要打包的项目=====================
echo 0 退出系统
echo 1 backend
echo 3 graphic
set/p a=请选择对应的数字:
if "%a%"=="1" goto :backend
if "%a%"=="3" goto :graphic
if "%a%"=="0" goto :end
echo\
echo 输入的值有误，请重新输入
echo\ 
goto :begin

#backend项目
:backend
::项目根目录
cd ../
::项目依赖模块
call mvn clean install -Dmaven.test.skip=true
::容器所在目录
cd SAND-WebApplication/backendApp
::休眠1s类似于Linux的sleep 1s
ping -n 1 127.0.0.1>nul
::打包成功之后打开target目录
call mvn clean install && explorer target
::回到项目根目录
cd ../
pause
goto :begin

#graphic项目
:graphic
::项目根目录
cd ../
::项目依赖模块
call mvn clean install -Dmaven.test.skip=true
::容器所在目录
cd SAND-WebApplication/graphicApp
::休眠1s类似于Linux的sleep 1s
ping -n 1 127.0.0.1>nul
::打包并成功之后打开target目录（cls是清除当前界面的打包信息）
call mvn clean install -Dmaven.test.skip=true && cls && explorer target 
::回到项目根目录
cd ../
pause
goto :begin
 
:end
exit
