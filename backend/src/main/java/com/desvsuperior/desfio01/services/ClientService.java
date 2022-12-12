package com.desvsuperior.desfio01.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desvsuperior.desfio01.dto.ClientDTO;
import com.desvsuperior.desfio01.entities.Client;
import com.desvsuperior.desfio01.repositories.ClientRepository;
import com.desvsuperior.desfio01.services.exceptions.DatabaseException;
import com.desvsuperior.desfio01.services.exceptions.ResourceNotFoundException;


@Service
public class ClientService {

	
	@Autowired
	private ClientRepository repository;
	
	//Importa do Spring, não do JavaX
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest){
		Page<Client> list = repository.findAll(pageRequest);
		
		return list.map(x -> new ClientDTO(x));	
		
		
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		//Importar o Optional do java.util
		//O optional é pra evitar objetos nulos
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
		
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {

		Client entity = new Client();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		//O findById toca no banco de dados e traz o dado pra você, o getOne não toca. Instancia um objeto
		// provisório, com o id no objeto
		
		try {
		Client entity = repository.getOne(id);
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
		}
		catch (EntityNotFoundException e){
			throw new ResourceNotFoundException("Id not found: " + id);
		}
		
	}

	//Não coloca o transactional, porque queremos capturar uma exceção do banco de dados
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found: " + id);
		}
		
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	public void copyDtoToEntity(ClientDTO cliDto, Client entity) {
		entity.setName(cliDto.getName());
		entity.setCpf(cliDto.getCpf());
		entity.setIncome(cliDto.getIncome());
		entity.setChildren(cliDto.getChildren());
		entity.setBirthDate(cliDto.getBirthDate());
		
		
	}
}
