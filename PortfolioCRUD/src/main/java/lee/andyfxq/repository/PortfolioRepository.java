package lee.andyfxq.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import lee.andyfxq.model.Portfolio;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

	List<Portfolio> findByClientId(String id);
	
}
