package org.xpb.controller;


import cn.hutool.core.lang.Dict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xpb.common.Result;
import org.xpb.domain.Consumer;
import org.xpb.domain.Singer;
import org.xpb.domain.Song;
import org.xpb.domain.SongList;
import org.xpb.service.ConsumerService;
import org.xpb.service.SingerService;
import org.xpb.service.SongListService;
import org.xpb.service.SongService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 统计数据的通用接口Controller
 */
@RestController
@RequestMapping("/countData")
public class CountDataController {
    @Resource
    ConsumerService consumerService;
    @Resource
    SingerService singerService;
    @Resource
    SongService songService;
    @Resource
    SongListService songListService;
    @GetMapping("/charts")
    public Result charts() {
        //统计用户总数
        List<Consumer> listConsumer = consumerService.list();
        List<Singer> listSinger = singerService.list();
        List<Song> listSong = songService.list();
        List<SongList> listSongList = songListService.list();
        //统计总数
        Dict dict = Dict.create();
        Dict Num = dict.set("consumerCount",listConsumer.size())
                       .set("singerCount",listSinger.size())
                       .set("songCount",listSong.size())
                       .set("songListCount",listSongList.size());
        //用户性别统计
        List<Dict> pieList = new ArrayList<>();
        List<String> Genders = listConsumer.stream().map(Consumer::getGender).distinct().collect(Collectors.toList());
        for (String gender : Genders) {
            int count = 0;
            for (Consumer consumer : listConsumer) {
                if (gender.equals(consumer.getGender())) {
                    count++;
                }
            }
            Dict dict1 = Dict.create();
            Dict pie = dict1.set("name", gender).set("value",count);
            pieList.add(pie);
        }
        //歌手性别统计
        List<Dict> pieList1 = new ArrayList<>();
        List<String> SingerGenders = listSinger.stream().map(Singer::getGender).distinct().collect(Collectors.toList());
        for (String gender : SingerGenders) {
            int count = 0;
            for (Singer singer : listSinger) {
                if (gender.equals(singer.getGender())) {
                    count++;
                }
            }
            Dict dict1 = Dict.create();
            Dict pie = dict1.set("name", gender).set("value",count);
            pieList1.add(pie);
        }
        //歌曲风格统计
        List<Dict> barList = new ArrayList<>();
        List<String> styles = listSongList.stream().map(SongList::getStyle).distinct().collect(Collectors.toList());
        for (String style : styles) {
            int count = 0;
            for (SongList songList : listSongList) {
                if (style.equals(songList.getStyle())) {
                    count++;
                }
            }
            Dict dict2 = Dict.create();
            Dict bar = dict2.set("category", style).set("value",count);
            barList.add(bar);
        }
        //歌手国籍统计
        List<Dict> barList1 = new ArrayList<>();
        List<String> SingAddress = listSinger.stream().map(Singer::getLocation).distinct().collect(Collectors.toList());
        System.out.println(SingAddress);
        for (String location : SingAddress) {
            int count = 0;
            for (Singer singer : listSinger) {
                if (location.equals(singer.getLocation())) {
                    count++;
                }
            }
            Dict dict2 = Dict.create();
            Dict bar = dict2.set("category", location).set("value",count);
            barList1.add(bar);
        }
        Dict res = Dict.create().set("Num",Num).set("ConsumerGender",pieList).set("SingerGender",pieList1)
                .set("Style",barList).set("Address",barList1);
        return Result.success(res);
    }
}
