package enums

enum Role {

    USER('ROLE_USER'),
    ADMIN('ROLE_ADMIN')

    String name
    Role(String name) {
        this.name = name
    }
}