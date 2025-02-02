# JavaGUINative

- Создание нативного приложения на Java для запуска без JRE.
  
После моих изысканий было выснено что без JRE джаву нельзя собрать в полноценный exe файл(или можно).

А простое GUI(и не только) приложение будет занимать почти 250МБ+(можно сделать с помощью jpackage или [launch4j](https://launch4j.sourceforge.net/)

Я был огорчён, ведь после работы с Python, в котором exe шник это просто архив, который распаковывается в папку TEMP винды хотелось создать что то на более сложном ЯП.

Итак приступим.

Для сборки джавы в exe файл нам понадобится. [GraalVM](https://www.graalvm.org/downloads/#), надо распаковать его и папку bin прописать в Path(Windows, гугл в помощь)

Это кастомный JDK, в котором есть утилита native-image, с помощью которой можно собрать AOT приложение на Java.

Но он криво работает с Swing(AWT) и JavaFX, поэтому я советую JDK от [be//soft](https://bell-sw.com/pages/downloads/native-image-kit/#nik-23-(jdk-17)) при установки надо обязательно выставить все флаги чтобы JDK автоматически добавилось в Path.

Также на пк пользователя, который будет запускать ваше приложение обязательно должны быть [Visual C++ Redistributable Runtimes](https://www.techpowerup.com/download/visual-c-redistributable-runtime-package-all-in-one/), достаточно установить (vcredist2015_2017_2019_2022_x64.exe) почти у всех на пк он есть.

И для сборки нужны [Visual Studio Build Tools 2022](https://aka.ms/vs/17/release/vs_BuildTools.exe)

Теперь проверим что всё установилось правильно

``` 
java -version
```





 
