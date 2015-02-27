/*******************************************************************************
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package com.bonitasoft.engine.bdm.validator;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bonitasoft.engine.bdm.model.NamedElement;

/**
 * @author Colin PUY
 * @deprecated from version 7.0.0 on, use {@link org.bonitasoft.engine.bdm.validator.UniqueNameValidator} instead.
 */
@Deprecated
public class UniqueNameValidator {

    public ValidationStatus validate(Collection<? extends NamedElement> namedElements, String namedElementTypePluralForm) {
        ValidationStatus status = new ValidationStatus();
        Set<String> duplicateNames = findDuplicateNames(namedElements);
        for (String name : duplicateNames) {
            status.addError("There are at least two " + namedElementTypePluralForm + " with the same name : " + name);
        }
        return status;
    }

    private Set<String> findDuplicateNames(Collection<? extends NamedElement> list) {
        Set<String> duplicates = new LinkedHashSet<String>();
        Set<String> uniqueNames = new HashSet<String>();

        for (NamedElement t : list) {
            if (!uniqueNames.add(t.getName())) {
                duplicates.add(t.getName());
            }
        }

        return duplicates;
    }
}
