package com.ctyeung.dictatekotlin.room

class DbMapper {

    fun mapVerses(list: List<Verse>): List<Verse> {
        return list.map {
            with(it) {
                Verse(datetime, verse, isSelected)
            }
        }
    }
}