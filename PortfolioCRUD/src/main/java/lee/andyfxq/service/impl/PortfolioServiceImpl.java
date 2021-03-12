package lee.andyfxq.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lee.andyfxq.model.Portfolio;
import lee.andyfxq.repository.PortfolioRepository;
import lee.andyfxq.service.PortfolioService;

@Service("portfService")
public class PortfolioServiceImpl implements PortfolioService {

	@Autowired
	PortfolioRepository portRepo;
	
	@Override
	public List<Portfolio> getAll() {
		return portRepo.findAll();
	}

	@Override
	public Optional<Portfolio> getById(String id) {
		return portRepo.findById(id);
	}

	@Override
	public List<Portfolio> getByClientId(String id) {
		return portRepo.findByClientId(id);
	}

	@Override
	public Portfolio _save(Portfolio portfolio) {
		return portRepo.save(portfolio);
	}

	@Override
	public void removeById(String id) {
		portRepo.deleteById(id);
	}

}
