package validators

import mixology.User

class PasswordValidator {

    public static final passwordRegex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{6,15})+\$"

    static passwordValidator = { String val ->
        if (!val.find(passwordRegex)) {
            return ["default.invalid.password.message", 'password', User.class.getSimpleName(), val]
        }
    }
}
