# Truck Loader

Программа для упаковки посылок в грузовик 6x6.

## Команды

### Работа с посылками
- `find-all` — показать все
- `find "Имя"` — найти
- `create -name "Имя" -form "XXX\nXXX\nXXX"` — создать
- `delete "Имя"` — удалить
- `load -parcels-text "Имя1\nИмя2" -type maxdense -out text` — упаковать

### Загрузка из файла
- `java ru.hofftech.Main test.txt maxdense 3`
- `java ru.hofftech.Main test.txt maxdense 3 --save result.json`

### Загрузка из JSON
- `java ru.hofftech.Main --load result.json`

## Telegram бот
Команды те же: /find-all, /find, /create, /delete, /load