/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package com.bonitasoft.engine.api.impl.transaction.application;

import java.util.List;

import org.bonitasoft.engine.persistence.QueryOptions;
import org.bonitasoft.engine.persistence.SBonitaReadException;
import org.bonitasoft.engine.persistence.SBonitaReadException;
import org.bonitasoft.engine.search.AbstractSearchEntity;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.descriptor.SearchEntityDescriptor;

import com.bonitasoft.engine.api.impl.convertor.ApplicationConvertor;
import com.bonitasoft.engine.business.application.ApplicationMenu;
import com.bonitasoft.engine.business.application.ApplicationService;
import com.bonitasoft.engine.business.application.model.SApplicationMenu;


/**
 * @author Elias Ricken de Medeiros
 *
 */
public class SearchApplicationMenus extends AbstractSearchEntity<ApplicationMenu, SApplicationMenu> {

    private final ApplicationService applicationService;
    private final ApplicationConvertor convertor;

    public SearchApplicationMenus(final ApplicationService applicationService, final ApplicationConvertor convertor,
            final SearchEntityDescriptor searchDescriptor, final SearchOptions options) {
        super(searchDescriptor, options);
        this.applicationService = applicationService;
        this.convertor = convertor;
    }

    @Override
    public long executeCount(final QueryOptions queryOptions) throws SBonitaReadException {
        return applicationService.getNumberOfApplicationMenus(queryOptions);
    }

    @Override
    public List<SApplicationMenu> executeSearch(final QueryOptions queryOptions) throws SBonitaReadException {
        return applicationService.searchApplicationMenus(queryOptions);
    }

    @Override
    public List<ApplicationMenu> convertToClientObjects(final List<SApplicationMenu> serverObjects) {
        return convertor.toApplicationMenu(serverObjects);
    }

}