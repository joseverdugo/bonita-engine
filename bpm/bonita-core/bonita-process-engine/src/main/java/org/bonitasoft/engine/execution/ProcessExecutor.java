/**
 * Copyright (C) 2011-2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.engine.execution;

import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.bpm.connector.ConnectorDefinitionWithInputValues;
import org.bonitasoft.engine.bpm.connector.ConnectorEvent;
import org.bonitasoft.engine.commons.exceptions.SBonitaException;
import org.bonitasoft.engine.core.connector.ConnectorService;
import org.bonitasoft.engine.core.expression.control.model.SExpressionContext;
import org.bonitasoft.engine.core.operation.model.SOperation;
import org.bonitasoft.engine.core.process.definition.model.SProcessDefinition;
import org.bonitasoft.engine.core.process.instance.api.exceptions.SFlowNodeExecutionException;
import org.bonitasoft.engine.core.process.instance.api.exceptions.SFlowNodeReadException;
import org.bonitasoft.engine.core.process.instance.api.exceptions.SProcessInstanceCreationException;
import org.bonitasoft.engine.core.process.instance.model.SProcessInstance;

/**
 * @author Baptiste Mesta
 * @author Yanyan Liu
 * @author Elias Ricken de Medeiros
 * @author Hongwen Zang
 * @author Celine Souchet
 * @author Matthieu Chaffotte
 */
public interface ProcessExecutor extends ContainerExecutor {

    SProcessInstance start(long processDefinitionId, long targetSFlowNodeDefinitionId, long starterId, long starterDelegateId,
            SExpressionContext expressionContext, List<SOperation> operations, Map<String, Object> context,
            List<ConnectorDefinitionWithInputValues> connectors, long callerId, long subProcessDefinitionId) throws SProcessInstanceCreationException;

    SProcessInstance start(SProcessDefinition processDefinition, long starterId, long starterDelegateId, List<SOperation> operations,
            Map<String, Object> context, List<ConnectorDefinitionWithInputValues> connectorsWithInput) throws SProcessInstanceCreationException;

    SProcessInstance start(SProcessDefinition sProcessDefinition, long targetSFlowNodeDefinitionId, long starterId, long starterDelegateId,
            SExpressionContext expressionContext, List<SOperation> operations, Map<String, Object> context,
            List<ConnectorDefinitionWithInputValues> connectors, long callerId, long subProcessDefinitionId) throws SProcessInstanceCreationException;

    boolean executeConnectors(SProcessDefinition processDefinition, SProcessInstance sInstance, ConnectorEvent activationEvent,
            ConnectorService connectorService) throws SBonitaException;

    SProcessInstance startElements(SProcessDefinition sDefinition, SProcessInstance sInstance) throws SProcessInstanceCreationException,
            SFlowNodeExecutionException, SFlowNodeReadException;

    SProcessInstance startElements(SProcessDefinition sDefinition, SProcessInstance sInstance, long subProcessDefinitionId,
            final long targetSFlowNodeDefinitionId) throws SProcessInstanceCreationException, SFlowNodeExecutionException, SFlowNodeReadException;

    void handleProcessCompletion(final SProcessDefinition sProcessDefinition, final SProcessInstance sProcessInstance, final boolean hasActionsToExecute)
            throws SBonitaException;
}
