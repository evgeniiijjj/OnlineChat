package util;


public enum LoggerMessages {
    CONNECT ("Соединение с сервером %s установлено"),
    EXCEPTION ("%s"),
    HANDLER ("Обработчик сообщений сервера запущен"),
    OUT ("Сервер %s разорвал соединение"),
    READ ("Сообщение сервера %s прочитано - %s"),
    SEND ("Серверу %s отправлено сообщение - %s"),
    START ("Клиент запущен"),
    STOP ("Клиент остановлен");

    private final String message;

    LoggerMessages(String message) {
        this.message = message;
    }

    public String getMessage(String... strings) {
        return switch (strings.length) {
            case 1 -> String.format(message, strings[0]);
            case 2 -> String.format(message, strings[0], strings[1]);
            default -> message;
        };
    }
}
