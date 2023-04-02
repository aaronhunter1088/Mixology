package validators

import mixology.User

class EmailValidator {

    public static final emailRegex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+\$"

    static emailValidator = { String val ->
        if (!val.find(emailRegex)) {
            return ["default.invalid.email.message", 'email', User.class.getSimpleName(), val]
        }
    }

}
