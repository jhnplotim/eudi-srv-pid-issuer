/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.europa.ec.eudi.pidissuer.domain.pid

import arrow.core.nonEmptySetOf
import com.nimbusds.jose.JWSAlgorithm
import eu.europa.ec.eudi.pidissuer.domain.*
import eu.europa.ec.eudi.sdjwt.SdObject
import eu.europa.ec.eudi.sdjwt.sd
import eu.europa.ec.eudi.sdjwt.sdJwt
import kotlinx.serialization.json.put
import java.time.ZonedDateTime

val PidSdJwtVcScope: Scope = Scope("${PID_DOCTYPE}_vc_sd_jwt")

val PidSdJwtVcV1: SdJwtVcMetaData = SdJwtVcMetaData(
    type = SdJwtVcType(pidDocType(1)),
    display = pidDisplay,
    claims = pidAttributes,
    cryptographicBindingMethodsSupported = listOf(CryptographicBindingMethod.Jwk(nonEmptySetOf(JWSAlgorithm.ES256K))),
    scope = PidSdJwtVcScope,
)

fun Pid.asSdObjectAt(iat: ZonedDateTime): SdObject =
    sdJwt {
        sd {
            put("given_name", givenName.value)
            put("family_name", familyName.value)
            put("birth_date", birthDate.toString())
            val age = iat.year - birthDate.get(java.time.temporal.ChronoField.YEAR)
            put("is_over_18", age >= 18)
            ageBirthYear?.let { put("age_birth_year", it.value) }
            put("unique_id", uniqueId.value)
            familyNameBirth?.let { put("family_name_birth", it.value) }
            givenNameBirth?.let { put("given_name_birth", it.value) }
            birthPlace?.let { put("birth_place", it) }
            birthCountry?.let { put("birth_country", it.value) }
            birthState?.let { put("birth_state", it.value) }
            birthCity?.let { put("birth_city", it.value) }
            residentCountry?.let { put("resident_country", it.value) }
            residentState?.let { put("resident_state", it.value) }
            residentCity?.let { put("resident_city", it.value) }
            residentPostalCode?.let { put("resident_postal_code", it.value) }
            residentHouseNumber?.let { put("resident_house_number", it) }
            gender?.let { put("gender", it.value.toInt()) }
            nationality?.let { put("", it.value) }
        }
    }
