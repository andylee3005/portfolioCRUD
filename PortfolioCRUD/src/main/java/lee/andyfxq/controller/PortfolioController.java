package lee.andyfxq.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lee.andyfxq.model.Portfolio;
import lee.andyfxq.model.Stock;
import lee.andyfxq.service.PortfolioService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/PORT")
public class PortfolioController {
	
	@Autowired
	@Qualifier("portfService")
	PortfolioService portfService;
	
	@GetMapping("/list")
	@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Portfolio> requestPortfolios() {
		return portfService.getAll();
	}
	
	@GetMapping("/id/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Optional<Portfolio> requestById(@PathVariable String id) {
		return portfService.getById(id);
	}
	
	@GetMapping("/cid/{cid}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Portfolio> requestByClientId(@PathVariable String cid) {
		return portfService.getByClientId(cid);
	}
	
	@PostMapping("/create")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Portfolio> createPortfolio(@RequestBody Portfolio portf) {
		try {
			Portfolio _portf = portfService._save(new Portfolio(
					portf.getClientId(), portf.getpName(), portf.getCurrency()
					));
			return new ResponseEntity<>(_portf, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Portfolio> editPortfolio(@PathVariable String id, @RequestBody Portfolio portf) {
		Optional<Portfolio> portfData = portfService.getById(id);
		
		if (portfData.isPresent()) {
			Portfolio _portf = portfData.get();
			_portf.setpName(portf.getpName());
			_portf.setCurrency(portf.getCurrency());
//			_portf.setStocks(portf.getStocks());
			List<Stock> newStocks = portf.getStocks();
			List<Stock> oldStocks = _portf.getStocks();
			if (newStocks.size() == 0) {
				_portf.setStocks(newStocks);
			} else {
				HashMap<String, Stock> oldSpotIds = new HashMap<>();
				for (Stock oldStock: oldStocks) {
					oldSpotIds.put(oldStock.getSpotId(), oldStock);
				}
				for (int i=0; i<newStocks.size(); i++) {
					Stock curr = newStocks.get(i);
					if (!oldSpotIds.containsKey(curr.getSpotId())) {
						Stock newStock = new Stock(curr.getSpotId(), curr.getSymbol(), curr.getCurrency(), curr.getVolume(), curr.getPrice());
						newStocks.remove(i);
						newStocks.add(newStock);
					}
				}
				_portf.setStocks(newStocks);
				
			}
			_portf.setUpdated(Portfolio.dtf.format(LocalDateTime.now()));
			
			return new ResponseEntity<>(portfService._save(_portf), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/edit/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<HttpStatus> deletePortfolio(@PathVariable String id) {
		try {
			portfService.removeById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
