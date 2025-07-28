package models;
import java.time.LocalDate;

public class Customer extends BasicInfo {
    private String address;
    private LocalDate dateOfBirth;
    private int loyaltyPoints;

    public Customer() {
        this.loyaltyPoints = 0;
    }

    public Customer(String firstname, String lastname, String email, String phoneNumber,
                    UserAccount userAccount, String address, LocalDate dateOfBirth) {
        super(firstname, lastname, email, phoneNumber, userAccount);
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.loyaltyPoints = 0;
    }

    // Getters et Setters
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    public boolean redeemLoyaltyPoints(int points) {
        if (points > 0 && this.loyaltyPoints >= points) {
            this.loyaltyPoints -= points;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", loyaltyPoints=" + loyaltyPoints +
                '}';
    }
}