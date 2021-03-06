package org.flowable.engine.impl;

import java.util.List;
import java.util.Map;

import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.interceptor.CommandExecutor;
import org.flowable.engine.runtime.NativeProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;

public class NativeProcessInstanceQueryImpl extends AbstractNativeQuery<NativeProcessInstanceQuery, ProcessInstance> implements NativeProcessInstanceQuery {

  private static final long serialVersionUID = 1L;

  public NativeProcessInstanceQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }

  public NativeProcessInstanceQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  // results ////////////////////////////////////////////////////////////////

  public List<ProcessInstance> executeList(CommandContext commandContext, Map<String, Object> parameterMap, int firstResult, int maxResults) {
    return commandContext.getExecutionEntityManager().findProcessInstanceByNativeQuery(parameterMap, firstResult, maxResults);
  }

  public long executeCount(CommandContext commandContext, Map<String, Object> parameterMap) {
    return commandContext.getExecutionEntityManager()
    // can use execution count, since the result type doesn't matter
        .findExecutionCountByNativeQuery(parameterMap);
  }

}
