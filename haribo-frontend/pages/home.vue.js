var homeView = Vue.component("Home", {
    props: ["isSigned"],
    template: `
        <div>
            <v-nav></v-nav>
            <div id="main-overview" class="container home-margin">
                <div class="col text-center">
                  <h1 class="pixelFont">FIGURE AUCTION</h1>
                  <br>
                  <h4>블록체인 기반 피규어 경매를 시작해보세요.</h4>
                  <div class='containers'>
                    <div class='search-box-container'>
                      <button class='submit'>
                        <i class='fa fa-search'></i>
                      </button>
                      <input class='search-box' v-model="value" @keyup.enter="Enter" placeholder="Search">
                    </div>
                    <h3 class='response'></h3>
                  </div>
                </div>

              <p class="home-margin">최근 게시된 작품</p>
              <div class="col text-center">
                <div class="row">
                  <div class="col-md-4 artwork" v-for="item in artworks">
                      <div class="card">
                          <div class="card-body">
                              <img :src="item['작품이미지']">
                              <h4>{{ item['이름'] }}</h4>
                              <p v-if="item['설명'] != null">{{ item["설명"] }}</p>
                              <p v-if="item['설명'] == null">-</p>
                              <router-link :to="{ name: 'work.detail', params: { id: item['id'] } }" class="btn btn-block btn-style" style="margin-left:0px;">이력보기</router-link>
                          </div>
                      </div>
                  </div>
                </div>
              </div>

              <p class="home-margin">최근 게시된 경매</p>
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
            auctions: [],
            value: ""
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
      Enter() {
        this.$router.push({ name: 'search', params: { searched: this.value }})
      }
    },
    mounted: function() {
      var scope = this;

      workService.findAll(function(data){
          scope.artworks.push(data[0]);
          scope.artworks.push(data[1]);
          scope.artworks.push(data[2]);
      });

      auctionService.homeAuction(function(data){
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

      $.fn.toggleState = function(b) {
      	$(this).stop().animate({
      		width: b ? "450px" : "50px"
      	}, 600, "easeOutElastic" );
      }

      $(document).ready(function() {
      	var container = $(".container");
      	var boxContainer = $(".search-box-container");
      	var submit = $(".submit");
      	var searchBox = $(".search-box");
      	var response = $(".response");
      	var isOpen = false;
      	submit.on("mousedown", function(e) {
      		e.preventDefault();
      		boxContainer.toggleState(!isOpen);
      		isOpen = !isOpen;
      		if(!isOpen) {
      			handleRequest();
      		} else {
      			searchBox.focus();
      		}
      	});
      	searchBox.blur(function() {
      		boxContainer.toggleState(false);
      		isOpen = false;
      	});
      });
    }
})
