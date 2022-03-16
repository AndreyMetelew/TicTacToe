package com.TicTacToe;

class GameMap { // Класс GameMap, отвечающий за работу с игровой картой

    private final Cell[][] map; // Создается локальная финальная переменная двухмерного массива без инициализации памяти под него

    protected GameMap (int x, int y) { // Наследуемый конструктор класса, позволяющий рисовать карту
        map = new Cell[y][x]; // Инициализация массива на основе введенных значений
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                map[i][j]=new Cell(j,i); // Цикловое заполнение значений из созданного массива
            }
        }
    }

    protected boolean isCellValid(int x, int y) { // Геттер, возвращающий результат логической сверки, на основе введенных x и y
        return (0 <= x && x < map[0].length) || (0 <= y && y < map.length);
        // Сверка: промахнулись ли мы в выборе позиции на игровой карте и вышли ли за нее
    }
    protected boolean isCellValid(Cell cell) { // Геттер, возвращающий результат логической сверки объекта cell класса Cell
        return (0 <= cell.columnNumber && cell.columnNumber < map[0].length)
                && (0 <= cell.rowNumber && cell.rowNumber < map.length);
        /* Сверка: (Если 0 меньше или равен columnNumber объекта cell И columnNumber объекта cell меньше размера массива с нулевым индексом) И
        (Если 0 меньше или равен rowNumber объекта cell И rowNumber объекта cell меньше размера массива) */
    }

    protected int getCountRow() { // Геттер, возвращающий количество рядов
        return map.length;
    }

    protected int getCountColumn() { // Геттер, возвращающий количество столбцов
        return map[1].length;
    }

    protected Cell getCell(int x, int y) { // Геттер, возвращающий значение матрицы объекта класса Cell
        return map[y][x];
    }

    protected Cell getCell(Cell cell) { // Геттер, возвращающий значение матрицы объекта класса Cell на основе его индекса колонны и столбца
        return map[cell.rowNumber][cell.columnNumber];
    }

    protected Cell[] getRow(int y) { // Геттер, возвращающий массив из объекта класса Cell, обозначающий строчку
        Cell[] row = new Cell[map[0].length]; // Создание массива класса Cell и задание его параметра на основе длины массива с индексом 0
        System.arraycopy(map[y], 0, row, 0, row.length); // Копирование массива, его параметров и его длины
        return row; // Возвращение значения этого массива
    }
    protected Cell[] getRow(Cell cell) { // Геттер, возвращающий массив из объекта класса Cell, обозначающий строчку
        Cell[] row = new Cell[map[0].length]; // Создание массива класса Cell и задание его параметра на основе длины массива с индексом 0
        System.arraycopy(map[cell.rowNumber], 0, row, 0, row.length); // Копирование массива, его параметров и его длины
        return row; // Возвращение значения этого массива
    }

    protected Cell[] getColumn(int x) { // Геттер, возвращающий массив из объекта класса Cell, обозначающий столбец
        Cell[] column = new Cell[map.length]; // Создание массива класса Cell и задание его параметра на основе длины массива
        for (int i = 0; i < column.length; i++) { // Цикловое присваивание
            column[i]=map[i][x]; // Новому массиву присваиваются значения созданного массива map
        }
        return column; // Возвращение значения массива
    }
    protected Cell[] getColumn(Cell cell) { // Геттер, возвращающий массив из объекта класса Cell, обозначающий столбец
        Cell[] column=new Cell[map.length]; // Создание массива класса Cell и задание его параметра на основе длины массива
        for (int i = 0; i < column.length; i++) { // Цикловое присваивание
            column[i]=map[i][cell.columnNumber]; // Новому массиву присваиваются значения созданного массива map
        }
        return column; // Возвращение значения массива
    }

    protected Cell[] getD1(Cell cell) { // Геттер, возвращающий массив из объекта d1 класса Cell,
        int b = cell.rowNumber-cell.columnNumber; // Создаем временную переменную b и инициируем ее разностью значения строки и столбца внутри объекта класса Cell
        Cell[] d1 = new Cell[map.length]; // Создаем массив d1, размер которого равен переданному значению длины массива map
        /* Цикл с созданной на время массива переменной i, значение которого выбирается:
        Если b больше или равен 0, мы присваиваем ему b, иначе 0.
        Условный оператор ограничивает цикл значением, равным разности длины d1 и выбираемому значению:
        Если b больше или равен 0, мы присваиваем 0, иначе - -b. Цикл каждый раз увеличивает i на 1 */
        for (int i = (b >= 0 ? b : 0); i < d1.length - (b >= 0 ? 0 : -b); i++) {
            d1[i] = map[i][i-b]; // Значению массива d1 с индексом i присваиваем значение двухмерного массива map с индексом i и i-b
        }
        return d1; // Возвращаем массив d1
    }

    protected Cell[] getD2(Cell cell) { // Геттер, возвращающий массив из объекта d2 класса Cell,
        int b = cell.rowNumber+cell.columnNumber; // Создаем временную переменную b и инициируем ее суммой значения строки и столбца внутри объекта класса Cell
        Cell[] d2 = new Cell[map.length]; // Создаем массив d2, размер которого равен переданному значению длины массива map
        /* Цикл с созданной на время массива переменной i, значение которого выбирается:
        Если b меньше длины массива map, мы присваиваем ему 0, иначе b - (длина массива map - 1).
        Условный оператор ограничивает цикл значением, равным разности длины d2 и выбираемому значению:
        Если b меньше длины массива map, мы присваиваем ему разность (длины массива map - 1) - b, иначе 0.
        Цикл каждый раз увеличивает i на 1 */
        for (int i = (b < map.length ? 0 : b - (map.length - 1));
             i < d2.length - (b < map.length ? (map.length - 1) - b : 0); i++) {
            d2[i]= (map[i][b-i]); // Значение массива d2 под индексом i получает значение массива map под индексом i и b-i
        }
        return d2; // Возвращаем массив d2
    }

    protected boolean isD1(Cell cell, int lengthD) { // Логический геттер, возвращающий значение на основе проверки в объекте cell и длины lengthD
        return (cell.rowNumber-(map.length-lengthD) <=cell.columnNumber // Проверка: значение rowNumber объекта cell минус (разность длины map минус больше или равно columnNumber объекта cell...
                && cell.columnNumber<=map[0].length-lengthD+cell.rowNumber); // ... И columnNumber объекта cell меньше или равен разнице длины map с индексом 0 и lengthD, плюс rowNumber объекта cell?
    }

    protected boolean isD2(Cell cell, int lengthD) { // Логический геттер, возвращающий значение на основе проверки в объекте cell и длины lengthD
        return (lengthD-1-cell.rowNumber <=cell.columnNumber // Проверка: значение lengthD минус 1 минус rowNumber объекта cell меньше или равен columnNumber объекта cell...
                && cell.columnNumber<=map[0].length-1-cell.rowNumber+(map.length-lengthD)); // ... И columnNumber объекта cell меньше или равен
    }

    private void printHorizontalLine() { // Метод черчения горизонтальной линии
        for (int i = 0; i <= map[1].length; i++) { // Цикл от 0 до длины массива с индексом один
            System.out.print("----"); // Черточки
        }
        System.out.println(); // Отступ
    }

    protected void printMap() { // Метод печати игровой карты крестиков-ноликов
        System.out.print(" | "); // Начинаем "рисовать"
        for (int i = 1; i <= map[1].length; i++) { // Печатаем до значения длины массива map с индексом 1
            System.out.print(i + " | ");
        }
        System.out.println(); // Разделение
        printHorizontalLine(); // С помощью метода рисуем горизонтальную линию
        for (int i = 0; i < map.length; i++) { // Цикл до длины цикла map
            System.out.print((i + 1) + " | "); // Печатаем i + 1 и черту
            for (int j = 0; j < map[1].length; j++) { // Внутренний цикл до длины массива map с индексом 1
                System.out.print((map[i][j].getDot() == null ? " " : map[i][j].getDot())+ " | "); // Если ссылки на объект нет, мы пишем пробел, иначе значение плюс черту
            }
            System.out.println(); // Отступ
            printHorizontalLine(); // Линия
        }
    }

}