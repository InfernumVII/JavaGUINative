# JavaGUINative: Создание нативного Java-приложения без JRE
  
## 🔥 Введение

После экспериментов выяснилось, что Java-приложение сложно собрать в полноценный `.exe` без JRE. Однако, даже простое GUI-приложение может занимать 250MB+ (например, при использовании `jpackage` или [Launch4j](https://launch4j.sourceforge.net/)).

После работы с Python, где `.exe` — это просто архив, распаковываемый в `TEMP`, захотелось создать нечто подобное, но на Java.

## 🚀 Подготовка к сборке

Для сборки `.exe` нам понадобится:

### 1️⃣ Установка GraalVM
🔗 [Скачать GraalVM](https://www.graalvm.org/downloads/#) и распаковать его. Затем добавить папку `bin` в `Path` (Windows, гугл в помощь).

**GraalVM** — это кастомный JDK с утилитой `native-image`, позволяющей собирать AOT-приложения на Java.

💡 **Важно!** GraalVM плохо работает с `Swing(AWT)` и `JavaFX`. Рекомендуется использовать JDK от [BellSoft](https://bell-sw.com/pages/downloads/native-image-kit/#nik-23-(jdk-17)). При установке не забудьте включить флаг автоматического добавления в `Path`.

### 2️⃣ Установка зависимостей
✅ [Visual C++ Redistributable Runtimes](https://www.techpowerup.com/download/visual-c-redistributable-runtime-package-all-in-one/) (установить `vcredist2015_2017_2019_2022_x64.exe` — он уже есть у большинства пользователей)
✅ [Visual Studio Build Tools 2022](https://aka.ms/vs/17/release/vs_BuildTools.exe) (при установке включить поддержку **разработки классических приложений на C++**)
![image0](https://github.com/user-attachments/assets/de4476e5-0e4c-4add-b0c2-e78cc48376db)

## 🛠 Проверка установки
```sh
java -version
```
![image1](https://github.com/user-attachments/assets/05f46b49-931b-4caf-a175-ba0e1cd195a9)
```sh
native-image --version
```
![image2](https://github.com/user-attachments/assets/f7b528ac-9cb3-4bf5-9208-473f6f3b8679)

## 🎨 Сборка Java Swing-приложения

Теперь приступим к сборке. Для Maven, Grandle [гугл в помощь](https://www.graalvm.org/latest/reference-manual/native-image/#build-a-native-executable-using-maven-or-gradle), я делал без них.

Берем простой код на swing и пробуем его собрать.

### 1️⃣ Исходный код `SwingTest.java`

```java
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

### 2️⃣ Запуск и тестирование

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

### 3️⃣ Компиляция и сборка `exe`

```
javac -encoding UTF-8 SwingTest.java
```
Собираем наш проект в [class](https://www.graalvm.org/latest/reference-manual/native-image/#from-a-class) или [jar](https://www.graalvm.org/latest/reference-manual/native-image/#from-a-jar-file)

И далее запускаем билд

Чтобы не было [проблем](https://stackoverflow.com/questions/76753136/graalvm-and-swing) с AWT добавляем параметр ``` -Djava.awt.headless=false ```

```
native-image -Djava.awt.headless=false SwingTest
```
После небольшого ожидания получаем `.exe`!

![image5](https://github.com/user-attachments/assets/418fff09-81c2-4f76-b883-82a6dbc41f67)

По-хорошему бы внедрить все dll-ки в наш exe-шник, но я хз как это сделать

💡 **Фикс открытия консоли при запуске GUI**:

Итак если вас бесит открытие консоли при запуске вашего GUI приложения вы конечно можете найти параметр для native-image билда, но я его не нашёл, поэтому просто открываем 

![image6](https://github.com/user-attachments/assets/fe8804d5-0e57-47f8-94f4-7ee1cc36c8ee)

Переходим в директорию с нашим `.exe` и прописываем 

```
editbin /subsystem:windows swingtest.exe
```

Консоли не стало и это прекрасно

## ⚡ Сборка JavaFX-приложения

В кастомном JDK уже есть мне необходимые библиотеки, поэтому ничего устанавливать не надо

```
java --list-modules
```

![image7](https://github.com/user-attachments/assets/408d7e87-12d7-4981-b362-614b4fe96fd0)

Пишем простенький код

### 1️⃣ Исходный код `JavaFXTest.java`

```java
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JavaFXTest extends Application {
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Нажми меня");
        btn.setOnAction(event -> System.out.println("Кнопка нажата!"));
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Тест JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}


```

### 2️⃣ Компиляция и сборка
```
javac -encoding UTF-8 JavaFXTest.java
```
```
native-image JavaFXTest
```
## ❗ Outro

Произошла магия и оно работает, но к сожалению только не на вашем пк

Я не знаю в чем проблема, но когда скидываешь данный `.exe` на любой другой пк, с другой архитектурой(или с такой-же) процессора - оно работает

Возможно когда-нибудь найду фикс данной проблемы

Вы всегда можете проверить свой `.exe` на другой машине с помощью [terminator aeza](https://terminator.aeza.net/), не забудьте установить [Visual C++ Redistributable Runtimes](https://www.techpowerup.com/download/visual-c-redistributable-runtime-package-all-in-one/)

## 🎯 Итог
Я достаточно намучился и пожалуй все же пойду пробовать писать на C++ свое приложение, а вам советую остановить данный гайд в самом начале и просто подкидывать JRE, JavaFX SDK вместе с вашим файлом exe, сделанным в [launch4j](https://launch4j.sourceforge.net/)










 
