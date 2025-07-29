package utils;

import java.util.UUID;


public class GenerateMatricule {
    public static final String MATRICULE_PREFIX = "ada-ch3-2025-";

    public static String generateMatricule(String firstname, String lastname) {
        StringBuilder initials = new StringBuilder();
        if (firstname != null && !firstname.isEmpty()) {
            initials.append(firstname.charAt(0));
        }
        if (lastname != null && !lastname.isEmpty()) {
            if(lastname.split(" ").length > 1){
                for(String word : lastname.split(" ")){
                    initials.append(word.charAt(0));
                }
            }else{
                initials.append(lastname.charAt(0));
            }

        }
        return MATRICULE_PREFIX + initials.toString()
                .toUpperCase() + "-"
                + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 4);
    }
}
