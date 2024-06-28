data class Person(
    val name: String,
    val phones: MutableList<String> = mutableListOf(),
    val emails: MutableList<String> = mutableListOf()
)

class PhoneBook {
    private val contacts: MutableList<Person> = mutableListOf()

    fun addPerson(person: Person) {
        contacts.add(person)
    }

    fun addPhoneToPerson(name: String, phone: String) {
        var person = contacts.find { it.name == name }
        if (person == null) {
            person = Person(name)
            contacts.add(person)
        }
        person.phones.add(phone)
        println("Добавлен телефон $phone для $name")
    }

    fun addEmailToPerson(name: String, email: String) {
        var person = contacts.find { it.name == name }
        if (person == null) {
            person = Person(name)
            contacts.add(person)
        }
        person.emails.add(email)
        println("Добавлен email $email для $name")
    }

    fun show(name: String) {
        val person = contacts.find { it.name == name }
        if (person != null) {
            println("Телефоны ${person.name}:")
            person.phones.forEach { println(it) }
            println("Emails ${person.name}:")
            person.emails.forEach { println(it) }
        } else {
            println("Человек $name не найден.")
        }
    }

    fun find(value: String) {
        val results = contacts.filter { it.phones.contains(value) || it.emails.contains(value) }
        if (results.isEmpty()) {
            println("Люди с $value не найдены")
        } else {
            println("Найденные люди для $value:")
            results.forEach { println(it.name) }
        }
    }
}

fun main() {
    println("Добро пожаловать в программу управления контактами")
    println("Доступные команды: add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>, show <Имя>, find <Телефон или Почта>, exit")

    val phoneBook = PhoneBook()

    while (true) {
        print("Введите команду: ")
        val input = readLine()?.trim() ?: ""
        val parts = input.split("\\s+".toRegex())

        when (parts[0]) {
            "exit" -> {
                println("Завершение программы.")
                return
            }
            "add" -> {
                if (parts.size >= 4) {
                    val name = parts[1]
                    val type = parts[2]
                    val value = parts.subList(3, parts.size).joinToString(" ")

                    when (type) {
                        "phone" -> phoneBook.addPhoneToPerson(name, value)
                        "email" -> phoneBook.addEmailToPerson(name, value)
                        else -> println("Неверная команда. Используйте 'add <Имя> phone <Номер телефона>' или 'add <Имя> email <Адрес электронной почты>'.")
                    }
                } else {
                    println("Неверная команда. Используйте 'add <Имя> phone <Номер телефона>' или 'add <Имя> email <Адрес электронной почты>'.")
                }
            }
            "show" -> {
                if (parts.size == 2) {
                    val name = parts[1]
                    phoneBook.show(name)
                } else {
                    println("Неверная команда. Используйте 'show <Имя>'.")
                }
            }
            "find" -> {
                if (parts.size == 2) {
                    val value = parts[1]
                    phoneBook.find(value)
                } else {
                    println("Неверная команда. Используйте 'find <Телефон или Почта>'.")
                }
            }
            else -> println("Неизвестная команда. Используйте add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>, show <Имя>, find <Телефон или Почта>, exit")
        }
    }
}
