package com.example.data.mappers

import com.example.data.local.ContactEntity
import com.example.data.remote.models.ContactDto
import com.example.data.remote.models.Results
import com.example.domain.model.Contact

// Mapping extensions from Dto to Entity
fun ContactDto.toContactEntities(): List<ContactEntity> =
    results.map { result ->
        result.toContactEntity()
    }

fun Results.toContactEntity(): ContactEntity =
    ContactEntity(
        gender = gender,
        name = name?.toNameEntity(),
        location = location?.toLocationEntity(),
        email = email,
        login = login?.toLoginEntity(),
        dob = dob?.toDobEntity(),
        registered = registered?.toRegisteredEntity(),
        phone = phone,
        cell = cell,
        id = id?.toIdEntity(),
        picture = picture?.toPictureEntity(),
        nat = nat,
    )

fun Results.Name.toNameEntity(): ContactEntity.Name = ContactEntity.Name(title, first, last)

fun Results.Street.toStreetEntity(): ContactEntity.Street = ContactEntity.Street(number, name)

fun Results.Coordinates.toCoordinatesEntity(): ContactEntity.Coordinates = ContactEntity.Coordinates(latitude, longitude)

fun Results.Timezone.toTimezoneEntity(): ContactEntity.Timezone = ContactEntity.Timezone(offset, description)

fun Results.Location.toLocationEntity(): ContactEntity.Location =
    ContactEntity.Location(
        street = street?.toStreetEntity(),
        city = city,
        state = state,
        country = country,
        postcode = postcode,
        coordinates = coordinates?.toCoordinatesEntity(),
        timezone = timezone?.toTimezoneEntity(),
    )

fun Results.Login.toLoginEntity(): ContactEntity.Login = ContactEntity.Login(uuid, username, password, salt, md5, sha1, sha256)

fun Results.Dob.toDobEntity(): ContactEntity.Dob = ContactEntity.Dob(date, age)

fun Results.Registered.toRegisteredEntity(): ContactEntity.Registered = ContactEntity.Registered(date, age)

fun Results.Id.toIdEntity(): ContactEntity.Id = ContactEntity.Id(name, value)

fun Results.Picture.toPictureEntity(): ContactEntity.Picture = ContactEntity.Picture(large, medium, thumbnail)

fun ContactEntity.toContact(): Contact =
    Contact(
        index = index,
        gender = gender,
        name = name?.toName(),
        location = location?.toLocation(),
        email = email,
        login = login?.toLogin(),
        dob = dob?.toDob(),
        registered = registered?.toRegistered(),
        phone = phone,
        cell = cell,
        id = id?.toId(),
        picture = picture?.toPicture(),
        nat = nat,
    )

fun ContactEntity.Name.toName(): Contact.Name = Contact.Name(title, first, last)

fun ContactEntity.Street.toStreet(): Contact.Street = Contact.Street(number, name)

fun ContactEntity.Coordinates.toCoordinates(): Contact.Coordinates = Contact.Coordinates(latitude, longitude)

fun ContactEntity.Timezone.toTimezone(): Contact.Timezone = Contact.Timezone(offset, description)

fun ContactEntity.Location.toLocation(): Contact.Location =
    Contact.Location(
        street = street?.toStreet(),
        city = city,
        state = state,
        country = country,
        postcode = postcode,
        coordinates = coordinates?.toCoordinates(),
        timezone = timezone?.toTimezone(),
    )

fun ContactEntity.Login.toLogin(): Contact.Login = Contact.Login(uuid, username, password, salt, md5, sha1, sha256)

fun ContactEntity.Dob.toDob(): Contact.Dob = Contact.Dob(date, age)

fun ContactEntity.Registered.toRegistered(): Contact.Registered = Contact.Registered(date, age)

fun ContactEntity.Id.toId(): Contact.Id = Contact.Id(name, value)

fun ContactEntity.Picture.toPicture(): Contact.Picture = Contact.Picture(large, medium, thumbnail)
