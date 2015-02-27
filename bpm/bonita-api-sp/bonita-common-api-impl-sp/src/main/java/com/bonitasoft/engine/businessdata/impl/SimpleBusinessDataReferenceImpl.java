/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package com.bonitasoft.engine.businessdata.impl;

import com.bonitasoft.engine.businessdata.SimpleBusinessDataReference;

/**
 * @author Matthieu Chaffotte
 * @deprecated since version 7.0.0, use {@link org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl}
 */
@Deprecated
public class SimpleBusinessDataReferenceImpl extends org.bonitasoft.engine.business.data.impl.SimpleBusinessDataReferenceImpl implements SimpleBusinessDataReference {

    private static final long serialVersionUID = -434357449996998735L;

    public SimpleBusinessDataReferenceImpl(org.bonitasoft.engine.business.data.SimpleBusinessDataReference dataReference) {
        super(dataReference.getName(), dataReference.getType(), dataReference.getStorageId());
    }

}
