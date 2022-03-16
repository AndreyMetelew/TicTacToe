package com.TicTacToe;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

enum Dots { X, O } // Заданы значения переменной Dots типа enum

public class TicTacToe { // Класс самой игры

    private static int dotsToWin = 3; // Максимальное кол-во символов до победы
    private static int mode; // Режим игры: если 0, то с человеком, если 1, то с ИИ
    private static String gamer1Name; // Заносим имя первого игрока
    private static String gamer2Name; // Заносим имя второго игрока
    private static int whoseMove; // Определяет, кто ходит сейчас
    private static GameMap map; // Создаем данные по карте
    private static int countStep; // Количество шагов
    private static Cell gamer1Cell; // Хранилище символов для первого игрока
    private static Cell gamer2Cell; // Хранилище символов для второго игрока
    private static int gamer1Score = 0; // Счет первого игрока
    private static int gamer2Score = 0; // Счет второго игрока
    private static int drawScore = 0; // Счет ходов
    private static final Scanner sc = new Scanner(System.in); // Сканер ввода
    private static final Random rand = new Random(); //

    private static void playRound() { // Метод игры
        countStep = 0; // Кол-во шагов инициализированы
        map.printMap(); // Метод черчения игровой зоны
        while (true) { // Бесконечный цикл
            whoseMove = 1; // Ход игрока
            humanTurn(); // Шаг игрока
            if (countStep == -1) break; // Если значение шага - -1, прерываем игру
            countStep++; // Повышаем счетчик шагов на 1
            map.printMap(); // Напечатать карту
            if (isWin(gamer1Cell)) break; // Если первый игрок выиграл - прерываем цикл
            if (isDraw()) break; // Ничья - тоже прерываем цикл

            whoseMove = 2; // Ход второго игрока/ии
            if (mode == 1) { // Если режим игры с ИИ...
                aiTurn(); // То ИИ делает ход
            } else { // Иначе...
                humanTurn(); // Ход игрока
            }
            countStep++; // Количество шагов выросло на 1
            map.printMap(); // Рисуем карту
            if (isWin(gamer2Cell)) break; // Если второй игрок/ИИ выиграл - прерываем цикл
            if (isDraw()) break; // Если ничья - прерываем цикл
        }
    }

    private static void helloInstruction() { // Текстовое приветствие и управление
        System.out.println("Крестики-нолики!");
        System.out.println("Вводите координаты в формате X Y через Пробел или Enter.");
        System.out.println("Если хотите прекратить игру - введите q вместо координат.");
        System.out.println();
    }

    private static int selectMode() { // Метод выбора режима игры
        int mode = 0; // Инициируем временную переменную метода
        try { // Отслеживаем ошибку и выводим инструкцию для правильного выбора режима
            System.out.println("Выберите режим игры:");
            System.out.println("Введите 0, если хотите играть с другим игроком.");
            System.out.println("Введите 1, если хотите играть с ИИ >>>.");
            mode = sc.nextInt(); // Вводим значение для выбора режима
        } catch (InputMismatchException e) { // Ловим исключения
            System.out.println("Вы ввели неправильный режим. Включен режим по умолчанию - 0!"); // Сообщение, выводимое при ошибке
        }
        // Вывод статуса игрового режима: если режим игры выбран для игры с ИИ (значение 1), выводим, что играем с ИИ, иначе - что с другим игроком
        System.out.println("Установлен режим игры с " + (mode == 1 ? "ИИ!" : "другим игроком!"));
        System.out.println();
        sc.nextLine(); // Запрашиваем ввод текста
        return mode; // Возвращаем значение режима
    }

    private static String introduceGamer(int n) { // Метод ввода имени игрока
        System.out.println("Введите имя игрока " + n + " >>>"); // Инструкция с учетом его номера
        return sc.nextLine(); // Возвращаем введенное значение
    }

