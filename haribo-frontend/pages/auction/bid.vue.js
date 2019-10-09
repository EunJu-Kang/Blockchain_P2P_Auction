var auctionBidView = Vue.component('AuctionBidView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 입찰하기" description="선택한 경매에 입찰을 합니다."></v-breadcrumb>
            <div class="row">
                <div class="col-md-4 mx-auto">
                    <div class="card">
                        <div class="card-header">경매 입찰하기</div>
                        <div class="card-body">
                            <div class="form-group">
                                <label id="privateKey"><b>입찰 대상 작품</b></label><br>
                                {{ work['이름'] }}
                            </div>
                            <div class="form-group">
                                <label id="privateKey"><b>내 지갑 잔액</b></label><br>
                                {{ wallet['잔액'] }} ETH
                            </div>
                            <div class="form-group">
                                <label id="privateKey"><b>지갑 개인키</b></label>
                                <input id="privateKey" v-model="input.privateKey" type="text" class="form-control" placeholder="지갑 개인키를 입력해주세요.">
                            </div>
                            <div class="form-group">
                                <label id="price"><b>입찰금액</b></label>
                                <div class="input-group">
                                    <input id="price" v-model="input.price" type="text" class="form-control" placeholder="입찰 금액을 입력해주세요.">
                                    <div class="input-group-append">
                                        <div class="input-group-text">ETH</div>
                                    </div>
                                </div><br>
                                <div class="alert alert-warning" role="alert">
                                    최소 입찰 금액은 {{ auction['최소금액'] }} ETH 입니다.
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6" v-show="check">
                                    <button class="btn btn-sm btn-primary" v-on:click="bid" :disabled="bidding">{{ bidding ? "입찰을 진행 하는 중입니다." : "입찰하기" }}</button>
                                </div>
                                <div class="col-md-6 text-right">
                                    <button class="btn btn-sm btn-outline-secondary" v-on:click="goBack">이전으로 돌아가기</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            bidding: false,
            work: {},
            auction: {},
            input: {
                privateKey: '',
                price: 0
            },
            sharedStates: store.state,
            wallet: {},
            check: false
        }
    },
    methods: {
        goBack: function(){
            this.$router.go(-1);
        },
        bid: function(){
            var scope = this;
            var options = {
                amount: scope.input.price,
                contractAddress: scope.auction['경매컨트랙트주소'],
                walletAddress: scope.wallet['주소'],
                privateKey: scope.input.privateKey
            }
            this.bidding = true;

            auction_bid(options, function(receipt){
                var bidder = scope.sharedStates.user.id;
                var auctionId = scope.$route.params.id;

                if(receipt.cumulativeGasUsed == 3000000){
                    alert("입찰을 실패했습니다.")
                    scope.bidding = false;
                } else {
                auctionService.saveBid(bidder, auctionId, options.amount, function(result){
                    alert("입찰이 완료되었습니다.");
                    scope.bidding = false;
                    scope.$router.go(-1);
                });
               }
            });
        }
    },
    mounted: function(){
        var scope = this;
        var auctionId = this.$route.params.id;
        auctionService.findById(auctionId, function(auction){
            auction['최소금액'] = Number(auction['최소금액']);
            scope.auction = auction
            if(auction != null ){
              scope.check = true
            }
            var workId = auction['작품id'];
            workService.findById(workId, function(work){
                scope.work = work;
            });
        });
        walletService.findById(scope.sharedStates.user.id, function(wallet){
            wallet['잔액'] = Number(wallet['잔액']) ;
            scope.wallet = wallet;
        });
    }
})
