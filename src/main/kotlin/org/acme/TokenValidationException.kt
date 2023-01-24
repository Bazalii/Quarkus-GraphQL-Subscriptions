package org.acme

import io.smallrye.graphql.api.ErrorCode

@ErrorCode("403")
class TokenValidationException(message: String): Exception(message)