    private static void selectMap() { // Выбираем игровую карту
        int sizeX, sizeY; // Временные хранилища для их значения
        try { // Отлов исключений
            do { // Делаем хотя бы раз цикл
                // Инструкции:
                System.out.println("Введите размер поля X  Y через пробел или enter. ");
                System.out.println("X =  от 3 до 8, а Y = от 3 до 5 (все включительно). ");
                System.out.println("При этом X должен быть >= Y >>> ");
                System.out.print("Введите X: ");
                sizeX = sc.nextInt();
                System.out.print("Введите Y: ");
                sizeY = sc.nextInt();
            } while (isSizeNotValid(sizeX, 3, 8) || isSizeNotValid(sizeY, 3, 5) || sizeX < sizeY);
            // Цикл совершается, пока удовлетворяются условия проверки: если длина/ширина не меньше 3 и 3 и не больше 8 и 5, соответственно,
            // ИЛИ первое значение все еще меньше второго
        } catch (InputMismatchException e) { // Ловятся исключения - делаем это:
            System.out.println("Карта по умолчанию - 3*3"); // Выводим ограничительную инструкцию
            sizeX = sizeY = 3; // Приравниваем значения и присваиваем им значение 3
            sc.nextLine(); // Делаем новый ввод
        }
        map = new GameMap(sizeX, sizeY); // Инициируем карту с выбранными значениями
    }

    private static int selectDotsToWin() { // Выбираем кол-во символов для победы
        int dots; // Временное хранилище для значения этих символов
        if (map.getCountRow() <= 4) { // Если количество рядов меньше или равно 4
            dots = map.getCountRow(); // Присваиваем количество точек ширине рядов поля
            System.out.println("Количество фишек для победы = " + dots + "\n"); // Выводим сообщение
        } else {
            try { // Ловим исключения
                do { // Делаем хоть раз, пока выполняется условие:
                    System.out.print("Введите количество фишек, необходимых для победы 4 или 5"); // Сообщение
                    dots = sc.nextInt(); // Ввод значения
                } while (isSizeNotValid(dots, 4, 5)); // Условие: значение не должно быть меньше 4 и больше 5
            } catch (InputMismatchException e) { // Если появляется ошибка
                dots = map.getCountRow(); // Присваиваем значение временной переменной, равное кол-ву рядов
                System.out.println("Количество фишек для победы = " + dots + "\n"); // Системное сообщение
                sc.nextLine(); // Ввод
            }
        }
        return dots; // Возвращаем временное значение
    }

    private static boolean isSizeNotValid(int size, int min, int max) { // Логический геттер, проверяющий валидность введенных данных
        if (size >= min && size <= max) { // Если значение больше минимума и меньше максимума...
            return false; // ... то возвращаем false, прерывая циклы
        }
        System.out.println("Неверное значение"); // Выводим сообщение об ошибке
        return true; // Если до этого условие не выполнялось, выводим true
    }

    private static void printScore() { // Вывод счета игроков
        System.out.printf("Счет:\t\t%s\t\t%s\t\tНичья\n", gamer1Name, gamer2Name);
        System.out.printf("\t\t\t%d\t\t\t%d\t\t\t%d\n", gamer1Score, gamer2Score, drawScore);
        System.out.println();
    }

    private static void printScoreToFile() { // Занесение счета в файл
        try (BufferedWriter out = new BufferedWriter(new FileWriter("Score.txt", true))) { // Инициируем "записыватель", проверяем исключения
            if (gamer1Score + gamer2Score + drawScore == 1) { // Если сумма счета игроков и счетчика ничьи = 1,
                out.write("\n"); // Записываем в файл, что была ничья
                out.write(String.format("Счет:\t\t%s\t\t%s\t\tНичья\n", gamer1Name, gamer2Name));
            }
            out.write(String.format("\t\t\t%d\t\t\t%d\t\t\t%d\n", gamer1Score, gamer2Score, drawScore)); // Запись в файл счета двух игроков и кол-во ничьей
        } catch (IOException e) { // Если поймали исключение, ...
            System.out.println("Exp: " + e.getMessage()); // Выводим сообщение об ошибке
        }
    }

    private static void humanTurn() { // Отслеживаем шаги игроков
        int x, y; // Временные переменные координат
        try { // Ловим исключение
            do { // Делаем следующее хоть раз:
                System.out.println("Введите координаты в формате X и Y:"); // Выводим инструкцию
                System.out.print("Введите X: ");
                x = sc.nextInt() - 1; // Присваиваем значение x разности введенного значения и 1
                System.out.print("Введите Y: ");
                y = sc.nextInt() - 1; // Присваиваем значение y разности введенного значения и 1
            } while (!isValidEmpty(x, y)); // Пока значения x и y непустые
        } catch (InputMismatchException e) { // Если случилась ошибка:
            System.out.println("Вы прервали раунд!"); // Системное сообщение
            countStep = -1; // Шаг переприсваивается
            sc.nextLine(); // Ввод
            return; // Пустой возврат (по сути - break)
        }
        if (mode == 0 && whoseMove == 2) { // Если режим игры - с игроком И сейчас ходит второй игрок
            map.getCell(x, y).setDot(Dots.O); // Карта на индексах x и y ставит нолик
            gamer2Cell = map.getCell(x, y); // Значение ячейки, что поставил второй игрок присваивается нынешнему значению
        } else {
            map.getCell(x, y).setDot(Dots.X); // Карта на индексах x и y ставит X
            gamer1Cell = map.getCell(x, y); // Значение ячейки, что поставил первый игрок присваивается нынешнему значению
        }
    }

