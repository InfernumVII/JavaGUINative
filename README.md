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

Не забываем поставить галочку на разработке классических приложений на C++

![image0](https://github.com/user-attachments/assets/de4476e5-0e4c-4add-b0c2-e78cc48376db)

Теперь проверим что всё установилось правильно

``` 
java -version
```
![image1](https://github.com/user-attachments/assets/05f46b49-931b-4caf-a175-ba0e1cd195a9)

```
native-image --version
```
![image2](https://github.com/user-attachments/assets/f7b528ac-9cb3-4bf5-9208-473f6f3b8679)

Теперь приступим к сборке. Для Maven, Grandle [гугл в помощь](https://www.graalvm.org/latest/reference-manual/native-image/#build-a-native-executable-using-maven-or-gradle), я делал без них.

Берем простой код на swing и пробуем его собрать.

```java
// SwingTest.java
import javax.swing.*;

public class SwingTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Test");
            JButton button = new JButton("Нажми меня");

            button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Привет, Swing!"));

            frame.add(button);
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

```

Для начала пробуем его запустить
``` 
java SwingTest.java
```
![image3](https://github.com/user-attachments/assets/b332ea79-d77e-4545-9fbc-fa854378c218)

Если у вас такая же дичь на экране добавляем параметр ``` -Dfile.encoding=UTF-8 ``` 

```
java -Dfile.encoding=UTF-8 SwingTest.java
```
![image4](https://github.com/user-attachments/assets/dca6e7f3-610e-474a-9983-2ce614d9c460)

Как видите все прекрасно

Теперь приступаем к самому важному, а именно сборке

```
javac -encoding UTF-8 SwingTest.java
```
Собираем наш проект в [class](https://www.graalvm.org/latest/reference-manual/native-image/#from-a-class) или [jar](https://www.graalvm.org/latest/reference-manual/native-image/#from-a-jar-file)

И далее запускаем билд

Чтобы не было [проблем](https://stackoverflow.com/questions/76753136/graalvm-and-swing) с AWT добавляем параметр ``` -Djava.awt.headless=false ```

```
native-image -Djava.awt.headless=false SwingTest
```
После небольшого ожидания мы получаем наш exe-шник

![image5](https://github.com/user-attachments/assets/418fff09-81c2-4f76-b883-82a6dbc41f67)

По-хорошему бы внедрить все dll-ки в наш exe-шник, но я хз как это сделать

Итак если вас бесит открытие консоли при запуске вашего GUI приложения вы конечно можете найти параметр для native-image билда, но я его не нашёл, поэтому просто открываем 

![image6](https://github.com/user-attachments/assets/fe8804d5-0e57-47f8-94f4-7ee1cc36c8ee)

Переходим в директорию с нашим exe-шником и прописываем 

```
editbin /subsystem:windows swingtest.exe
```

Консоли не стало и это прекрасно

Переходим к JavaFX.

В кастомном JDK уже есть мне необходимые библиотеки, поэтому ничего устанавливать не надо

```
java --list-modules
```

![image7](https://github.com/user-attachments/assets/408d7e87-12d7-4981-b362-614b4fe96fd0)









 
