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
                    <div class="col-md-3 artwork" v-for="item in artworks">
                        <div class="card">
                            <div class="card-body">
                                <img src="./assets/images/artworks/artwork1.jpg">
                                <h4>{{ item["이름"] }}</h4>
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
                                <li class="page-item disabled"><a class="page-link" href="#">이전</a></li>
                                <li class="page-item"><a class="page-link" href="#">1</a></li>
                                <li class="page-item"><a class="page-link" href="#">2</a></li>
                                <li class="page-item"><a class="page-link" href="#">3</a></li>
                                <li class="page-item"><a class="page-link" href="#">4</a></li>
                                <li class="page-item"><a class="page-link" href="#">5</a></li>
                                <li class="page-item"><a class="page-link" href="#">6</a></li>
                                <li class="page-item"><a class="page-link" href="#">7</a></li>
                                <li class="page-item"><a class="page-link" href="#">8</a></li>
                                <li class="page-item"><a class="page-link" href="#">9</a></li>
                                <li class="page-item"><a class="page-link" href="#">10</a></li>
                                <li class="page-item"><a class="page-link" href="#">다음</a></li>
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
                "설명": ""
            }]
        }
    },
    mounted: function(){
        var scope = this;

        workService.findAll(function(data){
            scope.artworks = data;
        });
    }
})