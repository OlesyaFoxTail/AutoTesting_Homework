public enum UsersMail {
    var_1("aamdkfng"," "),
    var_2("aamdkfng",""),
    var_3("aamdkfng","aiqkjlkjahlskdjjkhgdkjfbmsndfmnabsdjfhagwlehawjehfkljshdlkfjhnjhjhf@kolya.ru"),
    var_4("aamdkfng","aiolya@lkjlkjahlskdjjkhgdkjfbmsndfmnabsdjfhagwlehawjehfkljshdlkfjhnjhjhf.ru"),
    var_5("aamdkfng","aiolyamail.ru"),
    var_6("aamdkfng","aiolya@.ru"),
    var_7("aamdkfng","@mail.ru"),
    var_8("aamdkfng","aiolya@mail..ru"),
    var_9("aamdkfggsg",".aiolya@mail.ru"),
    var_10("aakjdkhsg","aiol--ya@mail.ru"),
    var_11("aalkjasdsg","-aiolya@mail.ru"),
    var_12("aaehoinsg","aiiolya@-mail.ru"),
    var_13("aalksdjfksg","aiolya-@mail.ru"),
    var_14("aakdjfkhsg","aiolya@mail.ru-");
    private String mail;
    private String name;

    UsersMail(String name, String mail) {
        this.mail = mail;
        this.name = name;
    }

    public String getMail() {
        return mail;
    }
    public String getName() {
        return name;
    }
}

