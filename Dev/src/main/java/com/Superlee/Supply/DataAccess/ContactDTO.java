package com.Superlee.Supply.DataAccess;

public class ContactDTO {

    private final String ContactIDColumnName="ContactID";
    private final String SupplierIdColumnName="SupplierId";
    private final String NameColumnName="Name";
    private final String PhoneNumberColumnName="PhoneNumber";
    private ContactDAO contactDAO;
    private String name;
    private String phoneNumber;
    private int contactId;
    private int supplierId;

    public ContactDTO(int contactId, int supplierId, String name, String phoneNumber) {
        this.name = name;
        this.contactId = contactId;
        this.supplierId = supplierId;
        this.phoneNumber = phoneNumber;
        this.contactDAO = new ContactDAO();
    }
    public boolean insert() {
        return contactDAO.insert(this);
    }
    public boolean delete() {
        return contactDAO.delete(this);
    }

    public String getName() {
        return name;
    }

    public int getContactId() {
        return contactId;
    }

    public void setName(String name) {
        contactDAO.updateContactName(contactId, name);
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        contactDAO.updateContactPhoneNumber(contactId, phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSupplierId() {
        return supplierId;
    }
}
