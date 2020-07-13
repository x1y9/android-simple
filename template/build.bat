@setlocal enableDelayedExpansion
@REM set LANG for grep run correctly 
@set LANG=zh_CN.UTF-8

@if  "%1"=="" goto do_help
@if  "%1"=="picture" goto do_picture
@if  "%1"=="clean" goto do_clean
@if  "%1"=="version" goto do_version


@set FLAVOR=%2
@echo check flavor:%2
@if "%2"=="" set FLAVOR=std

@if  "%1"=="debug" goto do_debug
@if  "%1"=="release" goto do_release
@if  "%1"=="publish" goto do_publish


:do_help
@echo build script for android project
@echo.
@echo Build:   build [debug^|release^|publish] [flavor]
@echo Clean:   build clean 
@echo Version: build version [number]
@goto end


:do_debug
call gradlew assemble%FLAVOR%Debug
@IF "%ERRORLEVEL%"=="0" adb install -r app\build\outputs\apk\%FLAVOR%\debug\app-%FLAVOR%-debug.apk
@goto end

:do_release
call gradlew assemble%FLAVOR%Release
@IF "%ERRORLEVEL%"=="0" adb install -r app\build\outputs\apk\%FLAVOR%\release\app-%FLAVOR%-release.apk
@goto end

:do_publish
@echo check uncommit files...
@git diff-files --quiet
@IF %ERRORLEVEL% NEQ 0 goto error_end
call gradlew clean assemble%FLAVOR%Release
@IF %ERRORLEVEL% NEQ 0 goto error_end
for /f %%i in ('grep -m 1 -oP "applicationId\s+.\K([a-zA-Z0-9.]+)" app\build.gradle') do set PACKAGE=%%i
for /f %%i in ('grep -oP "versionName\s+.\K([0-9.]+)" app\build.gradle') do set VERSION=%%i
for /f %%i in ('git rev-parse --short HEAD') do set HASH=%%i
@echo publish to %PACKAGE%-%VERSION%-%HASH%-%FLAVOR%.apk
if "%FLAVOR%"=="" (
copy app\build\outputs\apk\app-release.apk %PACKAGE%-%VERSION%-%HASH%.apk
) else (
copy app\build\outputs\apk\%FLAVOR%\release\app-%FLAVOR%-release.apk %PACKAGE%-%VERSION%-%HASH%-%FLAVOR%.apk
)
@goto end

:do_picture
for /f "delims=" %%i in ('grep -oP """app_name"">\K([a-zA-Z 0-9.]+)" app\src\main\res\values\strings.xml') do set APPNAME=%%i
convert app\src\main\res\drawable-xhdpi\icon.png -resize 512x publish\icon512.png
convert app\src\main\res\drawable-xhdpi\icon.png -resize 114x publish\icon114.png
convert -size 1024x500  radial-gradient:#8c8ca4-#232050 ~bg.png
convert publish\icon114.png -background none -pointsize 36 -size 480x -fill "#e7e7e7" -gravity center caption:"%APPNAME%" -append ~slogan.png
composite -gravity center ~slogan.png ~bg.png publish\feature.jpg
del ~bg.png ~slogan.png
@goto end

:do_clean
gradlew clean
@goto end

:do_version
@if "%2"=="" (
@grep -oP "versionName ""\K([0-9.]+)" app\build.gradle
) else (
sed -i -E "s/versionName ""(.*?)""/versionName ""%2""/" app\build.gradle
sed -i -E "s/versionCode ([0-9.]+)/versionCode %2/" app\build.gradle
sed -i -E "/versionCode/s/\.([0-9][0-9])/\1/g" app\build.gradle
sed -i -E "/versionCode/s/\./0/g" app\build.gradle
)
@goto end

:error_end
@echo Oops... Something wrong!
@ver /ERROR >NUL 2>&1

:end