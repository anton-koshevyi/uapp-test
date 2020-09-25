package com.uapp_llc.mapper;

import com.uapp_llc.dto.column.ColumnDto;
import com.uapp_llc.model.Column;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-24T23:27:25+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_265 (Private Build)"
)
public class ColumnMapperImpl implements ColumnMapper {

    @Override
    public ColumnDto toDto(Column model) {
        if ( model == null ) {
            return null;
        }

        ColumnDto columnDto = new ColumnDto();

        columnDto.setId( model.getId() );
        columnDto.setName( model.getName() );

        return columnDto;
    }
}
