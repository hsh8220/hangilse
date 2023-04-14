package com.erp.hangilse.client.controller;

import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.client.service.ClientService;
import com.erp.hangilse.global.CommonResponse;
import com.erp.hangilse.project.controller.ProjectDTO;
import com.erp.hangilse.project.controller.ProjectPageRequest;
import com.erp.hangilse.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/search")
    public ResponseEntity<Page<Client>> getClientByFilter(@RequestBody ClientDTO.clientFilterInfoDTO dto,
                                                            ProjectPageRequest pageRequest) {

        Page<Client> projects = clientService.getClientByFilter(dto, pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projects);
    }

    @GetMapping
    public ResponseEntity<Page<Client>> getClientAll(ClientPageRequest pageRequest) {

        Page<Client> clients = clientService.getClientAll(pageRequest.of());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {

        Client client = clientService.getClientById(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(client);
    }

    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody ClientDTO.createClientDTO dto) {

        Client client = clientService.saveClient(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(client);
    }

    @PutMapping
    public ResponseEntity<Client> updateClient(@RequestBody ClientDTO.updateClientDTO dto) {

        Client client = clientService.updateClient(dto);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse>deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);

        CommonResponse response = CommonResponse.builder()
                .code("Client DELETE")
                .status(200)
                .message("Client DELETE ID : "+ id)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
