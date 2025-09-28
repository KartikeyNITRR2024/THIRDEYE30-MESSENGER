package com.thirdeye3.messenger.services;

import java.util.Map;

import com.thirdeye3.messenger.dtos.Group;

public interface GroupService {
	void fetchGroups();
	Map<Long, Group> getGroups();
	void updateOrAddGroup(Group group);
	Group getGroup(Long groupId);
}
