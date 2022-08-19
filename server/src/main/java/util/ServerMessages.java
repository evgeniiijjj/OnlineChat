package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum ServerMessages {
    INFO ("Server %s - Вы подключились к открытому чат-серверу, для продолжения введите имя (кол-во символов от %d до %d) или exit для выхода"),
    FAILURE ("Server %s - Вы ввели некорректное имя, попробуйте еще раз или введите exit для выхода"),
    NAME_IS_ALREADY_EXISTS ("Server %s - Имя %s уже присутствует в чате, попробуйте другое имя или введите exit для выхода"),
    WELCOME ("Server %s - %s добро пожаловать в чат!"),
    WELCOMES ("Server %s - Поприветствуйте нового участника чата - %s!"),
    BROADCAST ("%s %s - %s");


    private final String message;

    ServerMessages(String message) {
        this.message = message;
    }

    public String getMessage(String... strings) {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return switch (strings.length) {
            case 1 -> String.format(message, dateTime, strings[0]);
            case 2 -> String.format(message, strings[0], dateTime, strings[1]);
            default ->
                    String.format(message, dateTime, ServerConstants.CLIENT_NAME_MIN_LEN.getLen(), ServerConstants.CLIENT_NAME_MAX_LEN.getLen());
        };
    }
}
