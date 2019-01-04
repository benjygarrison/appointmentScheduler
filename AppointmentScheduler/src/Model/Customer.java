
package Model;

/**
 *
 * @author Ben Garrison
 */
public class Customer {
    
    private String customerId;
    private String customerName;
    
    private String addressId;        
    private String address;
    private String address2;    
    private String postalCode;
    private String phone;
    
    private String cityId;
    private String city;
    
    private String countryId;    
    private String country;
    
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdatedBy;
    
    public Customer(String customerId, String customerName, String address, String address2, 
    String city, String cityId, String country, String postalCode, String phone) {                
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.cityId = cityId;
        this.country = country;
        this.postalCode = postalCode;
        this.phone = phone;        
    }
    
    public Customer(String customerName, String address, String address2, String phone){
        this.customerName = customerName;
        this.address = address;
        this.address2 = address2;
        this.phone = phone;        
    }

    public Customer(String customerId, String customerName, String address, String address2, String city, 
            String postalCode, String country, String phone, String addressId, String cityId, String countryId) {
        
        this.customerId = customerId;
        this.customerName = customerName; 
        this.address = address;
        this.address2 = address2;
        this.city = city;       
        this.postalCode = postalCode;       
        this.country = country;         
        this.phone = phone; 
        this.addressId = addressId;
        this.cityId = cityId;
        this.countryId = countryId;        
    }
    
    public Customer(String customerName, String customerId){
        this.customerName = customerName;
        this.customerId = customerId;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }               
}
