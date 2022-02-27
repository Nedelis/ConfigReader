# ConfigReader
Эта библиотека позволит работать вашей Java с файлами конфигурации типа `*.ncf`. Подробнее о данном формате можно посмотреть перейдя к соответствующему разделу (см. оглавление).
## Оглавление
0. [Описание библиотеки](#Описание-библиотеки)
1. [Работа с библиотекой](#Работа-с-библиотекой)
2. [Файлы конфигурации NCF](#Файлы-конфигурации-NCF)

### Описание библиотеки
Данная библиотека сейчас имеет реализацию только на языке Java. Внутри содержит парсер, анализирующий файл конфигурации, и переменную, в которую записываются все данные, полученные из файла конфигурации.

[Оглавление](#Оглавление) :arrow_up:
____

### Работа с библиотекой
Для начала работы с библиотекой выберите и скачайте `*.jar` архив (релиз) по [ссылке](https://github.com/Nedelis/ConfigReader/releases). А после подключите его к вашему проекту. Ниже вы сможете увидеть пример работы с библиотекой.

**Класс `Main`, читающий конфиг `some_config.ncf`**

```java

package com.examples.config_reader;

import com.github.nedelis.actions.FullRead;
import com.github.nedelis.util.data.ReadFiles;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        FullRead.read(new File("com/examples/some_config.ncf"));

        System.out.println(ReadFiles.getReadFiles());

    }

}
```
Вывод в консоль: `{test.ncf=Config[{NUMBERS={four=4, one=1, two=2, three=3}}]}`

**Файл `some_config.ncf`**
```
  modifications: DCP, DCT
  [NUMBERS]
  byte __one__ = (1)
  short __two__ = (2)
  int __three__ = (3)
  long __four__ = (4)
```

В этом примере можно увидеть, что у переменных внутри файла можно указать тип данных (все типы данных, имеющихся по умолчанию, перечислены в разделе о файлах конфигурации NFC). В библиотеке предусмотрено дополнение имеющихся типов данных своими при помощи вложенного класса `ModificationUnit` у класса `FieldTypes`. Пример использования `ModificationUnit` расположен ниже.

```java
package com.examples.custom_field_types;

import com.github.nedelis.vocabulary.keyword.FieldTypes;
import com.github.nedelis.vocabulary.keyword.FieldType;
import org.jetbrains.annotations.NotNull;

public class Main {

    public static void main(String[] args) {

        FieldTypes.ModificationUnit.addFieldType(new FieldType<ExampleFieldType>("example") {
            @Override
            public ExampleFieldType fromString(@NotNull String s) {
                return ExampleFieldType.parseExampleFieldType(s);
            }
        });

    }
}
```
Теперь в файлы конфигурации можно добавлять переменные с типом данных `example`.

Также вы можете регестрировать свои парсеры/транслейторы, имплементировав интерфейс `IParser`/`ITranslator`, а после зарегестрировав его через метод `registerParser`/`registerTranslator` в классе `ParserFactory`/`TranslatorFactory`. Пример регистрации парсера расположен ниже.

```java
package com.examples.parser_registration;

import com.github.nedelis.actions.parse.IParser;
import com.github.nedelis.actions.parse.ParserFactory;
import com.github.nedelis.actions.read.Page;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        //Регестрация парсера.
        ParserFactory.registerParser(new MyParser());
    }

    // Класс, реализующий интерфейс IParser.
    public static class MyParser implements IParser {

        private final ArrayList<String> PARSING_RESULT = new ArrayList<>();

        @Override
        public void parse(@NotNull Page page, @NotNull Set<String> set) {
            /* Ваша реализация этого метода. ) */
        }

        @Override
        public @NotNull ArrayList<String> getParsingResult() {
            return this.PARSING_RESULT;
        }

        @Override
        public @NotNull String parserModificationName() {
            return "MCP";
        }

        // Необязательно. (по умолчанию возвращает "ncf")
        @Override
        public @NotNull String fileExtension() {
            return "myncf";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyParser parser = (MyParser) o;
            return Objects.equals(this.parserModificationName(), parser.parserModificationName()) && Objects.equals(this.fileExtension(), parser.fileExtension());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.parserModificationName(), this.fileExtension());
        }
    }
}
```
Регестрация транслейтора происходит аналогичным способом. В будущем планируется регестрицаия через аннотированный метод.

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
Секции объединяют группу некоторых переменных под одно имя, что упрощает работу с ними. Название секции записывается в квадратных скобках. Пример: `[SOME_SECTION]`. В случае, если в файле нет секций, все переменные войдут в группу DEFAULT. Секция не может быть указана, если файл имеет модификацию NSS:
```
  modifications: NSS
  [NUMBERS]
  int __one__ = (1)
```
— выдаст ошибку.
____
#### 4. Модификации файла.
Модификации указываются на самой первой строке файла конфигурации, после ключевого слова `modifications` и двоеточия, перечисляются через запятую:
```
modifications: DCP, DCT, NSS, NFT (и т.д.)
```
Существует несколько модификаций.
* `DCP` — сокращение от `default config parsing`. Указывает, что в данном файле NFC используется парсер по умолчанию.
* `DCT` — сокращение от `default config translator`. Указывает, что в данном фале NCF используется транслейтор по умолчанию.
* `NFT` — сокращение от `no field types`. Указывает, что в файле у переменных нет типов данных. (в таком случае библиотека записывает все значения, как строку)
* `NSS` — сокращение от `no section separation`. Указывает, что в файле нет секций. (в таком случае все переменные записываются в группу DEFAULT)

Обратите внимание, что при указании в одном файле нескольких модификационных имён разных парсеров/транслейтеров, будет выбран тот, чья модификация идёт первой. Добавлять свои модификации можно только регестрируя новый парсер/транслейтор.
____

#### 5. Расширения файлов.
Вместо того чтобы указывать парсеру, как читать файл в модификации, можно изменить расширение файла:
* Файлы `*.ncf` используется парсер и транслейтор по умолчанию.
* Файлы с другими расширениями должны быть прописаны при регистрации парсера/транслейтора.
При этом модификации, изменяющие тип разделения файла, будут игнорироваться. Добавлять свои расширения можно только при регистрации нового парсера/транслейтора.

____
[Оглавление](#Оглавление) :arrow_up:
____
