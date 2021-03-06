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

package org.activiti.rest.service.api.identity;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.rest.exception.ActivitiConflictException;
import org.activiti.rest.service.api.RestResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Frederik Heremans
 */
@RestController
public class UserInfoCollectionResource extends BaseUserResource
{
    
    @Autowired
    protected RestResponseFactory restResponseFactory;
    
    @Autowired
    protected IdentityService identityService;
    
    @RequestMapping(value = "/identity/users/{userId}/info", method = RequestMethod.GET, produces = "application/json")
    public List<UserInfoResponse> getUserInfo(@PathVariable
    String userId, HttpServletRequest request)
    {
        User user = getUserFromRequest(userId);
        
        List<UserInfoResponse> responses = new ArrayList<UserInfoResponse>();
        String serverRootUrl = request.getRequestURL().toString();
        serverRootUrl = serverRootUrl.substring(0, serverRootUrl.indexOf("/identity/users/"));
        // Create responses for all keys,not including value as this is exposed through the individual resource URL.
        for (String key : identityService.getUserInfoKeys(user.getId()))
        {
            responses.add(restResponseFactory.createUserInfoResponse(key, null, user.getId(), serverRootUrl));
        }
        
        return responses;
    }
    
    @RequestMapping(value = "/identity/users/{userId}/info", method = RequestMethod.POST, produces = "application/json")
    public UserInfoResponse setUserInfo(@PathVariable
    String userId, @RequestBody
    UserInfoRequest userRequest, HttpServletRequest request, HttpServletResponse response)
    {
        
        User user = getUserFromRequest(userId);
        
        if (userRequest.getKey() == null)
        {
            throw new ActivitiIllegalArgumentException("The key cannot be null.");
        }
        if (userRequest.getValue() == null)
        {
            throw new ActivitiIllegalArgumentException("The value cannot be null.");
        }
        
        String existingValue = identityService.getUserInfo(user.getId(), userRequest.getKey());
        if (existingValue != null)
        {
            throw new ActivitiConflictException("User info with key '" + userRequest.getKey()
                + "' already exists for this user.");
        }
        
        identityService.setUserInfo(user.getId(), userRequest.getKey(), userRequest.getValue());
        
        response.setStatus(HttpStatus.CREATED.value());
        String serverRootUrl = request.getRequestURL().toString();
        serverRootUrl = serverRootUrl.substring(0, serverRootUrl.indexOf("/identity/users/"));
        return restResponseFactory.createUserInfoResponse(userRequest.getKey(),
            userRequest.getValue(),
            user.getId(),
            serverRootUrl);
    }
}
