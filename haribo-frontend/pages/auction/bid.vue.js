/**
 * 화면: 경매 입찰하기
 */

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
                                <div class="col-md-6">
                                    <button class="btn btn-sm btn-primary" v-on:click="bid" v-bind:disabled="bidding">{{ bidding ? "입찰을 진행 하는 중입니다." : "입찰하기" }}</button>
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
            wallet: {}
        }
    },
    methods: {
        goBack: function(){
            this.$router.go(-1);
        },
        bid: function(){
            /**
             * 컨트랙트를 호출하여 입찰하고
             * 입찰 정보 등록 API를 호출합니다. 
             */
            var scope = this;

            var options = {
                amount: this.input.price,
                contractAddress: this.auction['경매컨트랙트주소'],
                walletAddress: this.wallet['주소'],
                privateKey: this.input.privateKey
            };
            console.log(options);
            this.bidding = true;

            // 컨트랙트 bid 함수를 호출합니다.
            // components/auctionFactory.js의 auction_bid 함수를 호출합니다.
            // TODO auction_bid 함수의 내용을 완성합니다.             
            auction_bid(options, function(receipt){
                var bidder = scope.sharedStates.user.id;
                var auctionId = scope.$route.params.id;
                
                // 입찰 정보 등록 요청 API를 호출합니다. 
                auctionService.saveBid(bidder, auctionId, options.amount, function(result){
                    alert("입찰이 완료되었습니다.");
                    scope.bidding = false;
                    scope.$router.go(-1);
                });
            });
        }
    },
    mounted: function(){
        var scope = this;
        var auctionId = this.$route.params.id;

        auctionService.findById(auctionId, function(auction){
            auction['최소금액'] = Number(auction['최소금액']) / (10**18);
            scope.auction = auction;
            var workId = auction['작품id'];

            workService.findById(workId, function(work){
                scope.work = work;
            });
        });

        // 내 지갑 정보 조회
        walletService.findById(scope.sharedStates.user.id, function(wallet){
            wallet['잔액'] = Number(wallet['잔액']) / (10 ** 18);
            scope.wallet = wallet;
        });
    }
})