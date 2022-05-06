package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;



public interface PlayerService {
    Page<Player> getAll(Integer pageNo, Integer pageSize, String fieldOrder, Filter filter);
    Player getById(Long id);
    void save(Player player);
    void delete(Long id);
    Integer count(Integer pageNo, Integer pageSize, String fieldOrder, Filter filter);
    Long getTotal();
}
