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

package org.activiti.rest.service.api.history;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tijs Rademakers
 */
@RestController
public class HistoricProcessInstanceIdentityLinkCollectionResource
{
    
    @Autowired
    protected RestResponseFactory restResponseFactory;
    
    @Autowired
    protected HistoryService historyService;
    
    @RequestMapping(value = "/history/historic-process-instances/{processInstanceId}/identitylinks", method = RequestMethod.GET, produces = "application/json")
    public List<HistoricIdentityLinkResponse> getProcessIdentityLinks(@PathVariable
    String processInstanceId, HttpServletRequest request)
    {
        
        List<HistoricIdentityLink> identityLinks =
            historyService.getHistoricIdentityLinksForProcessInstance(processInstanceId);
        
        String serverRootUrl = request.getRequestURL().toString();
        serverRootUrl = serverRootUrl.substring(0, serverRootUrl.indexOf("/history/historic-process-instances/"));
        List<HistoricIdentityLinkResponse> responseList = new ArrayList<HistoricIdentityLinkResponse>();
        if (identityLinks != null)
        {
            for (HistoricIdentityLink instance : identityLinks)
            {
                responseList.add(restResponseFactory.createHistoricIdentityLinkResponse(instance, serverRootUrl));
            }
        }
        
        return responseList;
    }
}