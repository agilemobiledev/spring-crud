package com.github.lemniscate.spring.crud.svc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Identifiable;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author dave 8/8/14 9:52 PM
 */
public interface ApiResourceService<ID extends Serializable, E extends Identifiable<ID>, CB, RB, UB> {

    /* ************************************************************************************************
     * DEVELOPER NOTE: if you add methods here, add supporting methods to ApiResourceServices as well *
     * ***********************************************************************************************/

    E findOne(ID id);

    Page<E> find(Pageable p);

    Page<RB> findForRead(Pageable p);

    Page<E> query(MultiValueMap<String, String> params, Pageable p);

    Page<RB> queryForRead(MultiValueMap<String, String> params, Pageable p);

    List<E> findByIds(Iterable<ID> ids);

    E save(E entity);

    E create(CB bean);

    RB createForRead(CB bean);

    RB read(ID id);

    E update(ID id, UB bean);

    RB updateForRead(ID id, UB bean);

    E delete(ID id);

    List<E> delete(Iterable<ID> ids);

    E delete(E entity);

    Page<E> search(Map<String, Object> search, Pageable pageable);
    Page<RB> searchForRead(Map<String, Object> search, Pageable pageable);

    /* ************************************************************************************************
     * DEVELOPER NOTE: if you add methods here, add supporting methods to ApiResourceServices as well *
     * ***********************************************************************************************/

}
