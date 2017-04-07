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

package org.activiti.rest.service.api.runtime.task;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Event;
import org.activiti.rest.service.api.engine.EventResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Frederik Heremans
 */
@RestController
public class TaskEventCollectionResource extends TaskBaseResource
{
    
    @RequestMapping(value = "/runtime/tasks/{taskId}/events", method = RequestMethod.GET, produces = "application/json")
    public List<EventResponse> getEvents(@PathVariable
    String taskId, HttpServletRequest request)
    {
        List<EventResponse> result = new ArrayList<EventResponse>();
        HistoricTaskInstance task = getHistoricTaskFromRequest(taskId);
        
        String serverRootUrl = request.getRequestURL().toString();
        serverRootUrl = serverRootUrl.substring(0, serverRootUrl.indexOf("/runtime/tasks/"));
        
        for (Event event : taskService.getTaskEvents(task.getId()))
        {
            result.add(restResponseFactory.createEventResponse(event, serverRootUrl));
        }
        
        return result;
    }
}
