var homeView = Vue.component("Home", {
    props: ["isSigned"],
    template: `
        <div>
            <v-nav></v-nav>
            <div id="main-overview" class="container home-margin">
                <div class="col text-center">
                        <h2 class="pixelFont">FIGURE AUCTION</h2>
                        <h4>블록체인 기반 피규어 경매를 시작해보세요.</h4>

                        <input v-model="message" placeholder="Search">
                          <router-link :to="{ name: 'search', params: { searched: message } }" class="btn">검색</router-link>
                </div>

              <h6 class="home-margin">최근 게시된 작품</h6>
              <div class="col text-center">
                <div class="row">
                <div class="col-md-4 artwork" v-for="item in artworks">
                    <div class="card">
                        <div class="card-body">
                            <img :src="item['작품이미지']">
                            <h4>{{ item['이름'] }}</h4>
                            <p v-if="item['설명'] != null">{{ item["설명"] }}</p>
                            <p v-if="item['설명'] == null">-</p>
                            <router-link :to="{ name: 'work.detail', params: { id: item['id'] } }" class="btn btn-block btn-secondary">이력보기</router-link>
                        </div>
                    </div>
                </div>
                </div>
              </div>

              <h6 class="home-margin">최근 게시된 경매</h6>
              <div class="col text-center">
              <div class="row">
                  <div class="col-md-4 auction" v-for="item in auctions">
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
              </div>

            </div>
        </div>
    `,
    data() {
        return {
            sharedState: store.state,
            message: "",
            artworks: [],
            auctions: []
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
      }
    },
    mounted: function() {
      var scope = this;
      workService.findAll(function(data){
          scope.artworks.push(data[0]);
          scope.artworks.push(data[1]);
          scope.artworks.push(data[2]);
      });

      auctionService.findAll(function(data){
          var result = data;

          function fetchData(start, end){
              if(start == end) {
                scope.auctions.push(result[0]);
                scope.auctions.push(result[1]);
                scope.auctions.push(result[2]);
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
})
