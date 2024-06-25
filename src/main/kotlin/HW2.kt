sealed interface Command {
    fun isValid(): Boolean
}

data class AddCommand(val person: Person) : Command {
    override fun isValid(): Boolean {
        return isValidPhone(person.phone) && isValidEmail(person.email)
    }
}

object ShowCommand : Command {
    override fun isValid(): Boolean {
        return true // Всегда валидна, так как show не требует аргументов
    }
}

object HelpCommand : Command {
    override fun isValid(): Boolean {
        return true // Всегда валидна, так как help не требует аргументов
    }
}

object ExitCommand : Command {
    override fun isValid(): Boolean {
        return true // Всегда валидна, так как exit не требует аргументов
    }
}

data class Person(val name: String, var phone: String = "", var email: String = "")

fun readCommand(): Command {
    val input = readLine()?.trim() ?: ""
    val parts = input.split("\\s+".toRegex())

    return when (parts[0]) {
        "exit" -> ExitCommand
        "help" -> HelpCommand
        "show" -> ShowCommand
        "add" -> {
            if (parts.size >= 4) {
                val name = parts[1]
                val contactType = parts[2]
                val contactValue = parts.subList(3, parts.size).joinToString(" ")

                when (contactType) {
                    "phone" -> {
                        val person = Person(name = name, phone = contactValue)
                        AddCommand(person)
                    }
                    "email" -> {
                        val person = Person(name = name, email = contactValue)
                        AddCommand(person)
                    }
                    else -> HelpCommand // Некорректный тип контакта, возвращаем help
                }
            } else {
                HelpCommand // Неверное количество аргументов, возвращаем help
            }
        }
        else -> HelpCommand // Неизвестная команда, возвращаем help
    }
}

fun handleCommand(command: Command, contacts: MutableList<Person>) {
    when (command) {
        is ExitCommand -> {
            println("Выход из программы.")
            return
        }
        is HelpCommand -> {
            println("Список команд:")
            println("exit - выход из программы")
            println("help - вывод справки по командам")
            println("add <Имя> phone <Номер телефона> - добавление номера телефона для контакта")
            println("add <Имя> email <Адрес электронной почты> - добавление адреса электронной почты для контакта")
            println("show - вывод последнего добавленного контакта")
        }
        is ShowCommand -> {
            val lastAddedPerson = contacts.lastOrNull()
            if (lastAddedPerson == null) {
                println("Not initialized")
            } else {
                println("Последний добавленный контакт:")
                println("Имя: ${lastAddedPerson.name}")
                println("Телефон: ${lastAddedPerson.phone}")
                println("Email: ${lastAddedPerson.email}")
            }
        }
        is AddCommand -> {
            contacts.add(command.person)
            println("Добавлен контакт: ${command.person.name}")
        }
    }
}

fun main() {
    println("Добро пожаловать в программу управления контактами")
    println("Введите команду (exit, help, add <Имя> phone <Номер телефона>, add <Имя> email <Адрес электронной почты>): ")

    val contacts = mutableListOf<Person>()

    while (true) {
        val command = readCommand()
        handleCommand(command, contacts)

        println("Введите следующую команду:")
    }
}

fun isValidPhone(phoneNumber: String): Boolean {
    return phoneNumber.length in 10..12 && phoneNumber.all { it.isDigit() || it == '+' || it == '-' }
}

fun isValidEmail(email: String): Boolean {
    val atIndex = email.indexOf('@')
    return atIndex > 0 && email.indexOf('.', atIndex) > atIndex + 1
}