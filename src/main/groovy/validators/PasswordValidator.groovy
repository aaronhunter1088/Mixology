package validators

class PasswordValidator {

    public static final passwordRegex = "^(?=.*[!@#\$&])[a-zA-Z0-9!@#\$&]{6,15}\$"

    static passwordValidator = { String val ->
        if (!val.find(passwordRegex)) {
            return ["default.invalid.user.password.criteria"]
        }
        else if (val.length() < 6 || val.length() > 15) {
            return ["default.invalid.user.password.criteria"]
        }
    }
}
