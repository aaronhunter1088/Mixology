// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.active = true
grails.plugin.springsecurity.formTokensEnabled = true
grails.plugin.springsecurity.rejectIfNoRule = false
grails.plugin.springsecurity.useBasicAuth = true
grails.plugin.springsecurity.basic.realmName = 'Grails Realm'
grails.plugin.springsecurity.basic.credentialsCharset = 'UTF-8'
grails.mime.use.accept.header = true
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'mixology.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mixology.UserRole'
grails.plugin.springsecurity.authority.className = 'mixology.Role'

grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.endpointUrl =   '/login/auth'
grails.plugin.springsecurity.rest.logout.endpointUrl =  '/logout'
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'username'
grails.plugin.springsecurity.rest.login.passwordPropertyName = 'password'
grails.plugin.springsecurity.rest.token.validation.useBearerToken = false
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'
grails.plugin.springsecurity.rest.token.storage.useGorm = true
grails.plugin.springsecurity.rest.token.storage.gorm.tokenDomainClassName = 'mixology.AuthToken'
grails.plugin.springsecurity.rest.token.storage.gorm.tokenValuePropertyName = 'tokenValue'
grails.plugin.springsecurity.rest.token.storage.gorm.usernamePropertyName = 'username'

grails.plugin.springsecurity.providerNames = [
		'daoAuthenticationProvider'
		,'anonymousAuthenticationProvider'
		//,'rememberMeAuthenticationProvider'
]

grails.plugins.springsecurity.filterChain.filterNames = [
		'filterInvocationInterceptor','authenticationFilter'
		,'securityContextPersistenceFilter', 'logoutFilter'
		//,'anonymousAuthenticationFilter' //,'authenticationProcessingFilter'
		//   ,'rememberMeAuthenticationFilter', 'myCustomProcessingFilter'
		//   ,'exceptionTranslationFilter',
]

grails.plugin.springsecurity.filterChain.chainMap = [
		[pattern: '/assets/**',      filters: 'none'],
		[pattern: '/**/js/**',       filters: 'none'],
		[pattern: '/**/css/**',      filters: 'none'],
		[pattern: '/**/images/**',   filters: 'none'],
		[pattern: '/**/favicon.ico', filters: 'none'],
		[pattern:'/auth/**',
		 filters:'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationFilter,-securityContextPersistenceFilter'
		],
		//Stateless chain
		[pattern: '/v1/**', filters: 'none'],
		 //filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-authenticationFilter,''-exceptionTranslationFilter,-securityContextPersistenceFilter'],
		//Traditional, stateful chain //[pattern: '/**',             filters: 'JOINED_FILTERS'],
		[pattern: '/**',
		 filters: 'JOINED_FILTERS'//,-restTokenValidationFilter,-restExceptionTranslationFilter'
		]
]

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
		/* Defaults */
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/drinks',		 access: ['permitAll']],
	[pattern: '/customDrinks',	 access: ['permitAll']],
		/* Login Controller */
	[pattern: '/login/auth', 	 access: ['permitAll']],
		/* Drink Controller */
	[pattern: '/drink/index',	 access: ['ROLE_ADMIN']],
	[pattern: '/drink/customIndex',access: ['permitAll']],
	[pattern: '/drink/show',     access: ['permitAll']],
	[pattern: '/drink/save',     access: ['permitAll']],
	[pattern: '/drink/edit',     access: ['permitAll']],
	[pattern: '/drink/copy',     access: ['permitAll']],
	[pattern: '/drink/update',   access: ['permitAll']],
	[pattern: '/drink/sendADrinkEmail', access: ['permitAll']],
	[pattern: '/drink/validateIngredients', access: ['permitAll']],
	[pattern: '/drink/delete',   access: ['permitAll']],
		/* Ingredient Controller */
	[pattern: '/ingredient/index',	  access: ['ROLE_ADMIN']],
	[pattern: '/ingredient/customIndex',access: ['permitAll']],
	[pattern: '/ingredient/show',     access: ['permitAll']],
	[pattern: '/ingredient/save',     access: ['permitAll']],
	[pattern: '/ingredient/edit',     access: ['permitAll']],
	[pattern: '/ingredient/update',	  access: ['permitAll']],
	[pattern: '/ingredient/delete',   access: ['permitAll']],
		/* User Controller */
	[pattern: '/user/index',	 	  access: ['ROLE_ADMIN']],
	[pattern: '/user/show',      	  access: ['ROLE_ADMIN','ROLE_USER']],
	[pattern: '/user/create',    	  access: ['permitAll']],
	[pattern: '/user/save',      	  access: ['permitAll']],
	[pattern: '/user/edit',      	  access: ['ROLE_ADMIN','ROLE_USER']],
	[pattern: '/user/delete',         access: ['ROLE_ADMIN']],
	[pattern: '/user/darkMode',       access: ['permitAll']],
	[pattern: '/**',                  access: ['IS_AUTHENTICATED_FULLY']],
	[pattern: '/v1/tokens/**',        access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
	[pattern: '/v1/**',               access: ['ROLE_ADMIN','ROLE_USER']],
	[pattern: '/swagger-ui/**',       access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
	[pattern: '/v3/api-docs',         access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
]

grails.plugins.springsecurity.interceptUrlMap = [
	/* Resources */
	[pattern: '/**',                  access: ['IS_AUTHENTICATED_FULLY']],
	[pattern: '/v1/tokens/**',        access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
	[pattern: '/v1/**',               access: ['ROLE_ADMIN','ROLE_USER']],
	[pattern: '/swagger-ui/**',       access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
	[pattern: '/v3/api-docs',         access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
]
