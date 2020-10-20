package com.uapp_llc.test.model.wrapper.column

import com.uapp_llc.model.Column
import com.uapp_llc.test.model.wrapper.ModelWrapper

abstract class ColumnWrapper constructor(

    name: String,
    index: Int

) : ModelWrapper<Column>(Column(null, name, index))
