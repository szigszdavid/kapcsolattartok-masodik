package hu.futureofmedia.task.contactsapi.controllers;

import graphql.ExecutionResult;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import hu.futureofmedia.task.contactsapi.services.GraphQLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/graphql")
public class GraphQLController {

    private final GraphQLService graphQLService;

    @PostMapping
    public ResponseEntity<Object> requestHandler(@RequestBody String query)
    {
        ExecutionResult executionResult = graphQLService.getGraphQL().execute(query);

        return new ResponseEntity<>(executionResult, HttpStatus.OK);
    }

}
