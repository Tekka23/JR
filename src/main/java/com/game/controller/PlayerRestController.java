package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.Filter;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/players")
public class PlayerRestController {

    @Autowired
    private PlayerService service;
    private Filter filter;

     @GetMapping()
        public ResponseEntity<List<Player>> getAll(
                                               @Param("name")String name,
                                               @Param("title") String title,
                                               @Param("race") Race race,
                                               @Param("profession") Profession profession,
                                               @Param("after") Long after,
                                               @Param("before")Long before,
                                               @Param("banned") Boolean banned,
                                               @Param("minExperience")Integer minExperience,
                                               @Param("maxExperience") Integer maxExperience,
                                               @Param("minLevel") Integer minLevel,
                                               @Param("maxLevel") Integer maxLevel,
                                               @Param("order") PlayerOrder order,
                                               @Param("pageNumber") Integer pageNumber,
                                               @Param("pageSize")Integer pageSize){
         filter = new Filter();
         filter.setName(name);
         filter.setRace(race);
         filter.setProfession(profession);
         filter.setTitle(title);
         filter.setAfter(after);
         filter.setBefore(before);
         filter.setBanned(banned);
         filter.setMinExperience(minExperience);
         filter.setMaxExperience(maxExperience);
         filter.setMinLevel(minLevel);
         filter.setMaxLevel(maxLevel);
        if (order == null) order = PlayerOrder.ID;
        Page<Player> players = service.getAll(pageNumber,pageSize,order.getFieldName(), filter);
        if (players == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(players.getContent(), HttpStatus.OK);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Player> getPlayerByID(@PathVariable("id") Long id){
        if (id == null || id<=0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = service.getById(id);
        if (player == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(player, HttpStatus.OK);
        }


        @GetMapping("/count")
        public ResponseEntity<Integer> getPlayersCount(
                @Param("name")String name,
                @Param("title") String title,
                @Param("race") Race race,
                @Param("profession") Profession profession,
                @Param("after") Long after,
                @Param("before")Long before,
                @Param("banned") Boolean banned,
                @Param("minExperience")Integer minExperience,
                @Param("maxExperience") Integer maxExperience,
                @Param("minLevel") Integer minLevel,
                @Param("maxLevel") Integer maxLevel,
                @Param("order") PlayerOrder order,
                @Param("pageNumber") Integer pageNumber,
                @Param("pageSize")Integer pageSize)
        {
            filter = new Filter();
            filter.setName(name);
            filter.setRace(race);
            filter.setProfession(profession);
            filter.setTitle(title);
            filter.setAfter(after);
            filter.setBefore(before);
            filter.setBanned(banned);
            filter.setMinExperience(minExperience);
            filter.setMaxExperience(maxExperience);
            filter.setMinLevel(minLevel);
            filter.setMaxLevel(maxLevel);
            if (order == null) order = PlayerOrder.ID;
            return new ResponseEntity<>(service.count(pageNumber,pageSize,order.getFieldName(), filter), HttpStatus.OK);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long playerId){
            if (playerId == null || playerId == 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            Player player = service.getById(playerId);

            if (player == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            service.delete(playerId);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Player> createPlayer(@RequestBody Player jsonPlayer){
        if (jsonPlayer.getName() == null || (jsonPlayer.getName() !=null && (jsonPlayer.getName().trim().isEmpty() || jsonPlayer.getName().length()>12))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getTitle() == null || (jsonPlayer.getTitle() != null && jsonPlayer.getTitle().length()>30)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getRace() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getProfession() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getBirthday() == null || (jsonPlayer.getBirthday() != null && jsonPlayer.getBirthday().getTime()<0)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getExperience() == null || (jsonPlayer.getExperience() != null && (jsonPlayer.getExperience()<0||jsonPlayer.getExperience()>10_000_000))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        jsonPlayer.countLvl();
        jsonPlayer.countUntilNextLvl();
        service.save(jsonPlayer);
        return new ResponseEntity<>(jsonPlayer,HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@RequestBody Player jsonPlayer,
                                               @PathVariable("id") Long playerId){
        Player player = service.getById(playerId);
        if (jsonPlayer.checkFieldsOnNull()) return  new ResponseEntity<>(player,HttpStatus.OK);
        if (playerId == null || playerId <= 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (player == null || playerId > service.getTotal()) return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (jsonPlayer.getName() != null && jsonPlayer.getName().trim().isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getExperience()!=null && (jsonPlayer.getExperience()<0 || jsonPlayer.getExperience() >10_000_000)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (jsonPlayer.getBirthday() != null && jsonPlayer.getBirthday().getTime()< 0) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (jsonPlayer.getName() != null) player.setName(jsonPlayer.getName());
        if (jsonPlayer.getTitle() != null && !jsonPlayer.getTitle().trim().isEmpty()) player.setTitle(jsonPlayer.getTitle());
        if (jsonPlayer.getRace() != null) player.setRace(jsonPlayer.getRace());
        if (jsonPlayer.getProfession() != null) player.setProfession(jsonPlayer.getProfession());
        if (jsonPlayer.getBirthday() != null) player.setBirthday(jsonPlayer.getBirthday());
        if (jsonPlayer.getBanned() != null) player.setBanned(jsonPlayer.getBanned());
        if (jsonPlayer.getExperience() != null) player.setExperience(jsonPlayer.getExperience());
        player.countLvl();
        player.countUntilNextLvl();
        service.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

}
