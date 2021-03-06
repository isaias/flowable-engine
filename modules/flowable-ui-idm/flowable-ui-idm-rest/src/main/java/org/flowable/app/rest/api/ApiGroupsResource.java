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
package org.flowable.app.rest.api;

import java.util.ArrayList;
import java.util.List;

import org.flowable.app.idm.service.GroupService;
import org.flowable.app.model.common.GroupRepresentation;
import org.flowable.idm.api.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGroupsResource {
  
  @Autowired
  protected GroupService groupService;
  
  @RequestMapping(value = "/idm/groups", method = RequestMethod.GET, produces = {"application/json"})
  public List<GroupRepresentation> findUsersByFilter(@RequestParam("filter") String filter) {
    List<GroupRepresentation> result = new ArrayList<GroupRepresentation>();
    List<Group> groups = groupService.getGroups(filter);
    for (Group group : groups) {
      result.add(new GroupRepresentation(group));
    }
    return result;
  }

}
