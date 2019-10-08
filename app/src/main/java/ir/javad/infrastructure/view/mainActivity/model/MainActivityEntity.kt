package ir.javad.infrastructure.view.mainActivity.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mainActivityDb")
class MainActivityEntity(@PrimaryKey val id: Int)