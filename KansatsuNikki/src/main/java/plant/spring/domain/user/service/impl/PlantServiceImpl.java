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
	
	@Override
	public int deletePlant(Integer id) {
		return mapper.updateDelOne(id);
	}

	@Override
	public int getUserId(Integer id) {
		return mapper.findOneUserId(id);
	}
	
	@Override
	public Plants getPlant(Integer id) {
		return mapper.findOne(id);
	}
	
	@Override
	public int editPlant(Plants plant) {
		return mapper.updateOne(plant);
	}
	
	
}
