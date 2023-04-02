package enums

enum Role {

    USER('ROLE_USER'),
    ADMIN('ROLE_AMIN')

    String name
    Role(String name) {
        this.name = name
    }
}