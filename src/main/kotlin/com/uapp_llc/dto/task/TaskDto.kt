package com.uapp_llc.dto.task;

import java.util.Date
import com.uapp_llc.dto.column.ColumnDto

class TaskDto(

    var id: Long,
    var createdAt: Date,
    var name: String,
    var description: String,
    var index: Int,
    var column: ColumnDto

)
