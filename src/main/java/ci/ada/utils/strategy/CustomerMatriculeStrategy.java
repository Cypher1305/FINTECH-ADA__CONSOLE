package ci.ada.utils.strategy;

import ci.ada.Interfaces.MatriculeStrategy;
import ci.ada.utils.MatriculeUtil;
import java.util.UUID;

public class CustomerMatriculeStrategy implements MatriculeStrategy {
    private static final String PREFIX = "ada-ch3-2025-CUSTOMER-";

    @Override
    public String generateMatricule(String firstname, String lastname) {
        return PREFIX + MatriculeUtil.buildInitials(firstname, lastname) + "-" + MatriculeUtil.generateRandomSuffix();
    }
}
