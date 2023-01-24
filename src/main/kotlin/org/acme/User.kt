package org.acme

import java.util.*

open class User() {
    var id: UUID = UUID.randomUUID()
    var name: String = ""
    var roles: List<String> = arrayListOf()

    constructor(id: UUID, name: String, roles: List<String>) : this() {
        this.id = id
        this.name = name
        this.roles = roles
    }
}