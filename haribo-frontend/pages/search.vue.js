var searchView = Vue.component('searchView', {
    template: `
    <div>
        <v-nav></v-nav>
        <div class="container">
        <h3 class="home-margin">" {{ searched }} "으로 검색한 결과입니다.</h3>
        <div class="col-md-12 text-right">
            <router-link to="/" class="btn btn-outline-secondary">홈으로</router-link>
        </div>

            <div id="my-artwork" class="row">
                <div class="col-md-12 mt-5">
                    <h4>Artwork</h4>
                    <div class="row">
                        <div class="col-md-3 artwork" v-for="item in artworks" v-if="artworks.length > 0">
                            <div class="card">
                                <div class="card-body">
                                    <img :src="item['작품이미지']">
                                    <h4>{{ item["이름"] }}</h4>
                                    <p v-if="item['설명'] != null">{{ item["설명"] }}</p>
                                    <p v-if="item['설명'] == null">-</p>
                                    <router-link :to="{ name: 'work.detail', params: { id: item['id'] } }" class="btn btn-block btn-secondary">자세히보기</router-link>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-12 col-md-8 mt-3" v-if="artworks.length == 0">
                            <div class="alert alert-warning">검색된 작품이 없습니다.</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 mt-5">
                    <h4>Auction</h4>
                    <div class="row">
                        <div class="col-md-4 artwork" v-for="item in auctions" v-if="auctions.length > 0">
                            <div class="card">
                                <div class="card-body text-center">
                                    <img :src="item['작품이미지']">
                                    <h4>{{ item['작품정보']['이름'] }}</h4>
                                    <span class="badge badge-success">{{calculateDate(item['종료일시'])}}</span>
                                    <router-link :to="{ name: 'auction.detail', params: { id: item['id'] }}" class="btn btn-block btn-secondary mt-3">자세히보기</router-link>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-12 col-md-8 mt-3" v-if="auctions.length == 0">
                            <div class="alert alert-warning">검색된 경매 목록이 없습니다.</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `,
    data() {
        return {
          sharedStates: store.state,
          artworks: [],
          auctions: [],
          searched: ""
        }
    },
    methods: {
      calculateDate(date) {
          var now = new Date();
          var endDate = new Date(date);
          var diff = Math.floor((Date.parse(endDate) - now) / 1000);

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
      }
    },
    mounted: async function(){
      this.searched = this.$route.params.searched;
      var scope = this;


      if(this.searched !==undefined){
      workService.searchWork(this.searched, function(data) {
          scope.artworks = data;
      });

      auctionService.searchAuction(this.searched, function(data){
        var result = data;

        function fetchData(start, end){
            if(start == end) {
                scope.auctions = result;
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
    }
})
