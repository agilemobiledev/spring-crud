package com.github.lemniscate.spring.crud.web.assembler;

import com.github.lemniscate.spring.crud.mapping.ApiResourceControllerHandlerMapping;
import com.github.lemniscate.spring.crud.mapping.ApiResourceMapping;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional
public class ApiResourceAssembler<ID extends Serializable, E extends Identifiable<ID>, CB, RB extends Identifiable<ID>, UB>
        extends ResourceAssemblerSupport<RB, Resource<RB>> {

    @Inject
    protected EntityLinks entityLinks;

    @Getter @Setter
    protected ApiResourceMapping<ID, E, CB, RB, UB> mapping;

    @Inject
    protected ApiResourceControllerHandlerMapping handlerMapping;

    @Inject
    protected ApiResourceLinkBuilderFactory factory;

    public ApiResourceAssembler() {
        super( determineParam(3, 3), (Class<Resource<RB>>) (Class<?>) Resource.class);
    }

    @Override
    public Resource<RB> toResource(RB bean) {
        Collection<Link> links = new ArrayList<Link>();
        doAddLinks(links, bean);
        return new Resource<RB>(bean, links);
    }

    /**
     * Adds a reference to self, then calls {@link ApiResourceAssembler#addLinks(java.util.Collection, org.springframework.hateoas.Identifiable)}
     * so implementations can customize links.
     */
    private void doAddLinks(Collection<Link> links, RB bean) {
        Link link = entityLinks.linkToSingleResource(bean).withSelfRel();
        links.add(link);
        addLinks(links, bean);
    }

    public void addLinks(Collection<Link> links, RB bean) {
//        AssemblerFieldHelper helper = new AssemblerFieldHelper(beanLinks, arLinkBuilder, beanLookup, links, bean);
//        ReflectionUtils.doWithFields(bean.getClass(), helper, helper);

        List<ApiResourceControllerHandlerMapping.PathPropertyMapping> list = handlerMapping.getAssembleWith().get(mapping.domainClass());
        for( ApiResourceControllerHandlerMapping.PathPropertyMapping m : list){
            ApiResourceLinkBuilder builder = factory.linkToNestedResource( m.getController(), m.getMethod(), mapping.domainClass(), bean.getId());
            Link link = new Link( builder.toString(), m.getProperty());
            links.add(link);
        }

    }


    // Shield your eyes: this nastiness gets us to have default constructors for concrete Assemblers.
    private static Class<?>  determineParam(int callStackIndex, int paramIndex){
        try {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            Assert.isTrue(st.length >= callStackIndex, "CallStack didn't contain enough elements");
            // the fourth entry should be our concrete class (unless we have some base-classes... crap)
            String name = st[callStackIndex].getClassName();
            Class<?> clazz = Class.forName(name);
            Type result = GenericTypeResolver.resolveTypeArguments(clazz, ApiResourceAssembler.class)[paramIndex];
            return result.getClass();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
