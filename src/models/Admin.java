package models;

import java.util.ArrayList;
import java.util.List;

public class Admin extends BasicInfo {
    private List<String> privileges;

    public Admin() {
        this.privileges = new ArrayList<>();
    }

    public Admin(String firstname, String lastname, String email, String phoneNumber, UserAccount userAccount) {
        super(firstname, lastname, email, phoneNumber, userAccount);
        this.privileges = new ArrayList<>();
    }

    public List<String> getPrivileges() { return privileges; }
    public void setPrivileges(List<String> privileges) {
        if (privileges == null) throw new IllegalArgumentException("Les privilèges ne peuvent pas être null");
        this.privileges = privileges;
    }

    public void addPrivilege(String privilege) {
        if (privilege != null && !privileges.contains(privilege)) {
            privileges.add(privilege);
        }
    }

    public void removePrivilege(String privilege) {
        privileges.remove(privilege);
    }

    public boolean hasPrivilege(String privilege) {
        return privileges.contains(privilege);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
