package plant.spring.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
