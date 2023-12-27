package spring

import mixology.AuthenticationFilter
import mixology.AuthenticationService
//import mixology.AuthenticationSuccessListener
import mixology.UserPasswordEncoderListener
import mixology.AuthenticationProvider
import mixology.CustomUserDetailsService
import org.springframework.web.filter.CompositeFilter

// Place your Spring DSL code here. Equivalent to the root <beans> element in Spring XML.
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

    authenticationService(AuthenticationService) { bean ->
        bean.autowire = 'byName'
    }

    authenticationFilter(AuthenticationFilter) { bean ->
        bean.autowire = 'byName'
        authenticationService = ref('authenticationService')
    }

//    authenticationSuccessListener(AuthenticationSuccessListener) { bean ->
//        bean.autowire = 'byName'
//    }

//    customFilterChain(CompositeFilter) {
//        filters = [
//                ref('authenticationFilter')
//        ]
//    }

}
