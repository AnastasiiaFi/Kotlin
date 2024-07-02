package HW4

import java.io.File

data class Person(
    val name: String,
    val phones: MutableList<String> = mutableListOf(),
    val emails: MutableList<String> = mutableListOf()
)

class JsonBuilder {
    val stringBuilder = StringBuilder()

    fun array(init: JsonBuilder.() -> Unit) {
        stringBuilder.append("[\n")
        init()
        stringBuilder.append("]\n")
    }

    fun obj(init: JsonBuilder.() -> Unit) {
        stringBuilder.append("{\n")
        init()
        stringBuilder.append("}\n")
    }

    fun field(name: String, value: String) {
        stringBuilder.append("\t\"$name\": \"$value\",\n")
    }

    fun field(name: String, values: List<String>) {
        stringBuilder.append("\t\"$name\": [")
        values.forEachIndexed { index, value ->
            stringBuilder.append("\"$value\"")
            if (index < values.size - 1) {
                stringBuilder.append(", ")
            }
        }
        stringBuilder.append("],\n")
    }

    override fun toString(): String {
        return stringBuilder.toString()
    }
}

class PhoneBook {
    private val contacts: MutableList<Person> = mutableListOf()

    fun addPerson(person: Person) {
        contacts.add(person)
    }

    fun addPhoneToPerson(name: String, phone: String) {
        if (!isValidPhone(phone)) {
            println("Неверный формат номера телефона. Используйте формат XXXXXXXXXX (только цифры).")
            return
        }

        var person = contacts.find { it.name == name }
        if (person == null) {
            person = Person(name)
            contacts.add(person)
        }
        person.phones.add(phone)
        println("Добавлен телефон $phone для $name")
    }

    fun addEmailToPerson(name: String, email: String) {
        if (!isValidEmail(email)) {
            println("Неверный формат email адреса.")
            return
        }

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

    fun exportToJsonFile(filePath: String) {
        val jsonBuilder = JsonBuilder()
        jsonBuilder.array {
            contacts.forEachIndexed { index, person ->
                obj {
                    field("name", person.name)
                    field("phones", person.phones)
                    field("emails", person.emails)
                }
                if (index < contacts.size - 1) {
                    stringBuilder.append(",\n")
                }
            }
        }

        File(filePath).writeText(jsonBuilder.toString())
        println("Данные экспортированы в файл: $filePath")
    }

    private fun isValidPhone(phone: String): Boolean {
        val regex = Regex("\\d{10}")
        return regex.matches(phone)
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regex.matches(email)
    }
}

fun main() {
    println("Добро пожаловать в программу управления контактами")
    println("Доступные команды: add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>, show <Имя>, find <Телефон или Почта>, export <Путь к файлу>, exit")

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
            "export" -> {
                if (parts.size == 2) {
                    val filePath = parts[1]
                    phoneBook.exportToJsonFile(filePath)
                } else {
                    println("Неверная команда. Используйте 'export <Путь к файлу>'.")
                }
            }
            else -> println("Неизвестная команда. Используйте add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>, show <Имя>, find <Телефон или Почта>, export <Путь к файлу>, exit")
        }
    }
}

