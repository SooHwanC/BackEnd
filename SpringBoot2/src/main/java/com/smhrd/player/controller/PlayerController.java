package com.smhrd.player.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.player.service.PlayerService;

@RestController // 리액트 서버로 데이터만 응답
@CrossOrigin("http://54.180.100.253:3000")
public class PlayerController {

	@Autowired
	private PlayerService playerService;

	@RequestMapping("/PlayerList.do")
	public JSONArray PlayerList() {
		System.out.println("스프링 : PlayerList.do");
		// 리액트 서버로 json데이터 응답
		return playerService.PlayerList();
	}

	@RequestMapping("PlayerDetail.do")
	public JSONObject PlayerDetail(@RequestParam String name) {
		System.out.println("이름 확인 : " + name);
		
		// DB에서 나상호의 정보만 가지고와서 리액트로 전송
		// JsonObject -> put 메서드 활용
		
		return null;
	}

}
