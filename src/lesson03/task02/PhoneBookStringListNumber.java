package lesson03.task02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBookStringListNumber implements PhoneBook {

    private Map<String, List<Long>> phoneBook = new HashMap<>();

    public PhoneBookStringListNumber() {
    }

    public void add(Long phoneNumber, String lastName) {
        if (phoneNumber == null || lastName == null) {
            System.out.printf("Неверные параметры запроса: телефонный номер = %d, фамилия: %s\n", phoneNumber, lastName);
            return;
        }
        phoneBook.putIfAbsent(lastName, new ArrayList<>());
        phoneBook.get(lastName).add(phoneNumber);
    }

    public void get(String lastName) {
        List<Long> listPhoneNumbers = phoneBook.getOrDefault(lastName, new ArrayList<>());

        int countPhoneNumber = listPhoneNumbers.size();
        if (countPhoneNumber == 0) {
            System.out.printf(
                    "На фамилию: %s телефонных номеров не найдено. Проверьте правильность написания фамилии.\n", lastName);
        } else {
            System.out.printf("На фамилию: %s найдено телефонных номеров %d шт.:\n", lastName, countPhoneNumber);
            listPhoneNumbers.forEach(System.out::println);
        }
    }
}
