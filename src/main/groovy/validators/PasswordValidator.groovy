package validators

import mixology.User

class PasswordValidator {

    public static final passwordRegex = "^(?=.*[!@#\$&])[a-zA-Z0-9!@#\$&]{6,15}\$"

    static passwordValidator = { String val ->
        if (!val.find(passwordRegex)) {
            return ["default.invalid.user.password.criteria"]
        }
    }
}
