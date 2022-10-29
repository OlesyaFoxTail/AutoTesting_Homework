public enum UsersData {
    OPTION_1 ("molo", "molo@molo.ru", "molo"),
    OPTION_2 ("molo", "wowo@molo.ru", "wowo"),
    OPTION_3 ("wowo", "molo@molo.ru", "wowo");


    private String name;
    private String mail;
    private String password;

    UsersData(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }
}