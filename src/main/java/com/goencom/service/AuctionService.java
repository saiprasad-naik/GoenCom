package com.goencom.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goencom.dao.BidRepository;
import com.goencom.dao.ResultRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Result;

@Service
public class AuctionService {
	
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private BidRepository bidRepository;
	
	public void result(int auctionId) {
		List<Bid> bids = bidRepository.findBidsbyAuctionId(auctionId);
		bids = highestBids(bids);
		Bid bid = bids.get(0);
		if (bids.size() != 1) {
			bid = lowestBidId(bids);
		}
		bid.getAuction().setStatus(Auction.FINNISHED);
		bid.setStatus(Bid.WON_STATUS);
		Result result = new Result();
		bid.setResult(result);
		result.setBid(bid);
		resultRepository.save(result);
		System.out.println(bids);
	}
	
	private List<Bid> highestBids(List<Bid> bids){
		int max = 0;
		for ( int i = 0; i < bids.size(); i++) {
			if(bids.get(i).getAmount() > max) {
				max = bids.get(i).getAmount();
			}
		}
		List<Bid> results = new ArrayList<>();
		for(int i = 0; i < bids.size(); i++) {
			if(bids.get(i).getAmount() == max) {
				results.add(bids.get(i));
			}
		}
		return results;
	}
	
	private Bid lowestBidId(List<Bid> bids){
		Bid min = bids.get(0);
		
		for (int i = 0; i < bids.size(); i++) {
			if(bids.get(i).getBidId() < min.getBidId()) {
				min = bids.get(i);
			}
		}
		
		return min;
	}
}
