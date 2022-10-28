package com.manuelguerrero.listtvshows.models


data class Cast(
    val character: Character,
    val person: Person,
    val self: Boolean,
    val voice: Boolean
) {
    data class Character(
        val _links: Links,
        val id: Int,
        val image: Any?,
        val name: String,
        val url: String
    ) {
        data class Links(
            val self: Self
        ) {
            data class Self(
                val href: String
            )
        }
    }

    data class Person(
        val _links: Links,
        val birthday: String?,
        val country: Country?,
        val deathday: Any?,
        val gender: String,
        val id: Int,
        val image: Image,
        val name: String,
        val updated: Int,
        val url: String
    ) {
        data class Links(
            val self: Self
        ) {
            data class Self(
                val href: String
            )
        }

        data class Country(
            val code: String,
            val name: String,
            val timezone: String
        )

        data class Image(
            val medium: String,
            val original: String
        )
    }
}