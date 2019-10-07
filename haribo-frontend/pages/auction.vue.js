var auctionView = Vue.component('AuctionView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="경매 참여하기" description="경매 중인 작품을 보여줍니다."></v-breadcrumb>
            <div id="auction-list" class="container">
                <div class="row">
                    <div class="col-md-12 text-right">
                        <router-link :to="{ name: 'auction.regsiter' }" class="btn btn-outline-secondary">경매 생성하기</router-link>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 auction" v-for="item in pageAuction">
                        <div class="card">
                            <div class="card-body">
                                <img :src="item['작품정보']['작품이미지']">
                                <h4>{{ item['작품정보']['이름'] }}</h4>
                                <p>{{ calculateDate(item['종료일시']) }}</p>
                                <router-link :to="{ name: 'auction.detail', params: { id: item['id'] }}" class="btn btn-block btn-secondary">자세히보기</router-link>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 text-center">
                        <nav class="bottom-pagination">
                            <ul class="pagination">
                                <li class="page-item" :class="{disabled:currentPage == 1}"><a class="page-link" @click="movePage(1)">◀</a></li>
                                <li class="page-item" :class="{disabled:currentPage == 1}"><a class="page-link" @click="prevPage"><</a></li>
                                <li class="page-item" v-for="idx in pageCount"><a class="page-link" href="#" @click="movePage(idx)">{{idx}}</a></li>
                                <li class="page-item" :class="{disabled:currentPage == pageCount}"><a class="page-link" @click="nextPage">></a></li>
                                <li class="page-item" :class="{disabled:currentPage == pageCount}"><a class="page-link" @click="movePage(pageCount)">▶</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            auctions: [],
            pageAuction:[],
            pageCount: 0,
            perPage:8,
            currentPage: 1
        }
    },
    methods: {
        calculateDate(date) {
            var now = new Date();
            var endDate = new Date(date);
            var diff = Math.floor((Date.parse(endDate) - now) / 1000);
            // 만약 종료일자가 지났다면 "경매 마감"을 표시한다.
            if(diff < 0) {
                return "경매 마감";
            } else {
                var year = 0
                var month = 0
                var day = 0
                var hour = 0
                var minute = 0
                if(Math.floor(diff / 31536000) > 1){
                  year = Math.floor(diff / 31536000)
                  diff = Math.floor(diff % 31536000)
                }
                if(Math.floor(diff / 2592000) > 1){
                  month = Math.floor(diff / 2592000)
                  diff = Math.floor(diff % 2592000)
                }
                if(Math.floor(diff / 86400) > 1){
                  day = Math.floor(diff / 86400)
                  diff = Math.floor(diff % 86400)
                }
                if(Math.floor(diff / 3600) > 1){
                  hour = Math.floor(diff / 3600)
                  diff = Math.floor(diff % 3600)
                }
                if( Math.floor(diff / 60) > 1){
                  minute =  Math.floor(diff / 60);
                  diff =  Math.floor(diff % 60);
                }
                return  "마감까지 " + month + "월 " + day + "일 " + hour + "시 " + minute + "분 " + diff + "초 남았습니다.";
            }
        },
        nextPage(){
          this.currentPage++;
          this.movePage(this.currentPage);

        },
        prevPage(){
          this.currentPage--;
          this.movePage(this.currentPage);

        },
        movePage(p){
          this.currentPage = p;
          var start = (p-1)*this.perPage
          var end = this.auctions.length;
          if(end>start+this.perPage){
            end = start+this.perPage
          }
          this.pageAuction = [];
          for(var i = start; i<end; i++){
            this.pageAuction.push(this.auctions[i])
          }
        }
    },
    mounted: function(){
        var scope = this;

        auctionService.findAll(function(data){
            var result = data;

            // 각 경매별 작품 정보를 불러온다.
            function fetchData(start, end){
                if(start == end) {
                    scope.auctions = result;
                    scope.pageCount = Math.ceil(result.length /scope.perPage);
                    scope.movePage(1);
                } else {
                    var id = result[start]['경매작품id'];
                    workService.findById(id, function(work){
                        result[start]['작품정보'] = work;
                        fetchData(start+1, end);
                    });
                }
            }

            if(result != undefined){
              fetchData(0, result.length);
            }
        });
    }
});
