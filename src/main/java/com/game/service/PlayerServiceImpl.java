package com.game.service;

import com.game.entity.Player;

import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService{

    private PlayerRepository repository;

    public Integer count(Integer pageNo, Integer pageSize, String fieldOrder, Filter filter){
        if (pageNo == null) pageNo = 0;
        if (pageSize == null) pageSize = 3;
        return (int)repository.findAll(SpecificationCreator.createSpecification(filter),PageRequest.of(pageNo, pageSize,Sort.by(fieldOrder))).getTotalElements();
    }

    @Autowired
    public PlayerServiceImpl(PlayerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Player> getAll(Integer pageNo, Integer pageSize, String fieldOrder, Filter filter) {
        if (pageNo == null) pageNo = 0;
        if (pageSize == null) pageSize = 3;
        return repository.findAll(SpecificationCreator.createSpecification(filter),PageRequest.of(pageNo, pageSize,Sort.by(fieldOrder)));
    }

    @Override
    public Player getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void save(Player player) {
        repository.save(player);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Long getTotal() {
        return repository.count();
    }
}
