var artworksView = Vue.component('artworksView', {
  template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Artworks" description="작품을 둘러볼 수 있습니다."></v-breadcrumb>
            <div id="artwork-list" class="container">
                <div class="row">
                    <div class="col-md-6">
                    <input v-model="message" placeholder="작품">
                    <button type="button" class="btn btn-outline-secondary col-md-0.5" v-on:click="search">검색</button>
                    </div>
                    <div class="col-md-6 text-right">
                        <router-link to="/works/create" class="btn btn-outline-secondary">내 작품 등록하기</router-link>
                    </div>
                </div>
                <div class="row">
                <div class="col-md-3 artwork" v-for="item in pageArtwork">
                        <div class="card" id="auctionlistcss">
                            <div class="card-body">
                               <img :src="item['작품이미지']">
                                <h5 class="card-title">{{ item['이름']  | truncate(10) }}</h5>
                                <p class="card-text" v-if="item['설명'] != null">{{ item["설명"] | truncate(10)}}</p>
                                <p class="card-text" v-if="item['설명'] == null">-</p>
                                <router-link :to="{ name: 'work.detail', params: { id: item['id'] }}" class="btn  btn-block btn-secondary">작품자세히보기</router-link>
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
                                <li class="page-item" v-for="idx in pages">
                                  <a class="page-link" @click="movePage(idx)">
                                    <span class="pagination_curr" v-if="idx==currentPage">{{idx}}</span>
                                    <span class="pagination_page" v-else >{{idx}}</span>
                                  </a>
                                </li>
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
      artworks: [{
        "이름": "",
        "설명": "",
        "작품이미지": ""
      }],
      pageArtwork: [],
      pageCount: 0,
      perPage: 8,
      currentPage: 1,
      message: "",
      pages: []

    }
  },
  methods: {
    search() {
      var scope = this;
      if (this.message != "") {
        workService.searchWork(this.message, function (data) {
          scope.artworks = data;
          scope.pageCount = Math.ceil(data.length / scope.perPage);
          scope.movePage(1);
        });
      } else {
        this.findAll();
      }
    },
    nextPage() {
      this.currentPage++;
      this.movePage(this.currentPage);

    },
    prevPage() {
      this.currentPage--;
      this.movePage(this.currentPage);

    },
    movePage(p) {
      this.currentPage = p;
      var start = (p - 1) * this.perPage
      var end = this.artworks.length;
      if (end > start + this.perPage) {
        end = start + this.perPage
      }
      this.pageArtwork = [];
      for (var i = start; i < end; i++) {
        this.pageArtwork.push(this.artworks[i])
      }
      this.pages = [];
      var stPage = this.currentPage - 2 <= 0 ? 1 : this.currentPage - 2;
      var end = stPage + 5 > this.pageCount ? this.pageCount + 1 : stPage + 5;
      for (var i = stPage; i < end; i++) {
        this.pages.push(i);
      }
    },
    findAll() {
      var scope = this;
      workService.findAll(function (data) {
        scope.artworks = data;
        scope.pageCount = Math.ceil(data.length / scope.perPage);
        scope.movePage(1)
      });
    }
  },
  mounted: function () {
    this.findAll();
  }
})
