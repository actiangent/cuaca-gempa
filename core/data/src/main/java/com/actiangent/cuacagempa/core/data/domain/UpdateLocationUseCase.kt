package com.actiangent.cuacagempa.core.data.domain

import com.actiangent.cuacagempa.core.data.repository.location.LocationRepository
import com.actiangent.cuacagempa.core.data.repository.preferences.UserDataRepository
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val userDataRepository: UserDataRepository,
) {

    suspend operator fun invoke() {
        val location = locationRepository.requestUpdatedLocation()
        val address = locationRepository.requestAddress(location)
        userDataRepository.setLatitudeLongitude(location.latitude, location.longitude)
        userDataRepository.setProvinceEndpoint(address?.adminArea)
    }

}