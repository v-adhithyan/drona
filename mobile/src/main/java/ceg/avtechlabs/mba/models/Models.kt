package ceg.avtechlabs.mba.models

data class FeedObject(val title: String, val description: String)

data class FavObject(val title: String, val content: String,
                     val date: String, val imageUrl: String)