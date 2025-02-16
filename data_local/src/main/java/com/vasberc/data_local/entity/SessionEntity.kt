package com.vasberc.data_local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey
    val id: Int = 1,
    @ColumnInfo(name = "moves")
    val moves: Int,
    @ColumnInfo(name = "horse_x")
    val horseX: Int?,
    @ColumnInfo(name = "horse_y")
    val horseY: Int?,
    @ColumnInfo(name = "target_x")
    val targetX: Int?,
    @ColumnInfo(name = "target_y")
    val targetY: Int?,
    @ColumnInfo(name = "board_size")
    val boardSize: Int
)