package com.uapp_llc.mapper;

import com.uapp_llc.dto.task.TaskDto;
import com.uapp_llc.model.Task;
import javax.annotation.Generated;
import org.mapstruct.factory.Mappers;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-09-24T23:27:26+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_265 (Private Build)"
)
public class TaskMapperImpl implements TaskMapper {

    private final ColumnMapper columnMapper = Mappers.getMapper( ColumnMapper.class );

    @Override
    public TaskDto toDto(Task model) {
        if ( model == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( model.getId() );
        taskDto.setCreatedAt( model.getCreatedAt() );
        taskDto.setName( model.getName() );
        taskDto.setDescription( model.getDescription() );
        taskDto.setColumn( columnMapper.toDto( model.getColumn() ) );

        return taskDto;
    }
}
