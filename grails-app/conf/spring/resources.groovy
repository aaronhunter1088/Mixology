import mixology.UserPasswordEncoderListener
import mixology.AuthenticationProvider
import mixology.CustomUserDetailsService
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)
    authenticationProvider(AuthenticationProvider) {
        //coordinateValidator = ref('coordinateValidator')
        userDetailsService = ref('userDetailsService')
        passwordEncoder = ref('passwordEncoder')
        userCache = ref('userCache')
        preAuthenticationChecks = ref('preAuthenticationChecks')
        postAuthenticationChecks = ref('postAuthenticationChecks')
        authoritiesMapper = ref('authoritiesMapper')
        hideUserNotFoundExceptions = true
    }
}
