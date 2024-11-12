package com.example.horse_attila.model

data class SquareModel(
    val id: Int,
    val x_position: Int,
    val y_position: Int,
    var is_horse: Boolean = false,
    var is_king: Boolean = false,
    var is_on_fire: Boolean = false,
    var is_horse_visited : Boolean = false
)