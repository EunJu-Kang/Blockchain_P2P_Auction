var auctionDetailView = Vue.component('AuctionDetailView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 작품 상세 정보" description="선택하신 경매 작품의 상세 정보를 보여줍니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="card-body">
                              <div class="row">
                                <div class="col-md-4">
                                  <h5 class="text-secondary korean-font">작품 이미지</h5>
                                  <span>
                                    <img :src="work['작품이미지']" data-toggle="modal" data-target="#myModal" @click="clickImg(work['작품이미지'])">
                                    <v-img></v-img>
                                  </span>
                                </div>
                                <div class="col-md-8">
                                  <table class="table table-bordered korean-font">
                                      <tr>
                                          <th width="25%">생성자</th>
                                          <td><router-link :to="{ name: 'work.by_user', params: { id: creator['id'] } }">{{ creator['이름'] }}({{creator['이메일']}})</router-link></td>
                                      </tr>
                                      <tr>
                                          <th>작품명</th>
                                          <td>{{ work['이름'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>작품 설명</th>
                                          <td>{{ work['설명'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>경매 시작일</th>
                                          <td>{{ auction['경매시작시간'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>경매 종료일</th>
                                          <td>{{ auction['경매종료시간'] }}</td>
                                      </tr>
                                      <tr>
                                          <th>최저가</th>
                                          <td><strong>{{ auction['최소금액'] }} ETH</strong></td>
                                      </tr>
                                      <tr>
                                          <th>컨트랙트 주소</th>
                                          <td><a href="#">{{ auction['경매컨트랙트주소'] }}</a></td>
                                      </tr>
                                      <tr>
                                          <th>상태</th>
                                          <td>
                                            <span class="badge badge-blue" v-if="auction['종료'] == false">경매 진행중</span>
                                            <span class="badge badge-navy" v-if="auction['종료'] == true">경매 종료</span>
                                          </td>
                                      </tr>
                                  </table>
                                </div>
                              </div>
                                <table class="table table-bordered mt-3 korean-font" v-if="bidder.id">
                                    <tr>
                                        <th width="20%">현재 최고 입찰자</th>
                                        <td>{{ bidder['이름'] }}({{ bidder['이메일'] }})</td>
                                    </tr>
                                    <tr>
                                        <th>현재 최고 입찰액</th>
                                        <td>{{ auction['최고입찰액'] }} ETH</td>
                                    </tr>
                                </table>
                                <div class="alert alert-warning mt-3 korean-font" role="alert" v-if="!bidder.id">
                                    입찰 내역이 없습니다.
                                </div>
                                <div class="alert alert-danger mt-3 korean-font" role="alert" v-if="auction['종료'] == true">
                                    경매가 종료되었습니다.
                                </div>
                                <div class="row mt-5">
                                    <div class="col-md-6">
                                        <router-link :to="{ name: 'auction' }" class="btn btn-sm btn-outline-secondary korean-font">경매 리스트로 돌아가기</router-link>
                                    </div>
                                    <div class="col-md-6 text-right korean-font" v-if="sharedStates.user.id == work['회원id'] && auction['종료'] != true">
                                        <button type="button" class="btn btn-sm btn-primary korean-font" v-on:click="closeAuction" v-bind:disabled="isCanceling || isClosing">{{ isClosing ? "낙찰중" : "낙찰하기" }}</button>
                                        <button type="button" class="btn btn-sm btn-danger korean-font" v-on:click="cancelAuction" v-bind:disabled="isCanceling || isClosing">{{ isCanceling ? "취소하는 중" : "경매취소하기" }}</button>
                                    </div>
                                    <div class="col-md-6 text-right korean-font" v-if="sharedStates.user.id != work['회원id'] && auction['종료'] != true" v-show="check">
                                        <router-link :to="{ name: 'auction.bid', params: { id: this.$route.params.id } }" class="btn btn-sm btn-warning" v-if="!endBid">입찰하기</router-link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            work: {},
            creator: {id:0},
            auction: {},
            sharedStates: store.state,
            bidder: {},
            isCanceling: false,
            isClosing: false,
            check:false,
            endBid:false
        }
    },
    methods: {
        closeAuction: function(){
            var scope = this;
            var privateKey = window.prompt("경매를 종료하시려면 지갑 비밀키를 입력해주세요.","");
            var options = {
                contractAddress: this.auction['경매컨트랙트주소'],
                privateKey: privateKey
            };
            auction_close(options, function(receipt){
              var auctionId = scope.$route.params.id;
              var bidderId = scope.bidder.id;

              if(receipt.cumulativeGasUsed == 2000000){
                alert("경매 종료를 실패했습니다.")
              } else {
              auctionService.close(auctionId, bidderId, function(success){
                    alert("경매가 종료되었습니다.");
                    scope.auction['종료'] = true;
              })
            }
            });
        },
        cancelAuction: function(){
            var scope = this;
            var privateKey = window.prompt("경매를 취소하시려면 지갑 비밀키를 입력해주세요.","");
            var options = {
                contractAddress: this.auction['경매컨트랙트주소'],
                privateKey: privateKey
            };
            auction_cancel(options, function(receipt){
              var auctionId = scope.$route.params.id;
              var bidderId = scope.sharedStates.user.id;

              if(receipt.cumulativeGasUsed == 2000000){
                alert("경매 취소를 실패했습니다.")
              } else {
              auctionService.cancel(auctionId, bidderId,
                 function(){
                    alert("경매가 취소되었습니다.");
                    scope.auction['종료'] = true;
                 })
                }
            });
        },
        calculateDate(date) {
            var now = new Date();
            var endDate = new Date(date);
            var diff = Math.floor((Date.parse(endDate) - now) / 1000);
            if(diff < 0) {
              this.endBid = true;
            }
        },
        clickImg(data){
          $('#myModal').on('show.bs.modal', function(e) {
            $("#imgStr").attr("src", data);
          });
        }
    },
    mounted: async function(){
        var auctionId = this.$route.params.id;
        var scope = this;
        var web3 = createWeb3();

        auctionService.findById(auctionId, function(auction){
            var amount = Number(auction['최소금액']).toLocaleString().split(",").join("")
            var workId = auction['작품id'];
            if(auction != null ){
              scope.check = true
            }
            workService.findById(workId, function(work){
                scope.work = work;
                var creatorId = work['회원id'];
                userService.findById(creatorId, function(user){
                    scope.creator = user;
                });
            });
            if(auction['최고입찰액'] > 0) {
                var amount = Number(auction['최고입찰액']).toLocaleString().split(",").join("")
                auction['최고입찰액'] = web3.utils.fromWei(amount, 'ether');
                var bidderId = auction['최고입찰자id'];

                userService.findById(bidderId, function(user){
                    scope.bidder = user;
                });
            }
            scope.auction = auction;
            scope.calculateDate(scope.auction['경매종료시간'])
        });
    }
});
