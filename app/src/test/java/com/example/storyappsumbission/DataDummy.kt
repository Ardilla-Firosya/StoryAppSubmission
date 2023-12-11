package com.example.storyappsumbission

import com.example.storyappsumbission.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                id = "id_$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://images.app.goo.gl/v4swd7pJvzrhzDuq8",
                createdAt = "2021-01-22T22:22:22Z",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 5
            )
            items.add(quote)
        }
        return items
    }
}