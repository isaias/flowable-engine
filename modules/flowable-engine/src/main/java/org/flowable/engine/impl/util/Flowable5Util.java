/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.engine.impl.util;

import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.compatibility.Flowable5CompatibilityHandler;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.impl.interceptor.Command;
import org.flowable.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * @author Joram Barrez
 * @author Tijs Rademakers
 */
public class Flowable5Util {
  
  public static boolean isFlowable5ProcessDefinitionId(CommandContext commandContext, final String processDefinitionId) {
    
    if (processDefinitionId == null) {
      return false;
    }
    
    try {
      ProcessDefinition processDefinition = ProcessDefinitionUtil.getProcessDefinition(processDefinitionId);
      if (processDefinition == null) {
        return false;
      }
      return isFlowable5ProcessDefinition(commandContext, processDefinition);
    } catch (FlowableObjectNotFoundException e) {
      return false;
    }
  }
  
  /**
   * Use this method when running outside a {@link CommandContext}. 
   * It will check the cache first and only start a new {@link CommandContext} when no result is found in the cache. 
   */
  public static boolean isFlowable5ProcessDefinitionId(final ProcessEngineConfigurationImpl processEngineConfiguration, final String processDefinitionId) {
    
    if (processDefinitionId == null) {
      return false;
    }
    
    if (!processEngineConfiguration.isFlowable5CompatibilityEnabled()) {
      return false;
    }
    
    ProcessDefinitionCacheEntry cacheEntry = processEngineConfiguration.getProcessDefinitionCache().get(processDefinitionId);
    if (cacheEntry != null) {
      ProcessDefinition processDefinition = cacheEntry.getProcessDefinition();
      return processEngineConfiguration.getFlowable5CompatibilityHandler().isVersion5Tag(processDefinition.getEngineVersion());
    } else {
      return processEngineConfiguration.getCommandExecutor().execute(new Command<Boolean>() {
        
        @Override
        public Boolean execute(CommandContext commandContext) {
          return isFlowable5ProcessDefinitionId(commandContext, processDefinitionId);
        }
        
      });
      
    }
  }
  
  
  public static boolean isFlowable5ProcessDefinition(CommandContext commandContext, ProcessDefinition processDefinition) {
    
    if (!commandContext.getProcessEngineConfiguration().isFlowable5CompatibilityEnabled()) {
      return false;
    }
    
    if (processDefinition.getEngineVersion() != null) {
      if (commandContext.getProcessEngineConfiguration().getFlowable5CompatibilityHandler().isVersion5Tag(processDefinition.getEngineVersion())) {
        if (commandContext.getProcessEngineConfiguration().isFlowable5CompatibilityEnabled()) {
          return true;
        }
      } else {
        throw new FlowableException("Invalid 'engine' for process definition " + processDefinition.getId() + " : " + processDefinition.getEngineVersion());
      }
    }
    return false;
  }
  
  public static Flowable5CompatibilityHandler getFlowable5CompatibilityHandler() {
    Flowable5CompatibilityHandler flowable5CompatibilityHandler = Context.getFlowable5CompatibilityHandler();
    if (flowable5CompatibilityHandler == null) {
      flowable5CompatibilityHandler = Context.getFallbackFlowable5CompatibilityHandler();
    }
    
    if (flowable5CompatibilityHandler == null) {
      throw new FlowableException("Found Flowable 5 process definition, but no compatibility handler on the classpath");
    }
    return flowable5CompatibilityHandler;
  }

}
