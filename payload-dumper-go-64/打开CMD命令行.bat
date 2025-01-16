@echo off

:start
CLS
title payload-dumper-go -by Magisk中文网
color 2
echo.
echo.           Magisk中文网：magiskcn.com
echo.
echo.---------------------------------------------------
echo.  使用说明：
echo.
echo.  提取 全部.img ，输入 a      【提取时间更长】
echo.  提取 boot.img ，输入 b      【提取时间更短】
echo.  提取 init_boot.img ，输入 i 【提取时间更短】
echo.
echo.  参考教程：https://magiskcn.com/payload-dumper-go-boot
echo.---------------------------------------------------
%判断img文件夹是否存在%
if exist .\img rd .\img /s/q

%判断payload是否存在%
if exist .\payload.bin (goto input) else (goto nopayload)
pause>nul
exit

:nopayload
CLS
echo.
echo.  payload.bin不存在，请复制文件到当前文件夹（按任意键退出）
pause>nul
explorer "https://magiskcn.com/payload-dumper-go-boot?ref=payload-dumper-go"
exit

:input
set /p input=请输入：
%如果输入为空%
if "%input%"=="" goto error
%判断用户输入%
if /i %input% equ a goto all
if /i %input% equ b goto boot
if /i %input% equ i goto init_boot
goto error

:error
CLS
echo 请输入正确指令
pause
goto start


:all
%提取所有.img%
CLS
.\payload-dumper-go.exe -o .\img .\payload.bin 
goto end

:boot
%提取boot.img%
CLS
.\payload-dumper-go.exe -p boot -o .\img .\payload.bin
goto end

:init_boot
%提取init_boot.img%
CLS
.\payload-dumper-go.exe -p init_boot -o .\img .\payload.bin
goto end

:end
CLS
echo.
echo.  提取成功，请打开 img 文件夹查看（按任意键打开）
pause>nul
explorer .\img
exit