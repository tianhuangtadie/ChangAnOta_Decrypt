@echo off

:start
CLS
title payload-dumper-go -by Magisk������
color 2
echo.
echo.           Magisk��������magiskcn.com
echo.
echo.---------------------------------------------------
echo.  ʹ��˵����
echo.
echo.  ��ȡ ȫ��.img ������ a      ����ȡʱ�������
echo.  ��ȡ boot.img ������ b      ����ȡʱ����̡�
echo.  ��ȡ init_boot.img ������ i ����ȡʱ����̡�
echo.
echo.  �ο��̳̣�https://magiskcn.com/payload-dumper-go-boot
echo.---------------------------------------------------
%�ж�img�ļ����Ƿ����%
if exist .\img rd .\img /s/q

%�ж�payload�Ƿ����%
if exist .\payload.bin (goto input) else (goto nopayload)
pause>nul
exit

:nopayload
CLS
echo.
echo.  payload.bin�����ڣ��븴���ļ�����ǰ�ļ��У���������˳���
pause>nul
explorer "https://magiskcn.com/payload-dumper-go-boot?ref=payload-dumper-go"
exit

:input
set /p input=�����룺
%�������Ϊ��%
if "%input%"=="" goto error
%�ж��û�����%
if /i %input% equ a goto all
if /i %input% equ b goto boot
if /i %input% equ i goto init_boot
goto error

:error
CLS
echo ��������ȷָ��
pause
goto start


:all
%��ȡ����.img%
CLS
.\payload-dumper-go.exe -o .\img .\payload.bin 
goto end

:boot
%��ȡboot.img%
CLS
.\payload-dumper-go.exe -p boot -o .\img .\payload.bin
goto end

:init_boot
%��ȡinit_boot.img%
CLS
.\payload-dumper-go.exe -p init_boot -o .\img .\payload.bin
goto end

:end
CLS
echo.
echo.  ��ȡ�ɹ������ img �ļ��в鿴����������򿪣�
pause>nul
explorer .\img
exit