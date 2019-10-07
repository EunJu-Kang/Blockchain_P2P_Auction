var artworksView = Vue.component('artworksView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="Artworks" description="작품을 둘러볼 수 있습니다."></v-breadcrumb>
            <div id="artwork-list" class="container">
                <div class="row">
                    <div class="col-md-12 text-right">
                        <router-link to="/works/create" class="btn btn-outline-secondary">내 작품 등록하기</router-link>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 artwork" v-for="item in pageArtwork">
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
                <div class="row">
                    <div class="col-md-12 text-center">
                        <nav class="bottom-pagination">
                            <ul class="pagination">
                                <li class="page-item" :class="{disabled:currentPage == 1}"><a class="page-link" @click="movePage(1)">맨앞</a></li>
                                <li class="page-item" :class="{disabled:currentPage == 1}"><a class="page-link" @click="prevPage">prev</a></li>
                                <li class="page-item" v-for="idx in pageCount"><a class="page-link" href="#" @click="movePage(idx)">{{idx}}</a></li>
                                <li class="page-item" :class="{disabled:currentPage == pageCount}"><a class="page-link" @click="nextPage">next</a></li>
                                <li class="page-item" :class="{disabled:currentPage == pageCount}"><a class="page-link" @click="movePage(pageCount)">맨뒤</a></li>
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
            pageArtwork:[],
            pageCount: 0,
            perPage:8,
            currentPage: 1
        }
    },
    methods: {
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
        var end = this.artworks.length;
        if(end>start+this.perPage){
          end = start+this.perPage
        }
        this.pageArtwork = [];
        for(var i = start; i<end; i++){
          this.pageArtwork.push(this.artworks[i])
        }
      }
    },
    mounted: function(){
        var scope = this;
        workService.findAll(function(data){
            scope.artworks = data;
            scope.pageCount = Math.ceil(data.length /scope.perPage);
            console.log(data)
            scope.movePage(1)
        });
    }
})
