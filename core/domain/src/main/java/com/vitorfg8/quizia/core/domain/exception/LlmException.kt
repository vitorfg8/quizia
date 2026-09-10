package com.vitorfg8.quizia.core.domain.exception

import com.vitorfg8.quizia.core.domain.model.LlmProviderType

/** Failures a [com.vitorfg8.quizia.core.domain.repository.LlmProvider] can report to the UI. */
sealed class LlmException(message: String) : Exception(message) {

    /** The user picked a provider that needs a key but never entered one. */
    class MissingApiKey(val provider: LlmProviderType) :
        LlmException("No API key is stored for $provider.")

    /** The provider answered, but with nothing usable. */
    class EmptyResponse(val provider: LlmProviderType) :
        LlmException("$provider returned an empty response.")

    /** The provider answered, but the payload does not satisfy the quiz contract. */
    class InvalidResponse(val provider: LlmProviderType) :
        LlmException("$provider returned a response that is not a valid quiz.")

    /** Gemini Nano is not usable on this device. */
    class OnDeviceModelUnavailable :
        LlmException("Gemini Nano is not available on this device.")
}
