Перевел интерпретатор модельного паскалеподобного языка программирования (https://github.com/kharo1963/ConsoleAppIML) с С++ на ava Spring Boot (IntelliJ IDEA ) 
Добавил функцию get для получение значений из таблицы iml_param (PostgreSQL)
Описание таблицы:
CREATE TABLE IF NOT EXISTS iml_param
(
    id         BIGSERIAL PRIMARY KEY ,
    prog_name  VARCHAR(16) NOT NULL  ,
    param_name VARCHAR(8)  NOT NULL  ,
    param_val  integer
);
Пример использования в программе ext-gcd.txt:
get (m,ext-gcd/m) присваивает переменной m значение param_val из таблицы iml_param, где prog_name = ext-gcd и param_name = m