    private static boolean isValidEmpty(int x, int y) { // Проверка валидности координат
        if (!map.isCellValid(x, y)) { // Если мы вышли за пределы карты
            System.out.println("Неверные координаты"); // Системная ошибка
            return false; // Возвращаем false
        }
        if (map.getCell(x, y).isEmptyCell()) return true; // Если мы ставим значение в пустую ячейку, возвращается true
        System.out.println("Ячейка занята"); // Системное сообщение
        return false; // Если оба условия не выполняются, ставим false
    }

    private static boolean isWin(Cell lastCell) { // Проверка, кто победил
        if (countStep >= dotsToWin * 2 - 1) { // Если количество шагов больше или равно двойному кол-ву символов до победы минус 1, то проверяем:
            if (lastCell.getDot() == Dots.X && checkWin(lastCell)) { // Если при проверке, последняя ли осталась ячейка, там стоит крестик, и при проверке он побеждает, ...
                System.out.println("победил первый игрок!\n"); // Выводим сообщение о победе первого игрока
                gamer1Score++; // Увеличиваем его счетчик побед
                return true; // Возвращаем true
            }
            if (lastCell.getDot() == Dots.O && checkWin(lastCell)) { // Если при проверке все-таки победил второй игрок
                System.out.println("Победил второй игрок" + (mode == 1 ? " - ИИ!\n" : "!\n")); // Если победил ИИ - выводим, что победил ИИ, иначе - что игрок.
                gamer2Score++; // Счет второго игрока увеличивается
                return true; // Возвращаем true
            }
        }
        return false; // Если все условия не выполнены - возвращаем false
    }

    private static boolean checkWin(Cell lastCell) { // Проверяем условия победы
        if (countNonInterruptDotsToWin(lastCell.getDot(), map.getRow(lastCell)) == dotsToWin) {
            return true; // Если кол-во символов в ряд для победы совпадает, возвращаем true
        }
        if (countNonInterruptDotsToWin(lastCell.getDot(), map.getColumn(lastCell)) == dotsToWin) {
            return true; // Если кол-во символов в столбце для победы совпадает, возвращаем true
        }
        if (map.isD1(lastCell, dotsToWin) &&
                countNonInterruptDotsToWin(lastCell.getDot(), map.getD1(lastCell)) == dotsToWin) {
            return true; // Если кол-во символов по диагонали для победы совпадает, возвращаем true
        }
        // Если все условия не прервались - возвращаем результат проверки, совпадает ли количество символов для победы по другой диагонали
        return (map.isD2(lastCell, dotsToWin) &&
                countNonInterruptDotsToWin(lastCell.getDot(), map.getD2(lastCell)) == dotsToWin);
    }

    // Непрерывная последовательность символов - для определения победителя
    private static int countNonInterruptDotsToWin(Dots dot, Cell[] arrCells) {
        int counter = 0; // Счетчик заранее на нуле
        for (Cell cell : arrCells) { // Проверяем значения на карте
            // Счетчик увеличивает значение, если карта существует И значения есть
            // Иначе принудительно ставится 0
            counter = (cell != null && cell.getDot() == dot ? counter + 1 : 0);
            if (counter == dotsToWin) return counter; // Если счетчик совпадает со значениями количества символов для победы, возвращаем значение
        }
        return counter; // Если за все время цикл не прервал метод - возвращаем просто счетчик со значением 0
    }

    private static boolean isDraw() { // Проверка, наступила ли ничья
        if (countStep >= map.getCountColumn() * map.getCountRow()) { // Если количество шагов больше или равно произведению ширины и длины карты, то:
            System.out.println("Ничья\n"); // Выводим, что наступила ничья
            drawScore++; // Увеличиваем значение ничьи на 1
            return true; // Возвращаем true
        }
        return false; // Если метод не прервался - возвращаем false
    }

