package com.vitorfg8.quizia.core.domain.repository

/** Reports whether the on-device model can serve requests on this device. */
interface OnDeviceModelRepository {
    suspend fun isAvailable(): Boolean
}
