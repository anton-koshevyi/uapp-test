package com.uapp_llc.dto.task;

import com.uapp_llc.dto.column.ColumnDto
import java.util.Date

class TaskDto(

    var id: Long,
    var createdAt: Date,
    var name: String,
    var description: String,
    var index: Int,
    var column: ColumnDto

)
