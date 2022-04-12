package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import java.util.List;

public interface PrivilegeService {

    public List<Privilege> findPrivilegesById(List<Long> idList);
}
