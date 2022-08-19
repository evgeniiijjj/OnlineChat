package util;


public enum LoggerMessages {
    ACCEPT ("%s установил соединение с сервером"),
    AUTH ("зарегистрирован новый участник чата %s %s"),
    EXCEPTION ("%s"),
    OUT ("%s %s разорвал соединение"),
    READ ("Получено сообщение от %s %s - %s"),
    SEND ("%s %s отправлено сообщение - %s"),
    START ("Сервер запущен"),
    STOP ("Сервер остановлен");

    private final String message;

    LoggerMessages(String message) {
        this.message = message;
    }

    public String getMessage(String... strings) {
        return switch (strings.length) {
            case 1 -> String.format(message, strings[0]);
            case 2 -> String.format(message, strings[0], strings[1]);
            case 3 -> String.format(message, strings[0], strings[1], strings[2]);
            default -> message;
        };
    }
}
