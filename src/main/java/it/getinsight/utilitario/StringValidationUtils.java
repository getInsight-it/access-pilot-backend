package it.getinsight.utilitario;

public class StringValidationUtils {

    private StringValidationUtils() {}


    public static boolean isUpperSnakeCase(String input) {
        return input != null && input.matches("^[A-Z][A-Z0-9_]*$");
    }

}
