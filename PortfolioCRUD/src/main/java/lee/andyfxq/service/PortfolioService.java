package lee.andyfxq.service;

import java.util.List;
import java.util.Optional;

import lee.andyfxq.model.Portfolio;

public interface PortfolioService {

	List<Portfolio> getAll();
	Optional<Portfolio> getById(String id);
	List<Portfolio> getByClientId(String id);
	Portfolio _save(Portfolio portfolio);
	void removeById(String id);
	
}
