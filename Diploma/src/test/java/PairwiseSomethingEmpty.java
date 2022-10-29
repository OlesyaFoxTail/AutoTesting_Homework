import java.text.StringCharacterIterator;

public enum PairwiseSomethingEmpty {
    option_1("molo", "molo","ул.Советская, д13", "Адлер","","123456","+7-123-45-67","Russia","molo@molo.ru"),
    option_2("molo","", "", "","","","","Select a country / region…",""),
    option_3("", "","ул.Советская, д13", "","Адлерский","","+7-123-45-67","","molo@molo.ru"),
    option_4("", "molo","", "Адлер","","123456","","Russia","");

    public String nameNew;
    public String familyName;
    public String address;
    public String city;
    public String state;
    public String postCode;
    public String phone;
    public String country;
    public String email;

        PairwiseSomethingEmpty(String nameNew, String familyName, String address, String city, String state, String postCode, String phone, String country,String email){
        this.nameNew = nameNew;
        this.familyName = familyName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
        this.phone = phone;
        this.country = country;
        this.email = email;
    }

    public String getNameNew() {
        return nameNew;
    }
    public String getFamilyName() {
            return familyName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }
    public String getState() {
        return state;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
            return country;
    }
    public String getEmail() {
            return email;
    }

}
