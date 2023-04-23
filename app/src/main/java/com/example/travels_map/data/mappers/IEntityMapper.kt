package com.example.travels_map.data.mappers

fun interface IEntityMapper<EntityFrom, EntityTo> {
    fun mapEntity(entity: EntityFrom): EntityTo
}