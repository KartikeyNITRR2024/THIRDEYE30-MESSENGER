package com.thirdeye3.messenger.services.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messenger.dtos.Group;
import com.thirdeye3.messenger.exceptions.GroupException;
import com.thirdeye3.messenger.externalcontrollers.UserManagerClient;
import com.thirdeye3.messenger.services.GroupService;
import com.thirdeye3.messenger.dtos.Response;

@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);
    
    @Autowired
    private UserManagerClient userManager;
    
	private ConcurrentMap<Long, Group> groups = null;
	
	 @Override
	 public void fetchGroups() {
		 
		 Response<Map<Long, Group>> response = userManager.getAllGroups();
		 if(response.isSuccess())
		 { 
			 groups = new ConcurrentHashMap<>();
			 response.getResponse().forEach((gropId, group) -> {
				 groups.put(gropId, group);
		     });
		    logger.info("Groups {}",groups);
		 }
		 else
		 {
			 logger.error("Failed to fetch group");
	         throw new GroupException("Unable to fetch groups from Users Manager");
		 }
	 }
	 
	 @Override
	 public Map<Long, Group> getGroups()
	 {
		 if(groups == null)
		 {
			 fetchGroups();
		 }
		 return groups;
	 }
	 
	 @Override
	 public Group getGroup(Long groupId)
	 {
		 if(groups == null)
		 {
			 fetchGroups();
		 }
		 if(!groups.containsKey(groupId))
		 {
			 throw new GroupException("User not found");
		 }
		 return groups.get(groupId);
	 }
	 
	 @Override
	 public void updateOrAddGroup(Group group)
	 {
		 if(group == null)
		 {
			 fetchGroups();
		 }
		 if(group == null || group.getTelegramChatIds() == null || group.getTelegramChatIds().isEmpty())
		 {
			 logger.info("Removing group with id {}",group.getId());
			 groups.remove(group.getId());
		 }
		 else
		 {
			 logger.info("Updating group with id {}",group.getId());
			 groups.put(group.getId(), group);
		 }
	 }
	 
}
