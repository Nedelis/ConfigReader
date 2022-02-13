# ConfigReader
Эта библиотека позволит работать вашей Java с файлами конфигурации типа `*.ncf`. Подробнее о данном формате можно посмотреть перейдя к соответствующему разделу (см. оглавление).
## Оглавление
0. [Описание библиотеки](#Описание-библиотеки)
1. [Работа с библиотекой](#Работа-с-библиотекой)
2. [Файлы конфигурации NCF](#Файлы-конфигурации-NCF)

### Описание библиотеки
Данная библиотека сейчас имеет реализацию только на языке Java. Внутри содержит парсер, который анализирует файл конфигурации, и переменную, в которую записываются все данные, полученные из файла конфигурации.

[Оглавление](#Оглавление) :arrow_up:
____

### Работа с библиотекой
Для начала работы с библиотекой скачайте `*.jar` архив по [ссылке](https://youtu.be/DLzxrzFCyOs). А после подключите его к вашему проекту. Ниже вы сможете увидеть пример работы с библиотекой.

**Класс `Main`, читающий конфиг `some_config.ncf`**
```java

  package com.examples.config_reader;
  
  import com.github.nedelis.actions.parse.Parser;
  import com.github.nedelis.actions.read.Reader;
  
  import java.io.File;
  import java.util.Map;
  import java.util.HashMap;
  
  public class Main {
  
    public static final Map<String, Map<String, Map<String, Object>>> configuration = new HashMap<>();
    
    public static void main(String[] args) {
      
      Parser.parse(Reader.readConfig(new File("com/examples/some_config.ncf")));
      configuration.put("some_config", Parser.getData());
      
      System.out.println(configuration);
      
    }
    
  }
```
Вывод в консоль: `{some_config={NUMBERS={four=4, one=1, two=2, three=3}}}`

**Файл `some_config.ncf`**
```
  modifications: FLP
  [NUMBERS]
  byte __one__ = (1)
  short __two__ = (2)
  int __three__ = (3)
  long __four__ = (4)
```

В этом примере можно увидеть, что у переменных внутри файла можно указать тип данных (все типы данных, имеющихся по умолчанию, перечислены в разделе о файлах конфигурации NFC). В библиотеке предусмотрено дополнение имеющихся типов данных своими при помощи вложенного класса `ModificationUnit` у класса `FieldTypes`. Пример использования `ModificationUnit` расположен ниже.

```java
  public static void main(String[] args) {
  
    FieldTypes.ModificationUnit.addFieldType(new FieldType(ExampleFieldType.class, "example"));
  
  }
```
Теперь в файлы конфигурации можно добавлять переменные с типом данных `example`.

[Оглавление](#Оглавление) :arrow_up:
____

### Файлы конфигурации NCF
Файлы конфигурации NFC это файлы, имеющие специальный синтаксис, отличный от файлов конфигурации `*.cfg` и похожий на синтаксис файлов `*.ini`. Теперь от абстрактного описания перейдём на конкретные примеры.    
#### 1. Типы данных.
По умолчанию есть всего 9 типов данных, позаимствованных у Java, это:    
* byte
* short
* int
* long
* float
* double
* bool (boolean)
* char
* str (String)    
Также вы можете добавлять свои типы данных. Как это сделать написано в разделе про работу с библиотекой. Если тип данных не указан, то значение переменной станет строкой.
____
#### 2. Имена переменных.
Имена переменных записываются между двумя нижними подчёркиваниями. Пример: `__some_var__`.
____
#### 3. Секции.
Секции объединяют группу некоторых переменных под одно имя, что упрощает работу с ними. Название секции записывается в квадратных скобках. Пример: `[SOME_SECTION]`. В случае, если в файле нет секций, все переменные войдут в группу DEFAULT. Секция не может быть указана после объявления некоторого числа переменных:
```
  int __some_int__ = (0)
  [NAMES]
  __bobby__ = (bobby)
```
— выдаст ошибку.
____
#### 4. Модификации файла.
Модификации указываются на самой первой строке файла конфигурации, после ключевого слова `modifications` и двоеточия, перечисляются через запятую:
```
modifications: FLP, NSS, NFT (и т.д.)
```
Существует несколько модификаций.
* `FLP` — сокращение от `fast line parsing`. Указывает, что в данном файле NFC разделение идёт построчно. (активно по умолчанию)
* `VWP` — сокращение от `very fast word parsing`. Указывает, что в данном файле NFC разделение идёт пословно. (в данный момент такой функции нет)
* `SCP` — сокращение от `safe char parsing`. Указывает, что в данном файле NFC разделение идёт посимвольно. (есть свои нюансы, такие как спец. символы)
* `NFT` — сокращение от `no field types`. Указывает, что в файле у переменных нет типов данных. (в таком случае библиотека записывает все значения, как строку)
* `NSS` — сокращение от `no section separation`. Указывает, что в файле нет секций. (в таком случае все переменные записываются в группу DEFAULT)

Обратите внимание, что при указании в одном файле таких модификаций, как `FLP`, `VWP` и `SCP`, файл станет автоматически разделяться построчно.
____

#### 5. Расширения файлов.
Вместо того, чтобы указывать парсеру, как читать файл в модификации, можно изменить расширение файла:
* Файлы `*.ncfl` будут разделяться построчно.
* Файлы `*.ncfw` будут разделяться пословно.
* Файлы `*.ncfc` будут разделяться посимвольно.
При этом модификации, изменяющие тип разделения файла, будут игнорироваться.

____
[Оглавление](#Оглавление) :arrow_up:
____
