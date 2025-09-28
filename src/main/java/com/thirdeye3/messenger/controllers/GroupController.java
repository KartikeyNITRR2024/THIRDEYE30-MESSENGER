package com.thirdeye3.messenger.controllers;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.dtos.Group;
import com.thirdeye3.messenger.services.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me/update/messsenger")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping()
    public Response<Boolean> updateOrAddGroup(
            @RequestBody Group group) {
    	groupService.updateOrAddGroup(group);
        return new Response<>(true, 0, null, true);
    }
}
