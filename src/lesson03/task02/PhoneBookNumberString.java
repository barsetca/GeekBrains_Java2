package lesson03.task02;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneBookNumberString implements PhoneBook {

    private Map<Long, String> phoneBook = new HashMap<>();

    public PhoneBookNumberString() {
    }

    public void add(Long phoneNumber, String lastName) {
        if (phoneNumber == null || lastName == null) {
            System.out.printf("Неверные параметры запроса: телефонный номер = %d, фамилия: %s\n", phoneNumber, lastName);
            return;
        }
        phoneBook.put(phoneNumber, lastName);
    }

    public void get(String lastName) {

        List<Long> listPhoneNumbers = new ArrayList<>();
        phoneBook.forEach((key, value) -> {
            if (value.equals(lastName)) {
                listPhoneNumbers.add(key);
            }
        });
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
