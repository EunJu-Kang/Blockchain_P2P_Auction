var explorerAuctionView = Vue.component('ExplorerView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Auction Explorer" description="블록체인에 기록된 경매내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Auction</th>
                                <th>Status</th>
                                <th>Highest Bid</th>
                                <th>Highest Bidder</th>
                                <th>Expire Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in items">
                                <td><router-link :to="{ name: 'explorer.auction.detail', params: { contractAddress: item } }">{{ item.컨트랙트주소 | truncate(15) }}</router-link></td>
                                <td>
                                    <span class="badge badge-primary" v-if="!item.경매상태">Processing</span>
                                    <span class="badge badge-danger" v-else>Ended</span>
                                </td>
                                <td v-if="item.최고가 != null ">{{ item.최고가 }} ETH</td>
                                <td v-else>0 ETH</td>
                                <td>
                                    <span>{{ item.최고입찰자 }}</span>

                                </td>
                                <td>{{ item.종료일시 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    `,
    data(){
        return {
            contracts: [],
            items: []
        }
    },
    mounted: async function(){
         var scope = this;
         auctionService.findAll(function(data){
             var result = data;
             var arr = []
             scope.contracts = result;
             for( var idxs = 0; idxs<scope.contracts.length; idxs++){
               let temp_contract = result[idxs].컨트랙트주소
               let temp_endTime = result[idxs].종료일시
               let temp_startTime = result[idxs].시작일시
               let temp_workId = result[idxs].경매작품id
                auction_info(data[idxs].컨트랙트주소, (bid, bidder, endtime, auctionEndTime) => {
                 if(bid){
                   bid = web3.utils.fromWei(bid, 'ether');
                   var cnt = 0
                   for(var i=0; i<bidder.length; i++){
                     if(bidder[i] == "0"){
                       cnt = cnt + 1
                     }
                   }
                   if(cnt >= 30){
                     bidder = "-"
                   }
                 }

                 var obj = {
                   "최고가" : bid,
                   "최고입찰자" : bidder,
                   "경매종료시간" : auctionEndTime,
                   "경매상태" : endtime,
                   "컨트랙트주소" : temp_contract,
                   "종료일시" : temp_endTime,
                   "시작일시" : temp_startTime,
                   "작품id" : temp_workId,
                 }

                 if (bidder != "-") {
                    auctionService.highestBidder(bidder, function(data) {
                    obj["최고입찰자"]  = data;
                    arr.push(obj);
                   });
                 } else  {
                   arr.push(obj);
                 }
               });
               scope.items = arr
             }
         });
    }
})
