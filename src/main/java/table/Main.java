package table;


import java.util.Hashtable;


public class Main {
    static HTable<String, String> table = new HTable();

    static void test(String k, String v) {
        table.put(k, v);
        System.out.println(table);
    }

    public static void main(String[] args) {
        System.out.println("Таблица:");
        test("00", "zero");
        test("01", "one");
        test("02", "two");
        test("03", "three");
        test("04", "four");
        test("05", "five");
        test("06", "six");
        test("07", "seven");
        test("08", "eight");
        test("09", "nine");
        test("10", "ten");
        test("11", "eleven");
        test("12", "twelve");
        test("13", "thirteen");
        test("14", "fourteen");
        test("15", "fifteen");
        test("16", "sixteen");
        test("17", "seventeen");

        System.out.println("--------------------");
        System.out.println("Кол-во пар в таблице:");
        System.out.println(table.size());

        System.out.println("--------------------");
        System.out.println("Список всех ключей:");
        System.out.println(table.keySet());

        System.out.println("---------------------");
        System.out.println("Список всех значений:");
        System.out.println(table.values());

        System.out.println("---------------------");
        System.out.println("Добавление пары:");
        String S = table.put("20", "twenty");
        String K = table.put("09", "девять");
        String A = table.put("02", "два");
        System.out.println(table);
        System.out.println("Перезапись значений:");
        System.out.println(S);
        System.out.println(K);
        System.out.println(A);

        System.out.println("---------------------");
        System.out.println("Удаление пар:");
        table.remove("17");
        table.remove("02");
        table.remove("00");
        System.out.println(table);


        System.out.println("---------------------");
        System.out.println("Поиск значения по ключу:");
        System.out.println(table.get("05"));
        System.out.println(table.get("11"));
        System.out.println(table.get("28"));

        System.out.println("---------------------");
        System.out.println("Сравнение таблиц:");
        Hashtable<String, String> map = new Hashtable<>();
        map.put("00", "ноль");
        map.put("22", "двадцать два");
        System.out.println(table);
        System.out.println(map);
        System.out.println(map.equals(table));

        System.out.println("---------------------");
        System.out.println("Удаление всех пар из таблицы:");
        table.clear();
        System.out.println(table.entrySet());

    }
}

