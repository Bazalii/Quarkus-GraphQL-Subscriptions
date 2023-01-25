package org.acme

import io.smallrye.graphql.api.Context
import io.smallrye.graphql.api.Subscription
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor
import org.eclipse.microprofile.graphql.*
import java.util.*
import javax.enterprise.context.RequestScoped
import kotlin.collections.ArrayList

@GraphQLApi
@RequestScoped
class UserController {
    private var _users = ArrayList<User>()

    companion object {
        var BroadcastProcessor: BroadcastProcessor<User> =
            io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor.create()
    }

    @Query
    fun getUser(id: UUID): User {
        return _users.first { user -> user.id == id }
    }

    @Mutation("addUser")
    fun addUser(user: User): User {
        _users.add(user)
        BroadcastProcessor.onNext(user)

        return user
    }

    @Subscription
    fun subscribeToUsersWithFilter(context: Context, token: String, userName: String): Multi<User>? {
        if (token != "123") {
            return Multi.createFrom().failure(TokenValidationException("Invalid token!"))
        }

        return BroadcastProcessor.filter { user -> user!!.name == userName && isValid(token) }
    }

    @Subscription
    fun subscribeToUsers(): Multi<User> {
        return BroadcastProcessor
    }

    private fun isValid(token: String): Boolean {
        if (token != "123") {
            throw TokenValidationException("Invalid token!")
        }

        return true
    }
}