package com.src.book.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.src.book.domain.model.search.SearchItem

@Entity(
    tableName = "search_items",
    indices = [Index(
        value = ["name"],
        unique = true
    )]
)
data class SearchItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String
) {
    companion object {
        fun fromModel(searchItem: SearchItem) = SearchItemEntity(
            id = 0,
            name = searchItem.name
        )

        fun toModel(searchItemEntity: SearchItemEntity) = SearchItem(
            id = searchItemEntity.id,
            name = searchItemEntity.name
        )
    }
}
