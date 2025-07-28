package services;

import dao.AdminDao;
import dao.CustomerDao;
import dao.MerchantDao;
import dao.UserAccountDao;
import models.Admin;
import models.Customer;
import models.Merchant;
import models.UserAccount;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private MerchantDao merchantDao;
    private CustomerDao customerDao;

    private UserAccountDao userAccountDao;
    private AdminDao adminDao;

    public UserService() {
        this.userAccountDao = new UserAccountDao();
        this.adminDao = new AdminDao();
        this.merchantDao = new MerchantDao();
        this.customerDao = new CustomerDao();
    }

    // Méthodes d'authentification
    public Optional<UserAccount> authenticate(String username, String password) {
        Optional<UserAccount> account = userAccountDao.findByUsername(username);
        if (account.isPresent() && verifyPassword(password, account.get().getPassword())) {
            return account;
        }
        return Optional.empty();
    }

    public List<Merchant> getAllMerchants() {
        return merchantDao.findAll();
    }

    public boolean updateMerchant(Merchant merchant) {
        return merchantDao.update(merchant);
    }

    public boolean deleteMerchant(int id) {
        return merchantDao.delete(id);
    }


    // Méthodes pour les clients
    public boolean createCustomer(Customer customer) {
        customer.getUserAccount().setPassword(hashPassword(customer.getUserAccount().getPassword()));
        return customerDao.create(customer);
    }

    public Optional<Customer> getCustomerById(int id) {
        return customerDao.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    public boolean updateCustomer(Customer customer) {
        return customerDao.update(customer);
    }

    public boolean deleteCustomer(int id) {
        return customerDao.delete(id);
    }

    public List<Customer> getCustomersByLoyaltyPoints(int minPoints, int maxPoints) {
        return customerDao.findByLoyaltyPointsRange(minPoints, maxPoints);
    }

    public boolean createMerchant(Merchant merchant) {
        merchant.getUserAccount().setPassword(hashPassword(merchant.getUserAccount().getPassword()));
        return merchantDao.create(merchant);
    }

    // Méthodes pour les admins
    public boolean createAdmin(Admin admin) {
        // Hasher le mot de passe
        admin.getUserAccount().setPassword(hashPassword(admin.getUserAccount().getPassword()));
        return adminDao.create(admin);
    }

    public Optional<Admin> getAdminById(int id) {
        return adminDao.findById(id);
    }

    public List<Admin> getAllAdmins() {
        return adminDao.findAll();
    }

    public boolean updateAdmin(Admin admin) {
        return adminDao.update(admin);
    }

    public boolean deleteAdmin(int id) {
        return adminDao.delete(id);
    }

    // Utilitaires de sécurité
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }

    public boolean changePassword(int accountId, String oldPassword, String newPassword) {
        Optional<UserAccount> account = userAccountDao.findById(accountId);
        if (account.isPresent() && verifyPassword(oldPassword, account.get().getPassword())) {
            account.get().setPassword(hashPassword(newPassword));
            return userAccountDao.update(account.get());
        }
        return false;
    }
}

// ================================
// 7. DAO pour Merchant et Customer
// ================================

// MerchantDao.java


