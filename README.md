# Tv Show App
## Table of Contents
  - [Introduction](#introduction)
  - [Start with](#start-with)
  - [Profile a Kotlin application](#profile-a-kotlin-application)
## Introduction
This is a sample application consuming data from the [tvshow api](https://api.tvmaze.com/).
It's created with MVC architecture and with the followiing features:
- List TV Show Schedule for the current day
- Search functionality on TV Schedule consuming a search endpoint
- Details view with landscape and portrait layouts
- WebView integration to visualize the TV Show's webpage
-
## Start with
```sh
$ git clone https://github.com/hacknugithub/tvshow-schedule-app.git
$ cd tvshow-schedule-app/
```
Launch your Android Studio or check the test apk from [here](https://drive.google.com/file/d/1VYdvkL03SkNplE8SGgvRpwrB0YlBaep9/view?usp=sharing)

The testing environment:
```
Android Studio 4.1
test device: Android O (Google Pixel 2)
test device: Android P (Motorola G9 Plus)
Please make sure your device having Android version >= O.
```
## Profile a Kotlin application
Android Studio project: SimpleExampleOfKotlin
steps:
1. Build and install the application:
```sh
# Open SimpleperfExampleOfKotlin project with Android Studio,
# and build this project sucessfully, otherwise the `./gradlew` command below will fail.
$ cd SimpleperfExampleOfKotlin
# On windows, use "gradlew" instead.
$ ./gradlew clean assemble
$ adb install -r app/build/outputs/apk/profiling/app-profiling.apk
```
2. Record profiling data:
```sh
$ cd ../../scripts/
# app_profiler.py collects profiling data in perf.data, and binaries on device in binary_cache/.
$ python app_profiler.py -p com.example.simpleperf.simpleperfexampleofkotlin
```
3. Show profiling data:
```sh
# report_html.py generates profiling result in report.html.
$ python report_html.py --add_source_code --source_dirs ../demo --add_disassembly
```