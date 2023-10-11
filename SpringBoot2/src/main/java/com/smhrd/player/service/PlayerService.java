package com.smhrd.player.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.smhrd.player.converter.ImageToBase64;
import com.smhrd.player.converter.PlayerImageConverter;
import com.smhrd.player.entity.Player;
import com.smhrd.player.mapper.PlayerMapper;

@Service
public class PlayerService {

	@Autowired
	private PlayerMapper playerMapper;

	@Autowired
	private ResourceLoader resourceLoader; // 특정 경로에 있는 파일을 불러오기위한 객체

	public JSONArray PlayerList() {
		List<Player> p_list = playerMapper.PlayerList();

		JSONArray jsonPlayerList = new JSONArray();

		// DB에서 가지고 온 선수의 정보 중에서 imgSrc는 실제경로, 실제파일이 아님
		// React 서버에 이미지를 직접 넘겨야 하기 때문에 이미지 데이터 가공
		// imgSrc 파일 이름 -> 실제 파일 -> 이미지(byte형태의 문자열)

		PlayerImageConverter<File, String> converter = new ImageToBase64();
		for (Player p : p_list) {
			// 1. 파일이름 -> 실제파일 -> byte문자열
			// 1-1. 파일이름 -> 실제 파일
			// classpath : src/main/resources
			/* String filePath = "classpath:/static/player_img/" + p.getImgSrc(); */
			String filePath = "static/player_img/" + p.getImgSrc();
			System.out.println(filePath);

			// 1-2. 실제 파일로 불러오기
			// Resource resource = resourceLoader.getResource(filePath);
			File TempFile = null;
			String fileString = null;
			try {
				// 배포 시 파일로 불러오면 파일을 못찾기 때문에 inputStream으로 진행
				InputStream inputStream = new ClassPathResource(filePath).getInputStream();
				TempFile = File.createTempFile(p.getImgSrc(), "");
				FileUtils.copyInputStreamToFile(inputStream, TempFile);
				// 1-3. converter를 통해서 byte문자열로 변환
				fileString = converter.convert(TempFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println("fileString 확인 : " + fileString);

			// 2. imgSrc 필드 : 원래 파일 이름 -> byte 문자열
			p.setImgSrc(fileString);

			// 3. Json 형태로 변환(JsonArray)

			jsonPlayerList.add(p);
		}

		return jsonPlayerList;
	}

}
