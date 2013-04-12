package project.model;

public final class LoginException extends Exception {
    public static enum Type {
        USER_NOT_FOUND, DUPLICATE_USERNAME;
    }

    private final Type type;

    private final String username;

    LoginException(Type type, String username) {
        this.type = type;
        this.username = username;
    }
}
