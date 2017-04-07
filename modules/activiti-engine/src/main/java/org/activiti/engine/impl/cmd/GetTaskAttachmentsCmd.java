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

package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Attachment;

/**
 * @author Tom Baeyens
 */
public class GetTaskAttachmentsCmd implements Command<List<Attachment>>, Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    protected String taskId;
    
    public GetTaskAttachmentsCmd(String taskId)
    {
        this.taskId = taskId;
    }
    
    public List<Attachment> execute(CommandContext commandContext)
    {
        return commandContext.getAttachmentEntityManager().findAttachmentsByTaskId(taskId);
    }
}
