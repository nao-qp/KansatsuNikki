package plant.spring.domain.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plant.spring.domain.user.model.Plants;
import plant.spring.domain.user.service.PlantService;
import plant.spring.repository.PlantMapper;

@Service
public class PlantServiceImpl implements PlantService {

	@Autowired
	private PlantMapper mapper;
	
	@Override
	public Integer getCount(Integer id) {
		return mapper.getCount(id);
	}
	
	@Override
	public List<Plants> findMany(Integer id) {
		return mapper.findMany(id);
	}
	
	@Override
	public int addPlant(Plants plant) {
		return mapper.insertOne(plant);
	}
}
