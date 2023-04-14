package com.erp.hangilse.client.service;

import com.erp.hangilse.client.controller.ClientDTO;
import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.client.domain.repository.ClientQueryRepository;
import com.erp.hangilse.client.domain.repository.ClientRepository;
import com.erp.hangilse.global.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientQueryRepository clientQueryRepository;
    private final TagService tagService;

    public Page<Client> getClientAll(Pageable pageable) {
        return this.clientRepository.findAll(pageable);
    }

    public Page<Client> getClientByFilter(ClientDTO.clientFilterInfoDTO dto, Pageable pageable) {
        return this.clientQueryRepository.findClientDynamicQuery(dto, pageable);
    }

    public Client getClientById(Long id) {
        return this.clientRepository.findById(id).get();
    }

    @Transactional
    public Client saveClient(ClientDTO.createClientDTO dto) {
        Client client = Client.builder()
                .name(dto.getName())
                .type(dto.getType())
                .createTime(LocalDate.now())
                .build();

        if(dto.getAddress() != null) {
            client.setAddress(dto.getAddress());
        }
        if(dto.getContents() != null) {
            client.setContents(dto.getContents());
        }
        if(dto.getTags() != null) {
            client.setTags(tagService.saveTags(dto.getTags()));
        }

        return this.clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(ClientDTO.updateClientDTO dto) {
        Client client = this.getClientById(dto.getId());
        client.setUpdateTime(LocalDate.now());

        if(dto.getName() != null) {
            client.setName(dto.getName());
        }
        if(dto.getType() != null) {
            client.setType(dto.getType());
        }
        if(dto.getAddress() != null) {
            client.setAddress(dto.getAddress());
        }
        if(dto.getContents() != null) {
            client.setContents(dto.getContents());
        }
        if(dto.getTags() != null) {
            client.setTags(tagService.saveTags(dto.getTags()));
        }

        return this.clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
