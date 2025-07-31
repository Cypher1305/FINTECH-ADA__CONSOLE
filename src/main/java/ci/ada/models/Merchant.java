package ci.ada.models;

public class Merchant extends BasicInfo {
    private String storeName;
    private String storeAddress;
    private String taxNumber;

    public Merchant() {}

    public Merchant(String firstname, String lastname, String email, String phoneNumber,
                    UserAccount userAccount, String storeName, String storeAddress, String taxNumber) {
        super(firstname, lastname, email, phoneNumber, userAccount);
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.taxNumber = taxNumber;
    }

    // Getters et Setters
    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreAddress() { return storeAddress; }
    public void setStoreAddress(String storeAddress) { this.storeAddress = storeAddress; }

    public String getTaxNumber() { return taxNumber; }
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + getId() +
                ", firstname='" + getFirstname() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                '}';
    }
}
