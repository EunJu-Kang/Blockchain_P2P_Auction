var searchView = Vue.component('searchView', {
    template: `
    <div>
        <v-nav></v-nav>
        <div class="container">
        <h3 class="home-margin">" {{ searched }} "으로 검색한 결과입니다.</h3>

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
                        <div class="col-md-3 artwork" v-for="item in auctions" v-if="auctions.length > 0">
                            <div class="card">
                                <div class="card-body">
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
    mounted: async function(){
      this.searched = this.$route.params.searched;
      var scope = this;


      if(this.searched !==undefined){
      workService.searchWork(this.searched, function(data) {
          scope.artworks = data;

          auctionService.searchAuction(data, function(res){

          });
      });
     }
    }
})
