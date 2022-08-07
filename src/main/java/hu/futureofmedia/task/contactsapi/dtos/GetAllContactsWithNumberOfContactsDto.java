package hu.futureofmedia.task.contactsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllContactsWithNumberOfContactsDto {

    private List<GetAllContactsDTO> getAllContactsDTOList;

    private Integer numberOfPages;

    private Integer numberOfAllContacts;

    private Integer currentPageNumber;

    private Integer contactsPerPage;

    GetAllContactsWithNumberOfContactsDto(Page<GetAllContactsDTO> page) {

    }
}
