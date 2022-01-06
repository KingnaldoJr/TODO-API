package dev.rmjr.todo.util;

public final class Patterns {
    private Patterns() {}

    public static final String EMAIL = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$";
    public static final String FIRST_NAME = "^[\\w'\\-,.][^0-9_!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:[\\\\] ]{2,}$";
    public static final String LAST_NAME = "^[\\w'\\-,.][^0-9_!¡?÷?¿/\\\\+=@#$%ˆ&*(){}|~<>;:[\\\\]]{2,}$";
    public static final String PASSWORD = "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2,})[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\-\\$\\^\\@\\/]{8,32}$";
    public static final String PHONE = "^(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*$";
    public static final String UUID = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";
}
