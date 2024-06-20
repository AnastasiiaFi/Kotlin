fun main() {
    println("Добро пожаловать в программу управления контактами")
    println("Введите команду (exit, help, add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>): ")

    var command: String
    while (true) {
        command = readLine()?.trim() ?: ""

        when {
            command == "exit" -> {
                println("Выход из программы.")
                return
            }
            command == "help" -> {
                println("Список команд:")
                println("exit - выход из программы")
                println("help - вывод справки по командам")
                println("add <Имя> phone <Номер телефона> - добавление номера телефона для контакта")
                println("add <Имя> email <Адрес электронной почты> - добавление адреса электронной почты для контакта")
            }
            command.startsWith("add") -> handleAddCommand(command)
            else -> println("Неизвестная команда. Введите 'help' для получения справки.")
        }

        println("Введите следующую команду:")
    }
}

fun handleAddCommand(command: String) {
    val parts = command.trim().split("\\s+".toRegex())
    if (parts.size < 4) {
        println("Некорректная команда add. Используйте: add <Имя> phone <Номер телефона> или add <Имя> email <Адрес электронной почты>")
        return
    }

    val name = parts[1]
    val contactType = parts[2]
    val contactValue = parts.subList(3, parts.size).joinToString(" ")

    when (contactType) {
        "phone" -> {
            if (!validatePhoneNumber(contactValue)) {
                println("Некорректный формат номера телефона.")
                return
            }
            println("Добавлен контакт: $name - $contactType: $contactValue")
        }
        "email" -> {
            if (!validateEmail(contactValue)) {
                println("Некорректный формат адреса электронной почты.")
                return
            }
            println("Добавлен контакт: $name - $contactType: $contactValue")
        }
        else -> {
            println("Некорректный тип контакта. Используйте 'phone' или 'email'.")
            return
        }
    }
}

fun validatePhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.length in 10..12 && phoneNumber.all { it.isDigit() || it == '+' || it == '-' }
}

fun validateEmail(email: String): Boolean {
    val atIndex = email.indexOf('@')
    return atIndex > 0 && email.indexOf('.', atIndex) > atIndex + 1
}



