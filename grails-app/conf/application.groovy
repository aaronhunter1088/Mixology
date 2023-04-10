

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.logout.postOnly = false
grails.plugin.springsecurity.userLookup.userDomainClassName = 'mixology.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'mixology.UserRole'
grails.plugin.springsecurity.authority.className = 'mixology.Role'

grails.plugin.springsecurity.providerNames = [
		'daoAuthenticationProvider',
		'anonymousAuthenticationProvider',
		'rememberMeAuthenticationProvider']

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/user/index',	 access: ['ROLE_ADMIN']],
	[pattern: '/user/show',      access: ['permitAll']],
	[pattern: '/user/create',    access: ['permitAll']],
	[pattern: '/user/save',      access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/drink/**',       access: ['permitAll']],
	//[pattern: '/drink/create',   access: ['ROLE_USER']],
	[pattern: '/ingredient/**',  access: ['permitAll']],
	//[pattern: '/ingredient/create',access: ['ROLE_USER']],
	[pattern: '/secure/index',   access: ['ROLE_ADMIN']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

