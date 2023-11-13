package com.ophi.storyapp.pref

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoryModel(
    @PrimaryKey
    val id: String,

    val name: String?,
    val description: String?,
    val photoUrl: String?
): Parcelable
