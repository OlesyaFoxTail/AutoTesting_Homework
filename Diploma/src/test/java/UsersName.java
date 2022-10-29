public enum UsersName {
    OPTION_1 ("Kolya"),
    OPTION_2 ("Tolya "),
    OPTION_3 (" Volya"),
    OPTION_4 (""),
    OPTION_5 ("  "),
    OPTION_6 ("VAsyA"),
    OPTION_7 ("Маша`~@#$%^&*()_+|-=\\{}[]:”;’<>?,./®©£¥¢¦§«»€"),
    OPTION_8 ("?!"),
    OPTION_9 ("877637567"),
    OPTION_10 ("äöüÄÖÜß"),
    OPTION_11 ("àâçéèêëïô"),
    OPTION_12 ("NÑO"),
    OPTION_13 ("ÀàÁáÈè"),
    OPTION_14 ("한"),
    OPTION_15 ("中"),
    OPTION_16 ("日"),
    OPTION_17 ("<IMG SRC=j&#X41vascript:alert('test2')>"),
    OPTION_18 ("<script>alert(‘XSS’)</script>"),
    OPTION_19 ("javascript:alert('alert');");

    private String login;
    UsersName(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}


