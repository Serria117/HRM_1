package com.hrm.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapperUtils
{
    @Autowired
    ModelMapper mapper;

    public <S, D> List<D> mapToList(Collection<S> s, Class<D> d){
        return s.stream()
                .map( x -> mapper.map(x, d))
                .collect(Collectors.toList());
    }

    public <S, D> Set<D> mapToSet(Collection<S> s, Class<D> d){
        return s.stream()
                .map( x -> mapper.map(x, d))
                .collect(Collectors.toSet());
    }

    public ModelMapper getMapper()
    {
        return this.mapper;
    }
}
