package ci.ada.utils;

import java.util.UUID;


public class MatriculeUtil {
    public static String buildInitials(String firstname, String lastname) {
        StringBuilder initials = new StringBuilder();
        if (firstname != null && !firstname.isEmpty()) {
            initials.append(firstname.charAt(0));
        }
        if (lastname != null && !lastname.isEmpty()) {
            String[] parts = lastname.split(" ");
            for (String part : parts) {
                initials.append(part.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }

    public static String generateRandomSuffix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();
    }
}
