package plant.spring.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.Diaries;
import plant.spring.domain.user.service.DiaryService;
import plant.spring.repository.DiaryMapper;

@Service
public class DiaryServiceImpl implements DiaryService {

	@Autowired
	private DiaryMapper mapper;

	@Override
	public Integer getCount(Integer id) {
		return mapper.getCount(id);
	}
	
	@Override
	public List<Diaries> getDiaries(Integer id) {
		return mapper.findMany(id);
	}
	
	@Override
	public Integer getUserId(Integer id) {
		return mapper.findOneUserId(id);
	}
	
	@Override
	public Diaries getDiary(Integer id) {
		return mapper.findOne(id);
	}
	
	@Override
	public int addDiary(Diaries diary) {
		return mapper.insertOne(diary);
	}
	
	@Override
	public int deleteDiary(Integer id) {
		return mapper.updateDelOne(id);
	}
}
