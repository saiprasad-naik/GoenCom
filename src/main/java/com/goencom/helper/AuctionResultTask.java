package com.goencom.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.goencom.dao.AuctionRepository;
import com.goencom.dao.BidRepository;
import com.goencom.dao.ResultRepository;
import com.goencom.entities.Auction;
import com.goencom.entities.Bid;
import com.goencom.entities.Result;
import com.goencom.service.AuctionService;

public class AuctionResultTask implements Runnable {

	private int auctionId;
	@Autowired
	private AuctionService auctionService;
	/*
	 * @Autowired private ResultRepository resultRepository;
	 * 
	 * @Autowired private BidRepository bidRepository;
	 */

	/*
	 * public AuctionResultTask(int auctionId, BidRepository bidRepository,
	 * ResultRepository resultRepository) { this.auctionId = auctionId;
	 * this.bidRepository = bidRepository; this.resultRepository = resultRepository;
	 * }
	 */

	@Override
	public void run() {
		auctionService.result(this.auctionId);
		/*
		 * List<Bid> bids = bidRepository.findBidsbyAuctionId(this.auctionId); bids =
		 * highestBids(bids); Bid bid = bids.get(0); if (bids.size() != 1) { bid =
		 * lowestBidId(bids); } bid = bidRepository.findById(bid.getBidId()).get();
		 * Result result = new Result(); bid.setResult(result); result.setBid(bid);
		 * resultRepository.save(result); System.out.println(bids);
		 */
		
		
	} 

	public int getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public AuctionResultTask() {
		super();
	}

	/*
	 * private List<Bid> highestBids(List<Bid> bids){ int max = 0; for ( int i = 0;
	 * i < bids.size(); i++) { if(bids.get(i).getAmount() > max) { max =
	 * bids.get(i).getAmount(); } } List<Bid> results = new ArrayList<>(); for(int i
	 * = 0; i < bids.size(); i++) { if(bids.get(i).getAmount() == max) {
	 * results.add(bids.get(i)); } } return results; }
	 * 
	 * private Bid lowestBidId(List<Bid> bids){ Bid min = bids.get(0);
	 * 
	 * for (int i = 0; i < bids.size(); i++) { if(bids.get(i).getBidId() <
	 * min.getBidId()) { min = bids.get(i); } }
	 * 
	 * return min; }
	 */
}
