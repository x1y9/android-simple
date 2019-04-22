@setlocal enableDelayedExpansion
@if  "%1"=="" goto do_help

@if  "%1"=="debug" goto do_debug
@if  "%1"=="release" goto do_release
@if  "%1"=="publish" goto do_publish
@if  "%1"=="picture" goto do_picture
@if  "%1"=="clean" goto do_clean


:do_help
@echo build script for android project
@echo.
@echo Build: build [debug^|release^|publish] 
@echo Clean: build clean 
@goto end


:do_debug
call gradlew assembleDebug
@goto end

:do_release
call gradlew assembleRelease
@goto end

:do_publish
@echo check uncommit files...
@git diff-files --quiet
@IF %ERRORLEVEL% NEQ 0 goto error_end
call gradlew assembleRelease
@IF %ERRORLEVEL% NEQ 0 goto error_end
for /f %%i in ('c:\PROGRA~1\git\usr\bin\grep -oP "versionName\s+.\K([0-9.]+)" app\build.gradle') do set VERSION=%%i
for /f %%i in ('git rev-parse --short HEAD') do set HASH=%%i
@echo publish to me.i38.liquid-%VERSION%-%HASH%.apk
copy app\build\outputs\apk\app-release.apk me.i38.liquid-%VERSION%-%HASH%.apk
@goto end

:do_picture
convert app\src\main\res\drawable-xhdpi\icon.png -resize 512x publish\icon512.png
convert app\src\main\res\drawable-xhdpi\icon.png -resize 114x publish\icon114.png
convert -size 1024x500  radial-gradient:#8c8ca4-#232050 ~bg.png
convert publish\icon114.png -background none -pointsize 36 -size 480x -fill "#e7e7e7" -gravity center caption:"Liquid Screen" -append ~slogan.png
composite -gravity center ~slogan.png ~bg.png publish\feature.jpg
del ~bg.png ~slogan.png
@goto end

:do_clean
gradlew clean
@goto end


:error_end
@echo Oops... Something wrong!
@ver /ERROR >NUL 2>&1

:end