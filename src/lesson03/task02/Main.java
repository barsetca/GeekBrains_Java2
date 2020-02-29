package lesson03.task02;

public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.out.println("PhoneBookNumberString.class");
        PhoneBook phoneBookNumberString = new PhoneBookNumberString();
        fillPhoneBook(phoneBookNumberString);
        checkGetPhoneNumber(phoneBookNumberString);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("PhoneBookStringListNumber.class");
        PhoneBookStringListNumber phoneBookStringListNumber = new PhoneBookStringListNumber();
        fillPhoneBook(phoneBookStringListNumber);
        checkGetPhoneNumber(phoneBookStringListNumber);
    }

    public static void fillPhoneBook(PhoneBook phoneBook) {
        phoneBook.add(123L, "Иванов");
        phoneBook.add(1234L, "Сидоров");
        phoneBook.add(12345L, "Петров");
        phoneBook.add(123456L, "Петров");
        phoneBook.add(1234567L, "Иванов");
        phoneBook.add(12345678L, "Иванов");
        phoneBook.add(12345678L, null);
        phoneBook.add(null, "Сидоров");
    }

    public static void checkGetPhoneNumber(PhoneBook phoneBook) {
        phoneBook.get("Иванов");
        phoneBook.get("Сидоров");
        phoneBook.get("Петров");
        phoneBook.get("Дмитриев");
        phoneBook.get(null);
    }
}