    private static void aiTurn() { // Метод с логикой ИИ
        for (int decrement = 1; decrement < dotsToWin; decrement++) { // Цикл для временного значения от 1 с шагом +1 до количества точек до победы
            // Шаг для победы
            if (decrement == 1 && isAIStep(gamer2Cell, decrement)) break; // Если ИИ не победил, прерываем цикл
            // Шаг для помехи
            if (isAIStep(gamer1Cell, decrement)) break; // Если ИИ помешал игроку, прерываем цикл
        }
        if (gamer2Cell == null || !map.getCell(gamer2Cell).isEmptyCell()) { // Если ссылка на значение пустая ИЛИ ячейка НЕ пустая, то...
            int x, y; // Создаем хранилище для координат
            do { // Сделаем хоть раз:
                x = rand.nextInt(map.getCountColumn()); // Вводим значение в зависимости от кол-ва колонок
                y = rand.nextInt(map.getCountRow()); // Вводим значение в зависимости от кол-ва рядов
            } while (map.getCell(x, y).getDot() != null); // Условие: ссылка на значение карты НЕ пустая
            gamer2Cell = map.getCell(x, y); // Присваиваем значение второй ячейки
        }
        map.getCell(gamer2Cell).setDot(Dots.O); // Ставим 0
        System.out.println("Компьютер походил " + (gamer2Cell.columnNumber + 1) + " " + (gamer2Cell.rowNumber + 1)); // Системный вывод с координатами с поправкой на логику языка
    }

    // ИИ выбирает шаг
    private static boolean isAIStep(Cell lastCell, int decrement) {
        if (lastCell == null || countStep < dotsToWin * 2 - 1 && lastCell.getDot() == Dots.O) {
            return false;
        }
        if (map.isD1(lastCell, dotsToWin)
                && countDotsInLine(lastCell.getDot(), map.getD1(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(map.getD1(lastCell), lastCell)) {
            return true;
        }
        if (map.isD2(lastCell, dotsToWin)
                && countDotsInLine(lastCell.getDot(), map.getD2(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(map.getD2(lastCell), lastCell)) {
            return true;
        }
        if (countDotsInLine(lastCell.getDot(), map.getRow(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(map.getRow(lastCell), lastCell)) {
            return true;
        }
        return countDotsInLine(lastCell.getDot(), map.getColumn(lastCell)) == dotsToWin - decrement
                && isEmptyValidCellInLine(map.getColumn(lastCell), lastCell);
    }

    // Всего элементов - для определения пути противодействия игроку
    private static int countDotsInLine(Dots dot, Cell[] arrCells) {
        int counter = 0;
        for (Cell cell : arrCells) {
            if (cell != null && cell.getDot() == dot) counter++;
        }
        return counter;
    }

    // Находит пустую ячейку и устанавливает ее координаты
    private static boolean isEmptyValidCellInLine(Cell[] arrCells, Cell lastCell) {
        int position = 0;
        for (int i = 0; i < arrCells.length; i++) {
            if (arrCells[i] != null && arrCells[i] == lastCell) {
                position = i;
                break;
            }
        }
        if (position != 0 && arrCells[position - 1] != null && arrCells[position - 1].isEmptyCell()) {
            gamer2Cell = arrCells[position - 1];
            return true;
        }
        if (position != arrCells.length - 1 && arrCells[position + 1] != null && arrCells[position + 1].isEmptyCell()) {
            gamer2Cell = arrCells[position + 1];
            return true;
        }
        for (Cell cell : arrCells) {
            if (cell != null && cell.isEmptyCell()) {
                gamer2Cell = cell;
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        helloInstruction(); // Печатаем инструкцию по победе
        mode = selectMode(); // Выбор режима игры
        gamer1Name = introduceGamer(1); // Представляем первого игрока
        gamer2Name = mode == 0 ? introduceGamer(2) : "ИИ"; // Представляем второго игрока, если выбран верный игровой режим, если нет, представляем ИИ
        while (true) { // Массив игры с бесконечным true
            selectMap(); // Создаем игру
            dotsToWin = selectDotsToWin(); // Выбираем, сколько символов нужно для победы
            playRound(); // Раунд игры
            printScore(); // Счет
            printScoreToFile(); // Сохранение в файл
            System.out.println("Продолжить игру? Y/N >>>"); // Предложение продолжить
            if (!sc.next().equalsIgnoreCase("y")) { // Если введено условное нет, то...
                System.out.println("Пока.."); // Прощание
                break; // Прерывание игры в лице цикла
            }
        }
        sc.close(); // Закрываем файл
    }

}