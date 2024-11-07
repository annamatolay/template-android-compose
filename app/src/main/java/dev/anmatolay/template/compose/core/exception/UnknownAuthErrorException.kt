package dev.anmatolay.template.compose.core.exception

class UnknownAuthErrorException(isSuccessful: Boolean, isCurrentUserNull: Boolean) :
    RuntimeException(
        "Unknown error occurred during authentication. " +
            "Auth Task successful: $isSuccessful. Is user null: $isCurrentUserNull.",
    )
