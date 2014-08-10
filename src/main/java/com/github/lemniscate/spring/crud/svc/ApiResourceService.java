package com.github.lemniscate.spring.crud.svc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Identifiable;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;

/**
 * @Author dave 8/8/14 9:52 PM
 */
public interface ApiResourceService<ID extends Serializable, E extends Identifiable<ID>, CB, RB, UB> {

    E findOne(ID id);

    Page<E> find(Pageable p);

    Page<RB> findForRead(Pageable p);

    Page<E> query(MultiValueMap<String, String> params, Pageable p);

    Page<RB> queryForRead(MultiValueMap<String, String> params, Pageable p);

    E create(CB bean);

    RB createForRead(CB bean);

    RB read(ID id);

    E update(UB bean);

    RB updateForRead(UB bean);

    void delete(ID id);

    void delete(E entity);



}