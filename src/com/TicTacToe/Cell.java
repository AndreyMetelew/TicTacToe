package com.TicTacToe;

class Cell { // Класс Cell, отвечающий за работу с ячейками в игре

    protected final int columnNumber; // Здесь хранится номер столбца
    protected final int rowNumber; // Здесь хранится номер строки
    private Dots dot; //

    protected Cell (int x, int y) { // Конструктор класса - инициализирует значения columnNumber, rowNumber
        columnNumber = x;
        rowNumber = y;
    }

    protected Cell (int x, int y, Dots dot) { // Конструктор класса - инициализирует значения columnNumber, rowNumber, dot класса Dots
        columnNumber = x;
        rowNumber = y;
        this.dot = dot;
    }

    protected Cell (Cell that) { // Конструктора класса - подхватывает значения из другой ячейки
        this.columnNumber = that.columnNumber;
        this.rowNumber = that.rowNumber;
        this.dot = that.dot;
    }

    protected void setDot(Dots dot) { // Сеттер, подхватывающий ссылку на объект класса Dots
        this.dot = dot;
    }

    protected Dots getDot() { // Геттер, возвращающий значение в виде ссылки на объект класса Dots
        return dot;
    }

    protected boolean isEmptyCell() { // Логический Геттер, возвращающий значение Null, равное проверке, равно ли значение объекта класса Dots null
        return dot == null;
    }

    @Override
    public boolean equals(Object obj) { // "Перегруженный" (перезаписанный) логический метод equals, возвращающий логическое значение
        if (this == obj) return true; // Если этот объект совпадает с запрошенным методом - возвращает true
        if (obj == null || getClass() != obj.getClass()) return false; // Если объект равен null или при проверке объекта класса он не совпадает с введенным - возвращается false
        Cell that = (Cell) obj; // Этому объекту присваивается ссылка от объекта, введенного в метод
        if (this.columnNumber != that.columnNumber) return false; // Если сравниваемые значения номера столбцов не совпадают - возвращается false
        if (this.rowNumber != that.rowNumber) return false; // Если сравниваемые значения номера строк не совпадают - возвращается false
        return this.dot == that.dot; // После всех проверок возвращается логический результат сравнения совпадения ссылок на переменную dot двух объектов
    }
}