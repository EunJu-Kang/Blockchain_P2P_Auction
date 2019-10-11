var explorerAuctionDetailView = Vue.component('ExplorerDetailView', {
    template: `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Auction Explorer" description="블록체인에 기록된 경매내역을 보여줍니다."></v-breadcrumb>
        <div class="container">
            <explorer-nav></explorer-nav>
            <div class="row">
                <div class="col-md-12">
                    <div class="alert alert-warning" v-if="contract && contract.highestBid == 0">
                        아직 경매에 참여한 이력이 없습니다.
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th colspan="2"># {{ contractAddress }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th width="20%">Contract Address</th>
                                <td>{{ contractAddress }}</td>
                            </tr>
                            <tr>
                                <th width="20%">작품</th>
                                <td><router-link :to="{ name: 'work.detail', params: { id: work['id'] }}">{{ work && work['이름'] }}</router-link></td>
                            </tr>
                            <tr>
                                <th>Status</th>
                                <td>
                                    <span class="badge badge-primary" v-if="contract && !contract.ended">Processing</span>
                                    <span class="badge badge-danger" v-if="contract && contract.ended">Ended</span>
                                </td>
                            </tr>
                            <tr>
                                <th>Start Time Time</th>
                                <td>{{ contract && contract.startTime }}</td>
                            </tr>
                            <tr>
                                <th>Expire Time</th>
                                <td>{{ contract && contract.endTime}}</td>
                            </tr>
                            <tr>
                                <th>Highest Bid</th>
                                <td>{{ contract && contract.highestBid }} ETH</td>
                            </tr>
                            <tr>
                                <th>Highest Bidder</th>
                                <td>
                                    <span v-if="contract && contract.highestBid == 0">-</span>
                                    <span v-if="contract && contract.highestBid != 0">{{ contract && contract.highestBidder }}</span>
                                </td>
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
            contractAddress: "",
            contract: null,
            work: {
                id: 0
            }
        }
    },
    mounted: async function(){
         var scope = this;
         let temp =  this.$route.params.contractAddress;
         scope.contractAddress = temp.컨트랙트주소;
         auction_info(scope.contractAddress, (bid, bidder, endtime, auctionEndTime) => {
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
           let obj = {
             "ended" : endtime,
             "endTime" : temp.종료일시,
             "highestBid" : bid,
             "highestBidder" : bidder,
             "startTime" : temp.시작일시,
           }
           scope.contract = obj
         })
         workService.findById(temp.작품id, function(data){
           scope.work = data
         })
    }
})
